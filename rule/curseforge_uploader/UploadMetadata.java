package top.fifthlight.fabazel.curseforge;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.List;

@NullMarked
@JsonInclude(JsonInclude.Include.NON_NULL)
public record UploadMetadata(
        String changelog,
        String changelogType,
        String displayName,
        @Nullable Integer parentFileID,
        List<Integer> gameVersions,
        String releaseType,
        @Nullable Boolean isMarkedForManualRelease,
        @Nullable Relations relations
) {
    @NullMarked
    public record Relations(
            List<ProjectRelation> projects
    ) {
    }

    @NullMarked
    public record ProjectRelation(
            String slug,
            // projectID 是可选的映射
            @Nullable Integer projectID,
            String type
    ) {
    }
}
