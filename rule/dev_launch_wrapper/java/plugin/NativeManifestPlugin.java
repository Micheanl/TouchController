package top.fifthlight.fabazel.devlaunchwrapper.plugin;

import top.fifthlight.fabazel.devlaunchwrapper.DevLaunchContext;
import top.fifthlight.fabazel.devlaunchwrapper.DevLaunchPlugin;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.stream.Collectors;

public class NativeManifestPlugin implements DevLaunchPlugin {
    @Override
    public int priority() {
        return 100;
    }

    @Override
    public void load(DevLaunchContext context) throws IOException {
        String manifestPath = DevLaunchPlugin.property("nativeManifest");
        if (manifestPath == null) {
            return;
        }

        Path nativesDir = Paths.get("natives");
        Files.createDirectories(nativesDir);
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(context.resolveRunfile(manifestPath)))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isEmpty()) {
                    continue;
                }
                String[] entries = line.split(":");
                if (entries.length < 2) {
                    throw new IllegalArgumentException("Invalid native manifest entry: " + line);
                }
                Path path;
                if (entries[0].startsWith("external/")) {
                    path = Paths.get(context.resolveRunfile(entries[0].substring(9)));
                } else {
                    path = Paths.get(context.resolveRunfile(entries[0]));
                }
                List<String> excludes = Arrays.stream(entries).skip(1).collect(Collectors.toList());
                try (JarInputStream jis = new JarInputStream(Files.newInputStream(path))) {
                    JarEntry entry;
                    outer:
                    while ((entry = jis.getNextJarEntry()) != null) {
                        for (String exclude : excludes) {
                            if (entry.getName().startsWith(exclude)) {
                                continue outer;
                            }
                        }
                        Path targetPath = nativesDir.resolve(entry.getName());
                        Files.createDirectories(targetPath.getParent());
                        Files.copy(jis, targetPath, StandardCopyOption.REPLACE_EXISTING);
                    }
                }
            }
        }
        System.setProperty("org.lwjgl.librarypath", nativesDir.toAbsolutePath().toString());
    }
}
