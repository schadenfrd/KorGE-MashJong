package com.creature.mashjong.domain.model

enum class TileSuit { DOTS, BAMBOO, CHARACTERS, HONORS, FLOWERS, SEASONS }

data class TileInfo(
    val id: Int,        // Unique ID (0-35 for the base set, +8 for flowers/seasons)
    val suit: TileSuit,
    val value: Int,     // 1-9 for suits, 0-6 for honors, 1-4 for flowers/seasons
    val name: String = "$value of ${suit.name.lowercase()}",
    val quantity: Int = 4 // Number of copies in a standard deck
)