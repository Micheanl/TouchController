package top.fifthlight.combine.core.widget

import androidx.compose.runtime.Composable
import top.fifthlight.combine.core.layout.Layout
import top.fifthlight.combine.core.layout.measure.MeasurePolicy
import top.fifthlight.combine.core.modifier.Modifier
import top.fifthlight.combine.core.node.NodeRenderer

private val canvasDefaultMeasurePolicy = MeasurePolicy { _, constraints ->
    layout(
        width = constraints.minWidth,
        height = constraints.minHeight
    ) {
    }
}

@Composable
fun Canvas(
    modifier: Modifier = Modifier,
    measurePolicy: MeasurePolicy = canvasDefaultMeasurePolicy,
    renderer: NodeRenderer,
) {
    Layout(
        modifier = modifier,
        measurePolicy = measurePolicy,
        renderer = renderer,
        content = {}
    )
}
