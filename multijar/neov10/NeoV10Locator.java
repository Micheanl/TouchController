package top.fifthlight.multijar.neov10;

import net.neoforged.fml.jarcontents.JarContents;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.fml.util.ClasspathResourceUtils;
import net.neoforged.neoforgespi.language.IModInfo;
import net.neoforged.neoforgespi.locating.IDependencyLocator;
import net.neoforged.neoforgespi.locating.IDiscoveryPipeline;
import net.neoforged.neoforgespi.locating.IModFile;
import net.neoforged.neoforgespi.locating.ModFileDiscoveryAttributes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.fifthlight.multijar.common.MultiJarManifest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.security.DigestOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class NeoV10Locator implements IDependencyLocator {
    private static final Logger LOGGER = LoggerFactory.getLogger(NeoV10Locator.class);
    private final ModFileDiscoveryAttributes attributes;

    @SuppressWarnings("ReturnValueIgnored")
    public NeoV10Locator() throws NoSuchMethodException, NoSuchFieldException {
        IDependencyLocator.class.getMethod("scanMods", List.class, IDiscoveryPipeline.class);
        IModFile.class.getMethod("getModInfos");
        IModInfo.class.getMethod("getModId");
        IModInfo.class.getMethod("getVersion");
        JarContents.class.getMethod("ofPath", Path.class);
        JarContents.class.getMethod("findFile", String.class);
        IDiscoveryPipeline.class.getMethod("readModFile", JarContents.class, ModFileDiscoveryAttributes.class);
        IDiscoveryPipeline.class.getMethod("addModFile", IModFile.class);
        ModFileDiscoveryAttributes.class.getField("DEFAULT");

        attributes = ModFileDiscoveryAttributes.DEFAULT.withDependencyLocator(this);
    }

    private static Optional<IModInfo> findModInfo(List<IModFile> loadedMods, String modId) {
        return loadedMods.stream()
                .flatMap(file -> file.getModInfos().stream())
                .filter(info -> Objects.equals(info.getModId(), modId))
                .findFirst();
    }

    @Override
    public void scanMods(List<IModFile> loadedMods, IDiscoveryPipeline pipeline) {
        var minecraftInfo = findModInfo(loadedMods, "minecraft");
        if (minecraftInfo.isEmpty()) {
            LOGGER.error("Could not find minecraft mod!");
            return;
        }
        var minecraftVersionStr = minecraftInfo.get().getVersion().toString();
        var gameDirectory = FMLPaths.GAMEDIR.get();

        LOGGER.info("MultiJar loader on Minecraft {} in directory {}", minecraftVersionStr, gameDirectory);

        if (!FMLEnvironment.isProduction()) {
            for (var path : ClasspathResourceUtils.findFileSystemRootsOfFileOnClasspath(
                    MultiJarManifest.NEOFORGE_MANIFEST_PATH)) {
                if (!Files.isRegularFile(path)) continue;
                try (var contents = JarContents.ofPath(path)) {
                    processJar(contents, minecraftVersionStr, pipeline);
                } catch (IOException e) {
                    LOGGER.warn("Failed to read mod {} in classpath", path);
                }
            }
        }

        var modsDir = gameDirectory.resolve(FMLPaths.MODSDIR.relative()).toAbsolutePath().normalize();
        if (!Files.exists(modsDir)) {
            return;
        }

        List<Path> jarFiles;
        try (var files = Files.list(modsDir)) {
            jarFiles = files
                    .filter(p -> p.getFileName().toString().toLowerCase(Locale.ROOT).endsWith(".jar"))
                    .sorted(Comparator.comparing(p -> p.getFileName().toString().toLowerCase(Locale.ROOT)))
                    .toList();
        } catch (IOException e) {
            LOGGER.warn("Failed to list mods directory {}", modsDir, e);
            return;
        }

        for (var path : jarFiles) {
            if (!Files.isRegularFile(path)) continue;
            try {
                if (Files.size(path) == 0) continue;
            } catch (IOException ignored) {
            }

            try (var contents = JarContents.ofPath(path)) {
                processJar(contents, minecraftVersionStr, pipeline);
            } catch (IOException e) {
                LOGGER.warn("Failed to read mod {}", path);
            }
        }
    }

    private void processJar(JarContents contents, String minecraftVersionStr, IDiscoveryPipeline pipeline) throws IOException {
        var manifestStream = contents.openFile(MultiJarManifest.NEOFORGE_MANIFEST_PATH);
        if (manifestStream == null) {
            return;
        }

        MultiJarManifest manifest;
        try (var reader = new BufferedReader(new InputStreamReader(manifestStream))) {
            manifest = MultiJarManifest.fromJson(reader);
        } catch (Exception e) {
            LOGGER.warn("Failed to parse loader manifest for mod {}", contents.getPrimaryPath(), e);
            return;
        }

        LOGGER.info("Loading mod {}", contents.getPrimaryPath());

        var jars = manifest.jars(minecraftVersionStr);
        for (var jar : jars) {
            var jij = contents.findFile(jar);
            if (jij.isEmpty()) {
                LOGGER.warn("Failed to find jar {} for mod {}", jar, contents.getPrimaryPath());
                continue;
            }
            LOGGER.info("Loading jar {} for mod {}", jar, contents.getPrimaryPath());

            var jijCacheDir = FMLPaths.JIJ_CACHEDIR.get();
            Path tempFile;
            try {
                tempFile = Files.createTempFile(jijCacheDir, "_jij", ".tmp");
            } catch (IOException e) {
                LOGGER.error("Failed to create temp file in {}: {}", jijCacheDir, e);
                continue;
            }

            var filename = jar.substring(jar.lastIndexOf('/') + 1);
            Path finalPath;
            try {
                var checksum = extractEmbeddedJarFile(contents, jar, tempFile);
                finalPath = jijCacheDir.resolve(checksum + "/" + filename);
                if (!Files.isRegularFile(finalPath)) {
                    moveExtractedFileIntoPlace(tempFile, finalPath);
                }
            } finally {
                try {
                    Files.deleteIfExists(tempFile);
                } catch (IOException e) {
                    LOGGER.error("Failed to remove temp file {}: {}", tempFile, e);
                }
            }

            JarContents jijContents;
            try {
                jijContents = JarContents.ofPath(finalPath);
            } catch (IOException e) {
                LOGGER.error("Failed to read JiJ file {} from mod {} to {}", jar, contents.getPrimaryPath(), finalPath, e);
                continue;
            }
            var jijModFile = pipeline.readModFile(jijContents, attributes);
            pipeline.addModFile(jijModFile);
        }
    }

    private static String extractEmbeddedJarFile(JarContents contents, String relativePath, Path destination) {
        try (var inStream = contents.openFile(relativePath); var outStream = Files.newOutputStream(destination)) {
            if (inStream == null) {
                throw new IOException("Mod file does not contain " + relativePath);
            }

            MessageDigest digest;
            try {
                digest = MessageDigest.getInstance("SHA-256");
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }

            var digestOut = new DigestOutputStream(outStream, digest);
            inStream.transferTo(digestOut);

            return HexFormat.of().formatHex(digest.digest());
        } catch (IOException e) {
            LOGGER.error("Failed to copy JiJ file {} to {}", relativePath, destination, e);
            throw new UncheckedIOException(e);
        }
    }

    private static void moveExtractedFileIntoPlace(Path source, Path destination) {
        try {
            Files.createDirectories(destination.getParent());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        try {
            try {
                Files.move(source, destination, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
            } catch (AtomicMoveNotSupportedException ex) {
                Files.move(source, destination, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public String toString() {
        return "MultiJar NeoV10";
    }
}
