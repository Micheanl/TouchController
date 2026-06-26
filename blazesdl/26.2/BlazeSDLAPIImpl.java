/*
 * SPDX-License-Identifier: LGPL-3.0-or-later
 * Copyright (C) 2026 fifth_light
 */

package top.fifthlight.blazesdl;

import org.jspecify.annotations.NonNull;
import org.lwjgl.sdl.SDL_Event;
import org.lwjgl.sdl.SDLEvents;
import top.fifthlight.blazesdl.api.BlazeSDLAPI;
import top.fifthlight.blazesdl.api.BlazeSDLEventHandler;
import top.fifthlight.blazesdl.api.BlazeSDLGamepadHandler;
import top.fifthlight.blazesdl.api.BlazeSDLInitializationHandler;

import java.util.ArrayList;

public class BlazeSDLAPIImpl implements BlazeSDLAPI {
    private static final ArrayList<BlazeSDLEventHandler> eventHandlers = new ArrayList<>();

    // Dynamic event buffer
    private SDL_Event.Buffer eventBuffer;
    private static final int MIN_EVENT_BUFFER_SIZE = 64;
    private static final int MAX_EVENT_BUFFER_SIZE = 4096;

    public BlazeSDLAPIImpl() {
        this.eventBuffer = SDL_Event.calloc(MIN_EVENT_BUFFER_SIZE);
    }

    @Override
    public synchronized void registerEventHandler(@NonNull BlazeSDLEventHandler handler) {
        var priority = handler.getPriority();
        var i = 0;
        for (; i < eventHandlers.size(); i++) {
            if (eventHandlers.get(i).getPriority() < priority) {
                break;
            }
        }
        eventHandlers.add(i, handler);
    }

    @Override
    public synchronized void unregisterEventHandler(@NonNull BlazeSDLEventHandler handler) {
        eventHandlers.remove(handler);
    }

    private final ArrayList<BlazeSDLGamepadHandler> gamepadHandlers = new ArrayList<>();

    private final ArrayList<BlazeSDLInitializationHandler> initHandlers = new ArrayList<>();
    private boolean initCalled;

    @Override
    public synchronized void registerInitializationHandler(@NonNull BlazeSDLInitializationHandler handler) {
        var priority = handler.getPriority();
        var i = 0;
        for (; i < initHandlers.size(); i++) {
            if (initHandlers.get(i).getPriority() < priority) {
                break;
            }
        }
        initHandlers.add(i, handler);
        if (initCalled) {
            handler.onInit();
        }
    }

    @Override
    public synchronized void unregisterInitializationHandler(@NonNull BlazeSDLInitializationHandler handler) {
        if (initCalled) {
            handler.onShutdown();
        }
        initHandlers.remove(handler);
    }

    public void fireInit() {
        initCalled = true;
        for (var handler : initHandlers) {
            handler.onInit();
        }
    }

    public void fireShutdown() {
        initCalled = false;
        for (var handler : initHandlers) {
            handler.onShutdown();
        }
    }

    @Override
    public synchronized void registerGamepadHandler(@NonNull BlazeSDLGamepadHandler handler) {
        var priority = handler.getPriority();
        var i = 0;
        for (; i < gamepadHandlers.size(); i++) {
            if (gamepadHandlers.get(i).getPriority() < priority) {
                break;
            }
        }
        gamepadHandlers.add(i, handler);
    }

    @Override
    public synchronized void unregisterGamepadHandler(@NonNull BlazeSDLGamepadHandler handler) {
        gamepadHandlers.remove(handler);
    }

    public boolean handleGamepadAdded(int gamepadId) {
        for (var handler : gamepadHandlers) {
            if (handler.onGamepadAdded(gamepadId)) {
                return true;
            }
        }
        return false;
    }

    public boolean handleGamepadRemoved(int gamepadId) {
        for (var handler : gamepadHandlers) {
            if (handler.onGamepadRemoved(gamepadId)) {
                return true;
            }
        }
        return false;
    }

    public boolean handleGamepadButton(int gamepadId, int button, boolean pressed) {
        for (var handler : gamepadHandlers) {
            if (handler.onGamepadButton(gamepadId, button, pressed)) {
                return true;
            }
        }
        return false;
    }

    public boolean handleGamepadAxis(int gamepadId, int axis, float value) {
        for (var handler : gamepadHandlers) {
            if (handler.onGamepadAxis(gamepadId, axis, value)) {
                return true;
            }
        }
        return false;
    }

    public boolean handleEvent(SDL_Event event) {
        for (var handler : eventHandlers) {
            if (handler.handleEvent(event)) {
                return true;
            }
        }
        return false;
    }

    public boolean handleGamepadEvent(SDL_Event event) {
        if (gamepadHandlers.isEmpty()) {
            return false;
        }
        var eventType = event.type();
        switch (eventType) {
            case SDLEvents.SDL_EVENT_GAMEPAD_ADDED -> {
                return handleGamepadAdded(event.gdevice().which());
            }
            case SDLEvents.SDL_EVENT_GAMEPAD_REMOVED -> {
                return handleGamepadRemoved(event.gdevice().which());
            }
            case SDLEvents.SDL_EVENT_GAMEPAD_BUTTON_DOWN, SDLEvents.SDL_EVENT_GAMEPAD_BUTTON_UP -> {
                var button = event.gbutton();
                return handleGamepadButton(button.which(), button.button(), eventType == SDLEvents.SDL_EVENT_GAMEPAD_BUTTON_DOWN);
            }
            case SDLEvents.SDL_EVENT_GAMEPAD_AXIS_MOTION -> {
                var axis = event.gaxis();
                return handleGamepadAxis(axis.which(), axis.axis(), axis.value());
            }
            default -> {
                return false;
            }
        }
    }

    public SDL_Event.Buffer getEventBuffer(int requiredCapacity) {
        var capacity = Math.max(MIN_EVENT_BUFFER_SIZE, Math.min(MAX_EVENT_BUFFER_SIZE, requiredCapacity));
        if (eventBuffer.capacity() < capacity) {
            eventBuffer.close();
            eventBuffer = SDL_Event.calloc(capacity);
        }
        eventBuffer.clear();
        return eventBuffer;
    }
}
