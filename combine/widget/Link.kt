package top.fifthlight.combine.widget.ui

import androidx.compose.runtime.Composable
import top.fifthlight.combine.core.data.Text
import top.fifthlight.combine.core.data.TextStyle
import top.fifthlight.combine.core.modifier.Modifier
import top.fifthlight.combine.core.modifier.pointer.clickable
import top.fifthlight.combine.core.paint.Color
import top.fifthlight.combine.core.paint.Colors
import top.fifthlight.combine.ui.style.LocalTextStyle

@Composable
fun Link(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    color: Color = Colors.BLUE,
    textStyle: TextStyle = LocalTextStyle.current.copy(underline = true),
) {
    Text(
        text = text,
        modifier = Modifier.clickable(onClick = onClick).then(modifier),
        color = color,
        textStyle = textStyle
    )
}

@Composable
fun Link(
    text: Text,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    color: Color = Colors.BLUE,
    textStyle: TextStyle = LocalTextStyle.current.copy(underline = true),
) {
    Text(
        text = text,
        modifier = Modifier.clickable(onClick = onClick).then(modifier),
        color = color,
        textStyle = textStyle,
    )
}
