package top.fifthlight.mergetools.merger.plugin.expectactual;

import top.fifthlight.mergetools.processor.ActualData;

import java.util.Map;

public interface ExpectActualPluginContext {
    Map<String, ActualData> getActualDataMap();
}
