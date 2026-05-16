/*
 * SPDX-License-Identifier: LGPL-3.0-or-later
 * Copyright (C) 2026 fifth_light
 */

package top.fifthlight.touchcontroller.common.ui.config.tab.component

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.LocalNavigator
import top.fifthlight.combine.core.layout.Arrangement
import top.fifthlight.combine.core.modifier.Modifier
import top.fifthlight.combine.core.modifier.drawing.border
import top.fifthlight.combine.core.modifier.placement.fillMaxWidth
import top.fifthlight.combine.core.modifier.placement.padding
import top.fifthlight.combine.core.modifier.placement.width
import top.fifthlight.combine.core.modifier.scroll.verticalScroll
import top.fifthlight.combine.core.widget.layout.Column
import top.fifthlight.combine.widget.ui.Text
import top.fifthlight.touchcontroller.common.ui.config.tab.Tab
import top.fifthlight.touchcontroller.common.ui.config.tab.TabGroup
import top.fifthlight.touchcontroller.common.ui.config.tab.TabOptions
import top.fifthlight.touchcontroller.common.ui.theme.LocalTouchControllerTheme
import top.fifthlight.touchcontroller.common.ui.widget.TabButton

@Composable
fun SideTabBar(
    modifier: Modifier = Modifier,
    onTabSelected: (Tab, TabOptions) -> Unit,
    tabGroups: List<Pair<TabGroup?, List<Tab>>>,
) {
    val navigator = LocalNavigator.current
    Column(
        modifier = Modifier
            .width(130)
            .padding(top = 8, left = 2, right = 2, bottom = 2)
            .verticalScroll()
            .border(LocalTouchControllerTheme.current.borderBackgroundDark)
            .then(modifier),
        verticalArrangement = Arrangement.spacedBy(12),
    ) {
        for ((group, tabs) in tabGroups) {
            if (tabs.isEmpty()) {
                continue
            }
            Column(verticalArrangement = Arrangement.spacedBy(4)) {
                group?.let { group ->
                    Text(group.title)
                }
                Column(verticalArrangement = Arrangement.spacedBy(4)) {
                    for (tab in tabs) {
                        val options = tab.options
                        TabButton(
                            modifier = Modifier.fillMaxWidth(),
                            checked = navigator?.lastItem == tab,
                            onClick = {
                                onTabSelected(tab, options)
                            },
                        ) {
                            Text(options.title)
                        }
                    }
                }
            }
        }
    }
}
