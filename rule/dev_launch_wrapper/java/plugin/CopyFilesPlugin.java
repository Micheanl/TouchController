package top.fifthlight.fabazel.devlaunchwrapper.plugin;

import top.fifthlight.fabazel.devlaunchwrapper.DevLaunchContext;
import top.fifthlight.fabazel.devlaunchwrapper.DevLaunchPlugin;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public class CopyFilesPlugin implements DevLaunchPlugin {
    @Override
    public int priority() {
        return 200;
    }

    @Override
    public void load(DevLaunchContext context) throws IOException {
        String copyFiles = DevLaunchPlugin.property("copyFiles");
        if (copyFiles == null) {
            return;
        }

        Path workDir = context.workDir();
        String[] copyFileList = copyFiles.split(",");
        for (String entry : copyFileList) {
            int colonIndex = entry.indexOf(':');
            if (colonIndex == -1) {
                throw new IllegalArgumentException("Invalid copy file entry: " + entry);
            }
            String fromStr = entry.substring(0, colonIndex);
            Path from = Paths.get(context.resolveRunfile(fromStr)).toRealPath();
            Path to = workDir.resolve(entry.substring(colonIndex + 1));
            Files.createDirectories(to.getParent());
            if (Files.isDirectory(from)) {
                Files.walkFileTree(from, new CopyDirectoryVisitor(from, to, StandardCopyOption.REPLACE_EXISTING));
            } else {
                Files.copy(from, to, StandardCopyOption.REPLACE_EXISTING);
            }
        }
    }

    private static class CopyDirectoryVisitor extends SimpleFileVisitor<Path> {
        private final Path fromPath;
        private final Path toPath;
        private final CopyOption[] copyOptions;

        CopyDirectoryVisitor(Path fromPath, Path toPath, CopyOption... options) {
            this.fromPath = fromPath;
            this.toPath = toPath;
            this.copyOptions = options;
        }

        @Override
        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
            Path targetPath = toPath.resolve(fromPath.relativize(dir));
            Files.createDirectories(targetPath);
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
            Files.copy(file, toPath.resolve(fromPath.relativize(file)), copyOptions);
            return FileVisitResult.CONTINUE;
        }
    }
}
