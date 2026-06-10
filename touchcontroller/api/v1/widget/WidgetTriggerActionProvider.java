/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright (C) 2026 fifth_light
 */

package top.fifthlight.touchcontroller.api.v1.widget;

import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.Nullable;
import top.fifthlight.touchcontroller.api.v1.action.GameActionInstance;
import top.fifthlight.touchcontroller.api.v1.action.PlayerActionInstance;

@ApiStatus.NonExtendable
public interface WidgetTriggerActionProvider {
    WidgetTriggerAction gameAction(GameActionInstance gameAction);

    WidgetTriggerAction playerAction(PlayerActionInstance playerAction);

    @Nullable
    WidgetTriggerAction click(String keyMapping);

    @Nullable
    WidgetTriggerAction startLock(String keyMapping);

    @Nullable
    WidgetTriggerAction stopLock(String keyMapping);

    @Nullable
    WidgetTriggerAction invertLock(String keyMapping);
}
