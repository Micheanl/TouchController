package top.fifthlight.combine.example.widgetfactory.common.category

import androidx.compose.runtime.*
import top.fifthlight.combine.core.data.Text
import top.fifthlight.combine.widget.EditText
import top.fifthlight.combine.widget.Text

object EditTextCategory : WidgetCategory() {
    override val name: String
        get() = "EditText"

    @Composable
    override fun Interface() {
        Text("EditText")

        run {
            var value by remember { mutableStateOf("Normal") }
            EditText(
                value = value,
                onValueChanged = { value = it },
            )
        }

        run {
            var value by remember { mutableStateOf("") }
            EditText(
                value = value,
                placeholder = Text.literal("Placeholder"),
                onValueChanged = { value = it },
            )
        }
    }
}
