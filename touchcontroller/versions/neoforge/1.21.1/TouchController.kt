/*
 * SPDX-License-Identifier: LGPL-3.0-or-later
 * Copyright (C) 2026 fifth_light
 */

package top.fifthlight.touchcontroller.neoforge.v1_21_1

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.screens.Screen
import net.minecraft.resources.ResourceLocation
import net.neoforged.bus.api.IEventBus
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.ModContainer
import net.neoforged.fml.common.Mod
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent
import net.neoforged.neoforge.client.event.ClientTickEvent
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent
import net.neoforged.neoforge.client.event.RenderHighlightEvent
import net.neoforged.neoforge.client.gui.IConfigScreenFactory
import net.neoforged.neoforge.client.gui.VanillaGuiLayers
import net.neoforged.neoforge.common.NeoForge
import net.neoforged.neoforge.event.level.BlockEvent
import org.slf4j.LoggerFactory
import top.fifthlight.combine.backend.minecraft.render.v1_21_1.CanvasImpl
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
import top.fifthlight.touchcontroller.gal.gameconfig.v1_21_1.GameConfigEditorImpl

@Mod("touchcontroller_1_21_1")
class TouchController(modEventBus: IEventBus, private val container: ModContainer) {
    private val logger = LoggerFactory.getLogger(TouchController::class.java)

    init {
        modEventBus.addListener(::onClientSetup)
        modEventBus.addListener(::onRegisterHudHandler)
    }

    private fun onClientSetup(event: FMLClientSetupEvent) {
        logger.info("Loading TouchController…")

        initialize()

        TouchControllerLoadStatus.isLoaded = true
    }

    private fun onRegisterHudHandler(event: RegisterGuiLayersEvent) {
        event.registerAbove(
            VanillaGuiLayers.BOSS_OVERLAY,
            ResourceLocation.fromNamespaceAndPath(BuildInfo.MOD_ID, "hud")
        ) { guiGraphics, _ ->
            val client = Minecraft.getInstance()
            if (!client.options.hideGui) {
                val canvas = CanvasImpl(guiGraphics)
                RenderEvents.onHudRender(canvas)
            }
        }
    }

    private fun initialize() {
        val client = Minecraft.getInstance()

        container.registerExtensionPoint(IConfigScreenFactory::class.java, IConfigScreenFactory { _, parent ->
            getConfigScreen(parent) as Screen
        })

        client.execute {
            GlobalConfigHolder.load()
            WindowEvents.onWindowCreated()
            GameConfigEditorImpl.executePendingCallback()
        }

        NeoForge.EVENT_BUS.register(object {
            @SubscribeEvent
            fun blockOutlineEvent(event: RenderHighlightEvent.Block) {
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
            fun blockBroken(event: BlockEvent.BreakEvent) {
                BlockBreakEvents.afterBlockBreak()
            }
        })
    }
}
