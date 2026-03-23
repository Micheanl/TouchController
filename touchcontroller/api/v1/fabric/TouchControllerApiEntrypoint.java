/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright (C) 2026 fifth_light
 */

package top.fifthlight.touchcontroller.api.v1.fabric;

import top.fifthlight.touchcontroller.api.v1.TouchControllerApi;

public interface TouchControllerApiEntrypoint {
    void preTouchControllerInitialize(TouchControllerApi api);
}
