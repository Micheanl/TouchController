/*
 * SPDX-License-Identifier: LGPL-3.0-or-later
 * Copyright (C) 2026 fifth_light
 */

package top.fifthlight.blazesdl;

import org.lwjgl.sdl.SDLAudio;
import org.lwjgl.sdl.SDLInit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SDLAudioManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(SDLAudioManager.class);

    public static void init() {
        if ((SDLInit.SDL_WasInit(SDLInit.SDL_INIT_AUDIO) & SDLInit.SDL_INIT_AUDIO) == 0) {
            if (!SDLInit.SDL_Init(SDLInit.SDL_INIT_AUDIO)) {
                LOGGER.warn("Failed to init SDL audio: {}", org.lwjgl.sdl.SDLError.SDL_GetError());
                return;
            }
        }
        var audioDevices = SDLAudio.SDL_GetAudioPlaybackDevices();
        if (audioDevices != null) {
            try {
                LOGGER.info("SDL audio initialized with {} playback device(s)", audioDevices.limit());
            } finally {
                org.lwjgl.sdl.SDLStdinc.SDL_free(audioDevices);
            }
        }
    }

    public static void quit() {
        SDLAudio.SDL_CloseAudioDevice(0);
    }

    private SDLAudioManager() {
    }
}
