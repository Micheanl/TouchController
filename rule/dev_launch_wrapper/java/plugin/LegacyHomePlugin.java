package top.fifthlight.fabazel.devlaunchwrapper.plugin;

import top.fifthlight.fabazel.devlaunchwrapper.DevLaunchContext;
import top.fifthlight.fabazel.devlaunchwrapper.DevLaunchPlugin;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class LegacyHomePlugin implements DevLaunchPlugin {
    @Override
    public int priority() {
        return 400;
    }

    @Override
    public void load(DevLaunchContext context) throws IOException {
        if (!"true".equals(DevLaunchPlugin.property("legacyHome"))) {
            return;
        }
        System.setProperty("user.home", context.workDir().toString());
        Path minecraftDir = Paths.get(".minecraft");
        System.out.println(minecraftDir.toAbsolutePath());
        Files.deleteIfExists(minecraftDir);
        Files.createSymbolicLink(minecraftDir, context.workDir());
    }
}
