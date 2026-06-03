package top.fifthlight.fabazel.devlaunchwrapper;

import com.google.devtools.build.runfiles.AutoBazelRepository;
import com.google.devtools.build.runfiles.Runfiles;

import java.lang.reflect.Method;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@AutoBazelRepository
public class DevLaunchWrapper {
    public static void main(String[] args) throws Exception {
        Runfiles runfiles = Runfiles.preload().withSourceRepository(AutoBazelRepository_DevLaunchWrapper.NAME);
        Path workDir = Paths.get(".").toAbsolutePath();
        List<String> argsList = new ArrayList<>(Arrays.asList(args));
        DevLaunchContext context = new DevLaunchContext(runfiles, workDir, argsList);

        List<DevLaunchPlugin> plugins = new ArrayList<>();
        for (DevLaunchPlugin plugin : ServiceLoader.load(DevLaunchPlugin.class)) {
            plugins.add(plugin);
        }
        plugins.sort(Comparator.comparingInt(DevLaunchPlugin::priority));

        for (DevLaunchPlugin plugin : plugins) {
            plugin.load(context);
        }

        String mainClass = context.getMainClass();
        if (mainClass == null) {
            throw new IllegalArgumentException("No main class specified. Specify your real main class with dev.launch.mainClass JVM property.");
        }

        System.err.println("Launching game with arguments: " + String.join(" ", argsList));
        String[] array = argsList.toArray(new String[0]);

        Class<?> clazz = ClassLoader.getSystemClassLoader().loadClass(mainClass);
        Method mainMethod = clazz.getMethod("main", String[].class);
        mainMethod.invoke(null, (Object) array);
    }
}
