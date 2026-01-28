package com.creature.mashjong

import com.creature.mashjong.layout.LayoutRegistry
import com.creature.mashjong.layout.LayoutStrategy

data class GameSettings(
    val layoutStrategy: LayoutStrategy = LayoutRegistry.availableLayouts.values.random(),
    val atlasPath: String = "atlas_mish_mashjong.png",
    val maxUndos: Int = 3,
    val maxHints: Int = 3
)
