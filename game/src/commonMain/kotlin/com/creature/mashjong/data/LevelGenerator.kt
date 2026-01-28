package com.creature.mashjong.data

import com.creature.mashjong.domain.model.TileInfo
import com.creature.mashjong.domain.model.TilePosition
import com.creature.mashjong.layout.LayoutStrategy

object LevelGenerator {

    fun generateLayout(
        deck: List<TileInfo>,
        strategy: LayoutStrategy
    ): List<TilePosition> {
        val slots = strategy.getLayoutSlots()

        // Ensure we don't overflow or underflow
        val tileCount = minOf(deck.size, slots.size)
        val result = mutableListOf<TilePosition>()

        for (i in 0 until tileCount) {
            val slot = slots[i]
            val tileInfo = deck[i]

            result.add(TilePosition(slot.layer, slot.x, slot.y, tileInfo.id))
        }

        return result
    }
}
