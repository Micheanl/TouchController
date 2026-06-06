/*
 * SPDX-License-Identifier: LGPL-3.0-or-later
 * Copyright (C) 2026 fifth_light
 */

package top.fifthlight.touchcontroller.neoforge.v26_1_2

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.screens.Screen
import net.minecraft.resources.Identifier
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.IEventBus
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.ModContainer
import net.neoforged.fml.common.Mod
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent
import net.neoforged.neoforge.client.event.*
import net.neoforged.neoforge.client.event.lifecycle.ClientStartedEvent
import net.neoforged.neoforge.client.gui.IConfigScreenFactory
import net.neoforged.neoforge.client.gui.VanillaGuiLayers
import net.neoforged.neoforge.common.NeoForge
import net.neoforged.neoforge.event.level.block.BreakBlockEvent
import org.slf4j.LoggerFactory
import top.fifthlight.combine.backend.minecraft.render.v26_1.CanvasImpl
import top.fifthlight.touchcontroller.buildinfo.BuildInfo
import top.fifthlight.touchcontroller.common.config.data.StatusConfig
import top.fifthlight.touchcontroller.common.config.holder.GlobalConfigHolder
import top.fifthlight.touchcontroller.common.event.block.BlockBreakEvents
import top.fifthlight.touchcontroller.common.event.connection.ConnectionEvents
import top.fifthlight.touchcontroller.common.event.render.RenderEvents
import top.fifthlight.touchcontroller.common.event.tick.TickEvents
import top.fifthlight.touchcontroller.common.event.window.WindowEvents
import top.fifthlight.touchcontroller.common.model.ControllerHudModel
import top.fifthlight.touchcontroller.common.model.TouchControllerLoadStatus
import top.fifthlight.touchcontroller.common.ui.config.screen.getConfigScreen
import top.fifthlight.touchcontroller.gal.gameconfig.v26_1.GameConfigEditorImpl

@Mod("touchcontroller_26_1_2_neoforge", dist = [Dist.CLIENT])
class TouchController(modEventBus: IEventBus, private val container: ModContainer) {
    private val logger = LoggerFactory.getLogger(TouchController::class.java)

    init {
        modEventBus.addListener(::onClientSetup)
        modEventBus.addListener(::onLoadPlatformNativeWindow)
        modEventBus.addListener(::onRegisterHudHandler)
    }

    private fun onClientSetup(event: FMLClientSetupEvent) {
        logger.info("Loading TouchController…")

        initialize()

        TouchControllerLoadStatus.isLoaded = true
    }

    private fun onLoadPlatformNativeWindow(event: AddClientReloadListenersEvent) {
        WindowEvents.loadPlatformWindow()
    }

    private fun onRegisterHudHandler(event: RegisterGuiLayersEvent) {
        event.registerAbove(
            VanillaGuiLayers.BOSS_OVERLAY,
            Identifier.fromNamespaceAndPath(BuildInfo.MOD_ID, "hud")
        ) { guiGraphics, _ ->
            val client = Minecraft.getInstance()
            if (!client.options.hideGui) {
                val canvas = CanvasImpl(guiGraphics)
                RenderEvents.onHudRender(canvas)
            }
        }
    }

    private fun initialize() {
        container.registerExtensionPoint(IConfigScreenFactory::class.java, IConfigScreenFactory { _, parent ->
            getConfigScreen(parent) as Screen
        })

        NeoForge.EVENT_BUS.register(object {
            @SubscribeEvent
            fun onClientStarted(event: ClientStartedEvent) {
                GlobalConfigHolder.load()
                GameConfigEditorImpl.executePendingCallback()
            }

            @SubscribeEvent
            fun blockOutlineEvent(event: ExtractBlockOutlineRenderStateEvent) {
                event.isCanceled =
                    GlobalConfigHolder.config.value.status.status != StatusConfig.Status.DISABLED && !ControllerHudModel.result.showBlockOutline
            }

            @SubscribeEvent
            fun clientTick(event: ClientTickEvent.Post) {
                TickEvents.clientTick()
            }

            @SubscribeEvent
            fun joinWorld(event: ClientPlayerNetworkEvent.LoggingIn) {
                ConnectionEvents.onJoinedWorld()
            }

            @SubscribeEvent
            fun blockBroken(event: BreakBlockEvent) {
                BlockBreakEvents.afterBlockBreak()
            }
        })
    }
}
