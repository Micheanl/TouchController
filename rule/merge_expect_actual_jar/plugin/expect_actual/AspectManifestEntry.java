package top.fifthlight.mergetools.merger.plugin.expectactual;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import top.fifthlight.mergetools.merger.api.MergeEntry;
import top.fifthlight.mergetools.processor.AspectData;

import java.io.OutputStream;

public class AspectManifestEntry implements MergeEntry {
    private static final ObjectMapper mapper = new ObjectMapper();
    static {
        mapper.disable(JsonGenerator.Feature.AUTO_CLOSE_TARGET);
    }

    private final AspectData aspectData;

    public AspectManifestEntry(AspectData aspectData) {
        this.aspectData = aspectData;
    }

    @Override
    public void write(OutputStream output) throws Exception {
        mapper.writeValue(output, aspectData);
    }
}
