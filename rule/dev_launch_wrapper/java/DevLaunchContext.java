package top.fifthlight.fabazel.devlaunchwrapper;

import com.google.devtools.build.runfiles.Runfiles;

import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DevLaunchContext implements AttributeEnvironment {
    private final Runfiles runfiles;
    private final Path workDir;
    private final List<String> argsList;
    private final Map<ContextAttributeKey<?>, Object> attributes = new HashMap<>();
    private String mainClass;

    public DevLaunchContext(Runfiles runfiles, Path workDir, List<String> argsList) {
        this.runfiles = runfiles;
        this.workDir = workDir;
        this.argsList = argsList;
    }

    public Path workDir() {
        return workDir;
    }

    public String getMainClass() {
        return mainClass;
    }

    public void setMainClass(String mainClass) {
        this.mainClass = mainClass;
    }

    public void addArg(String arg) {
        argsList.add(arg);
    }

    public void addArgs(String... args) {
        Collections.addAll(argsList, args);
    }

    public String resolveRunfile(String path) {
        return runfiles.rlocation(path);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <K extends ContextAttributeKey<T>, T> T getAttribute(K key) {
        return (T) attributes.get(key);
    }

    @Override
    public <K extends ContextAttributeKey<T>, T> void putAttribute(K key, T value) {
        attributes.put(key, value);
    }
}
