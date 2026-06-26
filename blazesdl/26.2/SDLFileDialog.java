/*
 * SPDX-License-Identifier: LGPL-3.0-or-later
 * Copyright (C) 2026 fifth_light
 */

package top.fifthlight.blazesdl;

import org.lwjgl.sdl.SDLDialog;
import org.lwjgl.sdl.SDL_DialogFileCallback;
import org.lwjgl.sdl.SDL_DialogFileFilter;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.function.Consumer;

public class SDLFileDialog {
    private static final Logger LOGGER = LoggerFactory.getLogger(SDLFileDialog.class);

    public static void openFile(long window, String defaultLocation, boolean allowMany, String[] filters, Consumer<String[]> callback) {
        var cb = SDL_DialogFileCallback.create((userdata, filelist, filter) -> {
            if (filelist == 0L) {
                callback.accept(new String[0]);
                return;
            }
            var files = new ArrayList<String>();
            var ptr = MemoryUtil.memGetAddress(filelist);
            while (ptr != 0L) {
                var file = MemoryUtil.memUTF8(ptr);
                if (file != null) {
                    files.add(file);
                }
                filelist += org.lwjgl.system.Pointer.POINTER_SIZE;
                ptr = MemoryUtil.memGetAddress(filelist);
            }
            callback.accept(files.toArray(new String[0]));
        });

        try (var stack = MemoryStack.stackPush()) {
            SDL_DialogFileFilter.Buffer filterBuffer = null;
            if (filters != null && filters.length > 0) {
                filterBuffer = SDL_DialogFileFilter.calloc(filters.length, stack);
                for (int i = 0; i < filters.length; i++) {
                    filterBuffer.get(i)
                            .name(stack.UTF8(filters[i]))
                            .pattern(stack.UTF8(filters[i]));
                }
            }

            SDLDialog.SDL_ShowOpenFileDialog(cb, 0L, window, filterBuffer, defaultLocation, allowMany);
        }
    }

    public static void saveFile(long window, String defaultLocation, String[] filters, Consumer<String> callback) {
        var cb = SDL_DialogFileCallback.create((userdata, filelist, filter) -> {
            if (filelist == 0L) {
                callback.accept(null);
                return;
            }
            var file = MemoryUtil.memUTF8(MemoryUtil.memGetAddress(filelist));
            callback.accept(file);
        });

        try (var stack = MemoryStack.stackPush()) {
            SDL_DialogFileFilter.Buffer filterBuffer = null;
            if (filters != null && filters.length > 0) {
                filterBuffer = SDL_DialogFileFilter.calloc(filters.length, stack);
                for (int i = 0; i < filters.length; i++) {
                    filterBuffer.get(i)
                            .name(stack.UTF8(filters[i]))
                            .pattern(stack.UTF8(filters[i]));
                }
            }

            SDLDialog.SDL_ShowSaveFileDialog(cb, 0L, window, filterBuffer, defaultLocation);
        }
    }

    public static void openFolder(long window, String defaultLocation, Consumer<String> callback) {
        var cb = SDL_DialogFileCallback.create((userdata, filelist, filter) -> {
            if (filelist == 0L) {
                callback.accept(null);
                return;
            }
            var file = MemoryUtil.memUTF8(MemoryUtil.memGetAddress(filelist));
            callback.accept(file);
        });

        SDLDialog.SDL_ShowOpenFolderDialog(cb, 0L, window, defaultLocation, false);
    }

    private SDLFileDialog() {
    }
}
