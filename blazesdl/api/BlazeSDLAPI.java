/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright (C) 2026 fifth_light
 */

package top.fifthlight.blazesdl.api;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public interface BlazeSDLAPI {
    @Nullable
    static BlazeSDLAPI getInstance() {
        return BlazeSDLApiHolder.INSTANCE;
    }

    void registerEventHandler(@NonNull BlazeSDLEventHandler handler);

    void unregisterEventHandler(@NonNull BlazeSDLEventHandler handler);

    void registerGamepadHandler(@NonNull BlazeSDLGamepadHandler handler);

    void unregisterGamepadHandler(@NonNull BlazeSDLGamepadHandler handler);

    void registerInitializationHandler(@NonNull BlazeSDLInitializationHandler handler);

    void unregisterInitializationHandler(@NonNull BlazeSDLInitializationHandler handler);
}
