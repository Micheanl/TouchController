package top.fifthlight.fabazel.devlaunchwrapper.plugin;

import top.fifthlight.fabazel.devlaunchwrapper.DevLaunchContext;
import top.fifthlight.fabazel.devlaunchwrapper.DevLaunchPlugin;

public class MainClassPlugin implements DevLaunchPlugin {
    @Override
    public int priority() {
        return 800;
    }

    @Override
    public void load(DevLaunchContext context) {
        String mainClass = DevLaunchPlugin.property("mainClass");
        if (mainClass == null) {
            return;
        }
        context.setMainClass(mainClass);
    }
}
