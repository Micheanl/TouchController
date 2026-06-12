/*
 * SPDX-License-Identifier: LGPL-3.0-or-later
 * Copyright (C) 2026 fifth_light
 */

package top.fifthlight.touchcontroller.common.ui.entitypicker

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import top.fifthlight.combine.core.layout.Alignment
import top.fifthlight.combine.core.layout.Arrangement
import top.fifthlight.combine.core.modifier.Modifier
import top.fifthlight.combine.core.modifier.placement.fillMaxWidth
import top.fifthlight.combine.core.modifier.placement.minHeight
import top.fifthlight.combine.core.modifier.placement.padding
import top.fifthlight.combine.core.modifier.scroll.verticalScroll
import top.fifthlight.combine.core.widget.layout.Column
import top.fifthlight.combine.core.widget.layout.Row
import top.fifthlight.combine.item.widget.Item
import top.fifthlight.combine.theme.blackstone.widget.ListButton
import top.fifthlight.combine.widget.Text
import top.fifthlight.touchcontroller.common.gal.entity.EntityItemProvider
import top.fifthlight.touchcontroller.common.gal.entity.EntityType
import top.fifthlight.touchcontroller.common.gal.entity.EntityTypeProvider
import top.fifthlight.touchcontroller.common.gal.gamestate.GameState

@Composable
fun EntityPicker(
    modifier: Modifier = Modifier,
    onEntityChosen: (EntityType) -> Unit,
) {
    val inGame = GameState.inGame
    val totalEntityTypes = remember(inGame) {
        EntityTypeProvider.allTypes.map { type ->
            val icon = if (inGame) {
                EntityItemProvider.getEntityIconItem(type)
            } else {
                null
            }
            Pair(icon, type)
        }
    }

    Column(
        modifier = Modifier.padding(8)
            .verticalScroll()
            .then(modifier),
    ) {
        for ((icon, entityType) in totalEntityTypes) {
            ListButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = { onEntityChosen(entityType) },
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .minHeight(16),
                    horizontalArrangement = Arrangement.spacedBy(4),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    if (icon != null) {
                        Item(item = icon)
                    }
                    Text(
                        modifier = Modifier
                            .weight(1f),
                        text = entityType.name,
                    )
                }
            }
        }
    }
}
