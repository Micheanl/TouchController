package top.fifthlight.fabazel.devlaunchwrapper.plugin;

import top.fifthlight.fabazel.devlaunchwrapper.DevLaunchContext;
import top.fifthlight.fabazel.devlaunchwrapper.DevLaunchPlugin;

public class GlfwPlugin implements DevLaunchPlugin {
    @Override
    public int priority() {
        return 300;
    }

    @Override
    public void load(DevLaunchContext context) {
        String glfwLibName = DevLaunchPlugin.env("GLFW_LIBNAME");
        if (glfwLibName == null) {
            return;
        }
        System.setProperty("org.lwjgl.glfw.libname", glfwLibName);
    }
}
