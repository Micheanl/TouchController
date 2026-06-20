/*
 * SPDX-License-Identifier: LGPL-3.0-or-later
 * Copyright (C) 2026 fifth_light
 */

package top.fifthlight.blazesdl;

import com.mojang.blaze3d.platform.Monitor;
import com.mojang.blaze3d.platform.MonitorManager;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import org.jspecify.annotations.Nullable;
import org.lwjgl.sdl.SDLEvents;
import org.lwjgl.sdl.SDLStdinc;
import org.lwjgl.sdl.SDLVideo;

public class SDLMonitorManager extends MonitorManager {
    public SDLMonitorManager() {
        super();
        var displays = SDLVideo.SDL_GetDisplays();
        if (displays == null) {
            throw SDLError.handleError("SDL_GetDisplays");
        }
        try {
            for (var i = 0; i < displays.limit(); i++) {
                var displayId = displays.get(i);
                var monitor = Monitor.tryCreate(displayId);
                if (monitor != null) {
                    this.monitors.put(displayId, monitor);
                }
            }
        } finally {
            SDLStdinc.SDL_free(displays);
        }
    }

    @Override
    public void onMonitorChange(final long displayId, final int event) {
        RenderSystem.assertOnRenderThread();
        switch (event) {
            case SDLEvents.SDL_EVENT_DISPLAY_ADDED -> {
                var monitor = Monitor.tryCreate(displayId);
                if (monitor != null) {
                    this.monitors.put(displayId, monitor);
                    MonitorManager.LOGGER.debug("Monitor {} connected. Current monitors: {}", monitor, this.monitors);
                }
            }
            case SDLEvents.SDL_EVENT_DISPLAY_REMOVED -> {
                Monitor monitor = null;
                if (monitors.containsKey(displayId)) {
                    monitor = this.monitors.remove(displayId);
                }
                MonitorManager.LOGGER.debug("Monitor {} disconnected. Current monitors: {}", monitor != null ? monitor : displayId, this.monitors);
            }
        }
    }

    @Override
    public @Nullable Monitor findBestMonitor(Window window) {
        var windowMonitor = SDLVideo.SDL_GetDisplayForWindow(window.handle());
        if (windowMonitor != 0) {
            return this.getMonitor(windowMonitor);
        } else {
            var winMinX = window.getX();
            var winMaxX = winMinX + window.getScreenWidth();
            var winMinY = window.getY();
            var winMaxY = winMinY + window.getScreenHeight();
            var maxArea = -1;

            Monitor result = null;
            var primaryDisplay = SDLVideo.SDL_GetPrimaryDisplay();
            LOGGER.debug("Selecting monitor - primary: {}, current monitors: {}", primaryDisplay, this.monitors);

            for (var monitor : this.monitors.values()) {
                var monMinX = monitor.x();
                var monMaxX = monMinX + monitor.currentMode().getWidth();
                var monMinY = monitor.y();
                var monMaxY = monMinY + monitor.currentMode().getHeight();

                var minX = clamp(winMinX, monMinX, monMaxX);
                var maxX = clamp(winMaxX, monMinX, monMaxX);
                var minY = clamp(winMinY, monMinY, monMaxY);
                var maxY = clamp(winMaxY, monMinY, monMaxY);

                var sx = Math.max(0, maxX - minX);
                var sy = Math.max(0, maxY - minY);
                var area = sx * sy;

                if (area > maxArea) {
                    result = monitor;
                    maxArea = area;
                } else if (area == maxArea && primaryDisplay == monitor.monitor()) {
                    LOGGER.debug("Primary monitor {} is preferred to monitor {}", monitor, result);
                    result = monitor;
                }
            }

            LOGGER.debug("Selected monitor: {}", result);
            return result;
        }
    }

    @Override
    public void close() {
        // no-op
    }
}
