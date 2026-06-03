package top.fifthlight.fabazel.devlaunchwrapper.plugin;

import top.fifthlight.fabazel.devlaunchwrapper.DevLaunchContext;
import top.fifthlight.fabazel.devlaunchwrapper.DevLaunchPlugin;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TypePlugin implements DevLaunchPlugin {
    @Override
    public int priority() {
        return 700;
    }

    @Override
    public void load(DevLaunchContext context) throws IOException {
        String type = DevLaunchPlugin.property("type", "client");
        switch (type) {
            case "client": {
                Path allowSymlinksPath = Paths.get("allowed_symlinks.txt");
                DevLaunchPlugin.writeString(allowSymlinksPath, "[regex].*\n");
                break;
            }
            case "server": {
                Path serverPropertiesPath = Paths.get("server.properties");
                if (!Files.exists(serverPropertiesPath)) {
                    DevLaunchPlugin.writeString(serverPropertiesPath, "online-mode=false\n");
                }
                context.addArg("--nogui");
                Path eulaPath = Paths.get("eula.txt");
                DevLaunchPlugin.writeString(eulaPath, "eula=true\n");
                break;
            }
        }
    }
}
