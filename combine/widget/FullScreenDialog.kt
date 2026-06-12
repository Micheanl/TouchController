package top.fifthlight.combine.widget

import androidx.compose.runtime.Composable
import top.fifthlight.combine.core.screen.DismissHandler
import top.fifthlight.combine.core.widget.Popup

@Composable
fun FullScreenDialog(
    onDismissRequest: (() -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    DismissHandler(onDismissRequest != null) {
        onDismissRequest?.let { it() }
    }
    Popup(content = content)
}
