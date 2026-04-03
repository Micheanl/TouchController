package top.fifthlight.mergetools.processor;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;

public record AspectData(
        @JsonProperty("aspectProviderInterface") String aspectProviderInterface,
        @JsonProperty("aspectProviderFactory") String aspectProviderFactory,
        @JsonProperty("expects") ExpectEntry[] expects
) {
    @Override
    public String toString() {
        return "AspectData{" +
                "aspectProviderInterface='" + aspectProviderInterface + '\'' +
                ", aspectProviderFactory='" + aspectProviderFactory + '\'' +
                ", expects=" + Arrays.toString(expects) +
                '}';
    }

    public record ExpectEntry(
            @JsonProperty("interfaceName") String interfaceName,
            @JsonProperty("constructors") ExpectData.Constructor[] constructors
    ) {
        @Override
        public String toString() {
            return "ExpectEntry{" +
                    "interfaceName='" + interfaceName + '\'' +
                    ", constructors=" + Arrays.toString(constructors) +
                    '}';
        }
    }
}
