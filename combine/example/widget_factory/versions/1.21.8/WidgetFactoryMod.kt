package top.fifthlight.combine.example.widgetfactory.v1_21_8

import com.terraformersmc.modmenu.api.ConfigScreenFactory
import com.terraformersmc.modmenu.api.ModMenuApi
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
import net.minecraft.client.KeyMapping
import net.minecraft.client.gui.screens.Screen
import org.lwjgl.glfw.GLFW
import top.fifthlight.combine.core.data.TextFactoryFactory
import top.fifthlight.combine.core.screen.ScreenFactoryFactory
import top.fifthlight.combine.example.widgetfactory.common.WidgetFactory

class WidgetFactoryMod : ClientModInitializer, ModMenuApi {
    companion object {
        private val keyMapping =
            KeyMapping("combine_widget_factory", GLFW.GLFW_KEY_H, "combine.example")
    }

    override fun getModConfigScreenFactory() = ConfigScreenFactory { parent ->
        ScreenFactoryFactory.of().getScreen(
            parent = parent,
            renderBackground = true,
            title = TextFactoryFactory.of().literal("Widget Factory"),
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
                    title = TextFactoryFactory.of().literal("Widget Factory"),
                ) {
                    WidgetFactory()
                } as Screen
            )
        }
    }
}
