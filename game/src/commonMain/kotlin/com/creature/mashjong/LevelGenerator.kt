package com.creature.mashjong

import com.creature.mashjong.layout.LayoutStrategy
import korlibs.image.bitmap.BmpSlice

object LevelGenerator {

    fun generateLayout(
        deck: List<Pair<TileInfo, BmpSlice>>,
        strategy: LayoutStrategy
    ): List<TilePosition> {
        val slots = strategy.getLayoutSlots()

        // Ensure we don't overflow or underflow
        val tileCount = minOf(deck.size, slots.size)
        val result = mutableListOf<TilePosition>()

        for (i in 0 until tileCount) {
            val slot = slots[i]
            val tileInfo = deck[i].first

            result.add(TilePosition(slot.layer, slot.x, slot.y, tileInfo.id))
        }

        return result
    }
}
