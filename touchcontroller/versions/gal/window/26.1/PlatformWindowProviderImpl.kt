/*
 * SPDX-License-Identifier: LGPL-3.0-or-later
 * Copyright (C) 2026 fifth_light
 */

package top.fifthlight.touchcontroller.gal.window.v26_1

import net.minecraft.client.Minecraft
import org.lwjgl.glfw.GLFW
import org.lwjgl.glfw.GLFWNativeWayland
import org.lwjgl.glfw.GLFWNativeWin32
import top.fifthlight.mergetools.api.ActualConstructor
import top.fifthlight.mergetools.api.ActualImpl
import top.fifthlight.touchcontroller.common.gal.window.GlfwPlatform
import top.fifthlight.touchcontroller.common.gal.window.NativeWindow
import top.fifthlight.touchcontroller.common.gal.window.PlatformWindowProvider

@ActualImpl(PlatformWindowProvider::class)
object PlatformWindowProviderImpl : PlatformWindowProvider {
    @JvmStatic
    @ActualConstructor("of")
    fun of(): PlatformWindowProvider = PlatformWindowProviderImpl

    private val inner by lazy {
        Minecraft.getInstance().window
    }

    override val windowWidth: Int
        get() = inner.screenWidth
    override val windowHeight: Int
        get() = inner.screenHeight

    override val platform: GlfwPlatform<*> by lazy {
        when (GLFW.glfwGetPlatform()) {
            GLFW.GLFW_PLATFORM_WIN32 -> GlfwPlatform.Win32 {
                NativeWindow.Win32(GLFWNativeWin32.glfwGetWin32Window(inner.handle()))
            }

            GLFW.GLFW_PLATFORM_WAYLAND -> GlfwPlatform.Wayland {
                NativeWindow.Wayland(
                    displayPointer = GLFWNativeWayland.glfwGetWaylandDisplay(),
                    surfacePointer = GLFWNativeWayland.glfwGetWaylandWindow(inner.handle()),
                )
            }

            GLFW.GLFW_PLATFORM_COCOA -> GlfwPlatform.Cocoa
            GLFW.GLFW_PLATFORM_X11 -> GlfwPlatform.X11
            else -> GlfwPlatform.Unknown
        }
    }
}
