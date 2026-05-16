package top.fifthlight.combine.core.widget

import androidx.compose.runtime.Composable
import top.fifthlight.combine.core.data.Text
import top.fifthlight.combine.core.layout.Layout
import top.fifthlight.combine.core.layout.constraints.Constraints
import top.fifthlight.combine.core.layout.measure.Measurable
import top.fifthlight.combine.core.layout.measure.MeasurePolicy
import top.fifthlight.combine.core.layout.measure.MeasureResult
import top.fifthlight.combine.core.layout.measure.MeasureScope
import top.fifthlight.combine.core.modifier.Modifier
import top.fifthlight.combine.core.paint.Color
import top.fifthlight.combine.core.paint.Colors
import top.fifthlight.combine.core.text.TextMeasurer
import top.fifthlight.data.IntOffset

@Composable
fun BaseText(
    text: Text,
    modifier: Modifier = Modifier,
    color: Color = Colors.WHITE,
) = Layout(
    modifier = modifier,
    measurePolicy = object : MeasurePolicy {
        override fun MeasureScope.measure(measurables: List<Measurable>, constraints: Constraints): MeasureResult {
            val measureResult = if (constraints.maxWidth == Int.MAX_VALUE) {
                TextMeasurer.measure(text)
            } else {
                TextMeasurer.measure(text, constraints.maxWidth)
            }
            return layout(measureResult.width, measureResult.height) {}
        }

        override fun MeasureScope.minIntrinsicWidth(measurables: List<Measurable>, height: Int): Int =
            TextMeasurer.measure(text).width

        override fun MeasureScope.minIntrinsicHeight(measurables: List<Measurable>, width: Int): Int =
            TextMeasurer.measure(text, width).height

        override fun MeasureScope.maxIntrinsicWidth(measurables: List<Measurable>, height: Int): Int =
            TextMeasurer.measure(text).width

        override fun MeasureScope.maxIntrinsicHeight(measurables: List<Measurable>, width: Int): Int =
            TextMeasurer.measure(text, width).height
    },
    renderer = { canvas, node ->
        canvas.drawText(IntOffset.ZERO, node.width, text, color)
    }
)

@Composable
fun BaseText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Colors.WHITE,
) = Layout(
    modifier = modifier,
    measurePolicy = object : MeasurePolicy {
        override fun MeasureScope.measure(measurables: List<Measurable>, constraints: Constraints): MeasureResult {
            val measureResult = if (constraints.maxWidth == Int.MAX_VALUE) {
                TextMeasurer.measure(text)
            } else {
                TextMeasurer.measure(text, constraints.maxWidth)
            }
            return layout(measureResult.width, measureResult.height) {}
        }

        override fun MeasureScope.minIntrinsicWidth(measurables: List<Measurable>, height: Int): Int =
            TextMeasurer.measure(text).width

        override fun MeasureScope.minIntrinsicHeight(measurables: List<Measurable>, width: Int): Int =
            TextMeasurer.measure(text, width).height

        override fun MeasureScope.maxIntrinsicWidth(measurables: List<Measurable>, height: Int): Int =
            TextMeasurer.measure(text).width

        override fun MeasureScope.maxIntrinsicHeight(measurables: List<Measurable>, width: Int): Int =
            TextMeasurer.measure(text, width).height
    },
    renderer = { canvas, node ->
        canvas.drawText(IntOffset.ZERO, node.width, text, color)
    }
)
