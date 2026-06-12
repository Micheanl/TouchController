package top.fifthlight.combine.example.helloworld.common

import androidx.compose.runtime.*
import top.fifthlight.combine.core.layout.Alignment
import top.fifthlight.combine.core.layout.Arrangement
import top.fifthlight.combine.core.modifier.Modifier
import top.fifthlight.combine.core.modifier.placement.fillMaxSize
import top.fifthlight.combine.core.widget.layout.Box
import top.fifthlight.combine.core.widget.layout.Column
import top.fifthlight.combine.core.widget.layout.Row
import top.fifthlight.combine.theme.invoke
import top.fifthlight.combine.theme.vanilla.VanillaTheme
import top.fifthlight.combine.widget.Button
import top.fifthlight.combine.widget.Text

@Composable
fun HelloWorld() {
    VanillaTheme {
        Box(modifier = Modifier.fillMaxSize(), alignment = Alignment.Center) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4),
            ) {
                var i by remember { mutableStateOf(0) }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4),
                ) {
                    Text("Counter: $i")
                    Button(onClick = { i++ }) { Text("+") }
                }
            }
        }
    }
}
