package top.fifthlight.fabazel.devlaunchwrapper;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public interface DevLaunchPlugin {
    static String property(String key) {
        return System.getProperty("dev.launch." + key);
    }

    static String property(String key, String defaultValue) {
        return System.getProperty("dev.launch." + key, defaultValue);
    }

    static String env(String key) {
        return System.getenv(key);
    }

    static void writeString(Path path, String content) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            writer.write(content);
        }
    }

    static String readString(Path path) throws IOException {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            char[] buf = new char[4096];
            int len;
            while ((len = reader.read(buf)) != -1) {
                content.append(buf, 0, len);
            }
        }
        return content.toString();
    }

    int priority();

    void load(DevLaunchContext context) throws Exception;
}
