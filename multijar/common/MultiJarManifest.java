package top.fifthlight.multijar.common;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.io.Reader;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class MultiJarManifest {
    public static final String NEOFORGE_MANIFEST_PATH = "META-INF/jars/multijar-neoforge-manifest.json";
    public static final String FORGE_MANIFEST_PATH = "META-INF/jars/multijar-forge-manifest.json";

    private static final Gson GSON = new Gson();
    private static final Type MAP_TYPE = new TypeToken<Map<String, List<String>>>() {
    }.getType();
    private static final String COMMON_KEY = "common";

    private final Map<String, List<String>> entries;

    private MultiJarManifest(@NonNull Map<String, List<String>> entries) {
        this.entries = entries;
    }

    public static MultiJarManifest fromJson(@NonNull Reader reader) {
        Map<String, List<String>> map = GSON.fromJson(reader, MAP_TYPE);
        if (map == null) {
            map = Collections.emptyMap();
        }
        return new MultiJarManifest(map);
    }

    @NonNull
    public List<String> common() {
        return entries.getOrDefault(COMMON_KEY, Collections.emptyList());
    }

    @NonNull
    public List<String> forVersion(@NonNull String mcVersion) {
        return entries.getOrDefault(mcVersion, Collections.emptyList());
    }

    @NonNull
    public List<String> jars(@Nullable String mcVersion) {
        if (mcVersion == null) {
            return common();
        }
        return Stream.concat(common().stream(), forVersion(mcVersion).stream()).collect(Collectors.toList());
    }

    @NonNull
    public Set<String> versions() {
        return Collections.unmodifiableSet(entries.keySet());
    }

    @NonNull
    public Map<String, List<String>> entries() {
        return Collections.unmodifiableMap(entries);
    }
}
