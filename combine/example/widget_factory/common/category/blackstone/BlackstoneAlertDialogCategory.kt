package top.fifthlight.combine.example.widgetfactory.common.category.blackstone

import androidx.compose.runtime.*
import top.fifthlight.combine.example.widgetfactory.common.category.WidgetCategory
import top.fifthlight.combine.theme.Theme
import top.fifthlight.combine.theme.blackstone.BlackstoneTheme
import top.fifthlight.combine.theme.blackstone.widget.AlertDialog
import top.fifthlight.combine.widget.Button
import top.fifthlight.combine.widget.GuideButton
import top.fifthlight.combine.widget.Text

object BlackstoneAlertDialogCategory : WidgetCategory() {
    override val name: String
        get() = "AlertDialog"

    override val themes: Set<Theme>
        get() = setOf(BlackstoneTheme)

    @Composable
    override fun Interface() {
        var openDialog by remember { mutableStateOf(false) }

        Button(onClick = {
            openDialog = true
        }) {
            Text("Open Dialog")
        }

        AlertDialog(
            visible = openDialog,
            onDismissRequest = { openDialog = false },
            title = { Text("Title") },
            actions = {
                GuideButton(onClick = { openDialog = false }) {
                    Text("Ok")
                }
                Button(onClick = { openDialog = false }) {
                    Text("Cancel")
                }
            },
        ) {
            Text("Dialog content")
        }
    }
}
