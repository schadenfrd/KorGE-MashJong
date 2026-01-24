package com.creature.mashjong

import korlibs.image.bitmap.BmpSlice

object LevelGenerator {
    
    fun generateTurtleLayout(deck: List<Pair<TileInfo, BmpSlice>>): List<TilePosition> {
        val positions = getTurtlePositions()
        
        // Ensure we don't overflow or underflow
        val tileCount = minOf(deck.size, positions.size)
        
        val result = mutableListOf<TilePosition>()
        
        for (i in 0 until tileCount) {
            val (layer, x, y) = positions[i]
            
            val tileInfo = deck[i].first
            result.add(TilePosition(layer, x, y, tileInfo.id))
        }
        
        return result
    }

    private data class GridPoint(val layer: Int, val x: Int, val y: Int)

    private fun getTurtlePositions(): List<GridPoint> {
        val list = mutableListOf<GridPoint>()

        // Layer 0 (Base): 87 tiles
        // 7 rows of 12 (x=2..24, y=0..12) -> Rotated: x=0..12, y=2..24
        for (y in 0..12 step 2) {
            for (x in 2..24 step 2) {
                list.add(GridPoint(0, y, x)) // Swapped x/y for portrait
            }
        }
        // Ears (3 tiles) - Rotated
        list.add(GridPoint(0, 6, 0))  // Top (was Left)
        list.add(GridPoint(0, 6, 26)) // Bottom (was Right 1)
        list.add(GridPoint(0, 6, 28)) // Bottom (was Right 2)

        // Layer 1 (6x6): 36 tiles
        // Center is now (6, 14)
        // x in 9..19, y in 1..11 -> Rotated: x in 1..11, y in 9..19
        for (y in 1..11 step 2) {
            for (x in 9..19 step 2) {
                list.add(GridPoint(1, y, x))
            }
        }

        // Layer 2: x in 11..17, y in 3..9 -> Rotated
        for (y in 3..9 step 2) {
            for (x in 11..17 step 2) {
                list.add(GridPoint(2, y, x))
            }
        }

        // Layer 3: x in 13..15, y in 5..7 -> Rotated
        for (y in 5..7 step 2) {
            for (x in 13..15 step 2) {
                list.add(GridPoint(3, y, x))
            }
        }

        // Layer 4: x=14, y=6 -> Rotated (6, 14)
        list.add(GridPoint(4, 6, 14))

        return list
    }
}
