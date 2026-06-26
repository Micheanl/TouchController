/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright (C) 2026 fifth_light
 */

package top.fifthlight.blazesdl.api;

import org.jspecify.annotations.NonNull;

public interface BlazeSDLInitializationHandler {
    int getPriority();
    void onInit();
    void onShutdown();
}
