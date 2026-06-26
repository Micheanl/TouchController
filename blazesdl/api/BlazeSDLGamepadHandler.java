/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright (C) 2026 fifth_light
 */

package top.fifthlight.blazesdl.api;

import org.jspecify.annotations.NonNull;

public interface BlazeSDLGamepadHandler {
    int getPriority();
    boolean onGamepadAdded(int gamepadId);
    boolean onGamepadRemoved(int gamepadId);
    boolean onGamepadButton(int gamepadId, int button, boolean pressed);
    boolean onGamepadAxis(int gamepadId, int axis, float value);
}
