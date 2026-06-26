/*
 * SPDX-License-Identifier: LGPL-3.0-or-later
 * Copyright (C) 2026 fifth_light
 */

package top.fifthlight.blazesdl;

import com.mojang.blaze3d.platform.Window;
import org.jspecify.annotations.Nullable;
import org.lwjgl.sdl.SDLKeyboard;
import org.lwjgl.sdl.SDLVideo;
import org.lwjgl.sdl.SDL_Rect;
import org.lwjgl.system.MemoryStack;

public class SDLUtil {
    public static final boolean IS_WAYLAND = "wayland".equalsIgnoreCase(SDLVideo.SDL_GetCurrentVideoDriver());
    public static final boolean IS_WINDOWS = "windows".equalsIgnoreCase(SDLVideo.SDL_GetCurrentVideoDriver());

    public static long keyboardStateAddress;
    public static boolean keyboardStateValid;

    public static void setKeyboardState(long address) {
        keyboardStateAddress = address;
        keyboardStateValid = address != 0L;
    }

    public static boolean isMouseGrabbed = false;
    public static double virtualMouseX;
    public static double virtualMouseY;
    public static double realMouseX;
    public static double realMouseY;

    public static void updateTextInputArea(Window window, int x, int y, int w, int h, int cursor) {
        if (!(window instanceof SDLWindow sdlWindow)) {
            return;
        }
        var widthScale = (float) window.getWidth() / window.getScreenWidth();
        var heightScale = (float) window.getHeight() / window.getScreenHeight();
        try (var stack = MemoryStack.stackPush()) {
            var rect = SDL_Rect.calloc(1, stack);
            rect.x((int) (x / widthScale));
            rect.y((int) (y / heightScale));
            rect.w((int) (w / widthScale));
            rect.h((int) (h / heightScale));
            SDLKeyboard.SDL_SetTextInputArea(sdlWindow.handle(), rect, (int) (cursor / widthScale));
        }
    }

    public static void updateTextInputAreaScaled(Window window, int x, int y, int w, int h, int cursor) {
        var scale = window.getGuiScale();
        updateTextInputArea(window, x * scale, y * scale, w * scale, h * scale, cursor * scale);
    }

    @SuppressWarnings("unchecked")
    public static <T extends Throwable> void throwAny(Throwable throwable) throws T {
        throw (T) throwable;
    }
}
