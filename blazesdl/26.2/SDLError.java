/*
 * SPDX-License-Identifier: LGPL-3.0-or-later
 * Copyright (C) 2026 fifth_light
 */

package top.fifthlight.blazesdl;

import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.BiConsumer;

public class SDLError {
    private static final Logger LOGGER = LoggerFactory.getLogger(SDLError.class);
    private static @Nullable BiConsumer<String, String> errorCallback;

    public static void setErrorCallback(@Nullable BiConsumer<String, String> callback) {
        errorCallback = callback;
    }

    public static RuntimeException handleError(String func) {
        var error = org.lwjgl.sdl.SDLError.SDL_GetError();
        var cb = errorCallback;
        if (cb != null) {
            if (error != null) {
                cb.accept(func, error);
            } else {
                cb.accept(func, "");
            }
        }
        if (error != null) {
            LOGGER.error("SDL error in {}: {}", func, error);
            return new RuntimeException("Function " + func + " failed with cause: " + error);
        } else {
            LOGGER.error("SDL error in {}: no cause", func);
            return new RuntimeException("Function " + func + " failed with no cause");
        }
    }
}
