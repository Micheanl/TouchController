package top.fifthlight.fabazel.devlaunchwrapper.plugin;

import top.fifthlight.fabazel.devlaunchwrapper.DevLaunchContext;
import top.fifthlight.fabazel.devlaunchwrapper.DevLaunchPlugin;

public class FMLPlugin implements DevLaunchPlugin {
    @Override
    public int priority() {
        return 550;
    }

    @Override
    public void load(DevLaunchContext context) {
        String neoFormVersion = DevLaunchPlugin.property("neoFormVersion");
        if (neoFormVersion == null) {
            return;
        }
        String version = context.getAttribute(GameArgsPlugin.VERSION_KEY);
        if (version != null) {
            context.addArgs("--fml.mcVersion", version);
        }
        context.addArgs("--fml.neoFormVersion", neoFormVersion);
    }
}
