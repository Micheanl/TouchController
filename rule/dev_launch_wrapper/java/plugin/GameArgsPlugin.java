package top.fifthlight.fabazel.devlaunchwrapper.plugin;

import top.fifthlight.fabazel.devlaunchwrapper.ContextAttributeKey;
import top.fifthlight.fabazel.devlaunchwrapper.DevLaunchContext;
import top.fifthlight.fabazel.devlaunchwrapper.DevLaunchPlugin;

public class GameArgsPlugin implements DevLaunchPlugin {
    public static final ContextAttributeKey<String> VERSION_KEY =
            ContextAttributeKey.create("version");

    @Override
    public int priority() {
        return 500;
    }

    @Override
    public void load(DevLaunchContext context) {
        context.addArgs("--gameDir", context.workDir().toString());
        String version = DevLaunchPlugin.property("version");
        if (version != null) {
            context.addArgs("--version", version);
            context.putAttribute(VERSION_KEY, version);
        }
    }
}
