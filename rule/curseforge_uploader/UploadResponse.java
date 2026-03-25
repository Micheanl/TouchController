package top.fifthlight.fabazel.curseforge;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jspecify.annotations.NullMarked;

/**
 * 上传成功后的响应结构
 * 使用 @JsonIgnoreProperties 确保 API 增加新字段时不会报错
 */
@NullMarked
@JsonIgnoreProperties(ignoreUnknown = true)
public record UploadResponse(
    int id
) {}
