package top.fifthlight.fabazel.devlaunchwrapper.plugin;

import top.fifthlight.fabazel.devlaunchwrapper.DevLaunchContext;
import top.fifthlight.fabazel.devlaunchwrapper.DevLaunchPlugin;

public class ExpandRunfilePlugin implements DevLaunchPlugin {
    @Override
    public int priority() {
        return 0;
    }

    @Override
    public void load(DevLaunchContext context) {
        String expandRunfileProperties = DevLaunchPlugin.property("expandRunfileProperties");
        if (expandRunfileProperties == null) {
            return;
        }
        for (String prop : expandRunfileProperties.split(",")) {
            String original = System.getProperty(prop);
            System.setProperty(prop, context.resolveRunfile(original));
        }
    }
}
