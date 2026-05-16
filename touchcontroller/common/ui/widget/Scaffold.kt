/*
 * SPDX-License-Identifier: LGPL-3.0-or-later
 * Copyright (C) 2026 fifth_light
 */

package top.fifthlight.touchcontroller.common.ui.widget

import androidx.compose.runtime.Composable
import top.fifthlight.combine.core.modifier.Modifier
import top.fifthlight.combine.core.modifier.placement.fillMaxHeight
import top.fifthlight.combine.core.modifier.placement.fillMaxSize
import top.fifthlight.combine.core.widget.layout.Column
import top.fifthlight.combine.core.widget.layout.Row

@Composable
fun Scaffold(
    modifier: Modifier = Modifier,
    topBar: @Composable () -> Unit = {},
    leftSideBar: @Composable () -> Unit = {},
    rightSideBar: @Composable () -> Unit = {},
    content: @Composable (Modifier) -> Unit,
) {
    Column(Modifier.fillMaxSize().then(modifier)) {
        topBar()
        Row(Modifier.weight(1f)) {
            leftSideBar()
            content(Modifier.weight(1f).fillMaxHeight())
            rightSideBar()
        }
    }
}
