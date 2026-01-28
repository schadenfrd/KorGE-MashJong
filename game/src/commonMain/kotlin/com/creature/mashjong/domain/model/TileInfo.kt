package com.creature.mashjong.domain.model

data class TileInfo(
    val id: Int,             // Unique instance ID
    val definition: TileDefinition,
    val quantity: Int = 4
) {
    // Convenience delegate
    val name: String = definition.displayName
}