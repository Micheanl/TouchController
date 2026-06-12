package top.fifthlight.combine.example.widgetfactory.v1_21_11

import com.terraformersmc.modmenu.api.ConfigScreenFactory
import com.terraformersmc.modmenu.api.ModMenuApi
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
import net.minecraft.client.KeyMapping
import net.minecraft.client.gui.screens.Screen
import net.minecraft.resources.Identifier
import org.lwjgl.glfw.GLFW
import top.fifthlight.combine.core.data.Text
import top.fifthlight.combine.core.screen.ScreenFactoryFactory
import top.fifthlight.combine.example.widgetfactory.common.WidgetFactory

class WidgetFactoryMod : ClientModInitializer, ModMenuApi {
    companion object {
        private val keyCategory =
            KeyMapping.Category.register(Identifier.fromNamespaceAndPath("combine", "example"))
        private val keyMapping = KeyMapping("combine_widget_factory", GLFW.GLFW_KEY_H, keyCategory)
    }

    override fun getModConfigScreenFactory() = ConfigScreenFactory { parent ->
        ScreenFactoryFactory.of().getScreen(
            parent = parent,
            renderBackground = true,
            title = Text.literal("Widget Factory"),
        ) {
            WidgetFactory()
        } as Screen
    }

    override fun onInitializeClient() {
        KeyBindingHelper.registerKeyBinding(keyMapping)
        ClientTickEvents.END_CLIENT_TICK.register { client ->
            if (!keyMapping.isDown) {
                return@register
            }
            if (client.screen != null) {
                return@register
            }
            client.setScreen(
                ScreenFactoryFactory.of().getScreen(
                    parent = null,
                    title = Text.literal("Widget Factory"),
                ) {
                    WidgetFactory()
                } as Screen
            )
        }
    }
}
