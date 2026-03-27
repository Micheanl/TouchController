/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright (C) 2026 fifth_light
 */

package top.fifthlight.touchcontroller.api.v1.widget;

import org.jetbrains.annotations.ApiStatus;

@ApiStatus.NonExtendable
public interface WidgetTextureBuilder {
    WidgetTextureBuilder id(String id);

    WidgetTextureBuilder classic(String namespace, String path);

    WidgetTextureBuilder classicExtension(String namespace, String path);

    WidgetTextureBuilder newStyle(String namespace, String path);

    WidgetTextureBuilder newRegression(String namespace, String path);
}
