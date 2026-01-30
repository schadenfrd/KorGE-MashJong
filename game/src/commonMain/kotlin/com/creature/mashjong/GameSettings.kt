package com.creature.mashjong

import com.creature.mashjong.domain.model.DeckStyle
import com.creature.mashjong.layout.LayoutRegistry
import com.creature.mashjong.layout.LayoutStrategy

data class GameSettings(
    val layoutStrategy: LayoutStrategy = LayoutRegistry.availableLayouts.values.random(),
    val deckStyle: DeckStyle = DeckStyle.MishMash,
    val maxUndos: Int = 3,
    val maxHints: Int = 3,
    val maxShuffles: Int = 1
)
