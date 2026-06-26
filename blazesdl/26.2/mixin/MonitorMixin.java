/*
 * SPDX-License-Identifier: LGPL-3.0-or-later
 * Copyright (C) 2026 fifth_light
 */

package top.fifthlight.blazesdl.mixin;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.platform.Monitor;
import com.mojang.blaze3d.platform.VideoMode;
import org.jspecify.annotations.NonNull;
import org.lwjgl.sdl.SDLStdinc;
import org.lwjgl.sdl.SDLVideo;
import org.lwjgl.sdl.SDL_Rect;
import org.lwjgl.system.MemoryStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import top.fifthlight.blazesdl.SDLError;
import top.fifthlight.blazesdl.SDLVideoMode;

@Mixin(Monitor.class)
public abstract class MonitorMixin {
    @Overwrite
    public static @NonNull Monitor tryCreate(final long displayId) {
        var monitorName = SDLVideo.SDL_GetDisplayName((int) displayId);
        if (monitorName == null) {
            throw SDLError.handleError("SDL_GetDisplayName");
        }

        ImmutableList.Builder<VideoMode> videoModes = ImmutableList.builder();
        var modes = SDLVideo.SDL_GetFullscreenDisplayModes((int) displayId);
        if (modes == null) {
            throw SDLError.handleError("SDL_GetFullscreenDisplayModes");
        }
        try {
            for (var i = modes.limit() - 1; i >= 0; i--) {
                var mode = SDLVideoMode.fromSDLDisplayMode(modes.get(i));
                if (mode.getRedBits() >= 8 && mode.getGreenBits() >= 8 && mode.getBlueBits() >= 8) {
                    videoModes.add(mode);
                }
            }
        } finally {
            SDLStdinc.SDL_free(modes);
        }

        int x;
        int y;
        VideoMode currentMode;
        try (var stack = MemoryStack.stackPush()) {
            var rect = SDL_Rect.malloc(stack);
            if (!SDLVideo.SDL_GetDisplayBounds((int) displayId, rect)) {
                throw SDLError.handleError("SDL_GetDisplayBounds");
            }
            x = rect.x();
            y = rect.y();

            var mode = SDLVideo.SDL_GetCurrentDisplayMode((int) displayId);
            if (mode == null) {
                throw SDLError.handleError("SDL_GetCurrentDisplayMode");
            }
            currentMode = SDLVideoMode.fromSDLDisplayMode(mode.address());
        }
        return new Monitor(monitorName, displayId, videoModes.build(), currentMode, x, y);
    }
}
