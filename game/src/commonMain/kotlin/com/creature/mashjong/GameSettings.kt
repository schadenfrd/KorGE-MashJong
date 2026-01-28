package com.creature.mashjong

import com.creature.mashjong.layout.LayoutStrategy
import com.creature.mashjong.layout.TurtleLayoutStrategy

data class GameSettings(
    val layoutStrategy: LayoutStrategy = TurtleLayoutStrategy(),
    val atlasPath: String = "atlas_mish_mashjong.png",
    val maxUndos: Int = 3,
    val maxHints: Int = 3
)
