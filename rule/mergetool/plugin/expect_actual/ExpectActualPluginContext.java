package top.fifthlight.mergetools.merger.plugin.expectactual;

import top.fifthlight.mergetools.processor.ActualData;
import top.fifthlight.mergetools.processor.AspectData;

import java.util.Map;

public interface ExpectActualPluginContext {
    Map<String, ActualData> getActualDataMap();
    Map<String, AspectData.ExpectEntry> getUpstreamExpectsMap();
    String getAspectProviderInterface();
    String getAspectProviderFactory();
}
