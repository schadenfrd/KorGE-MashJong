package com.creature.mashjong.layout

import com.creature.mashjong.TilePosition
import kotlin.math.abs

class TurtleLayoutStrategy : LayoutStrategy {

    override fun getLayoutSlots(): List<LayoutSlot> {
        val list = mutableListOf<LayoutSlot>()

        // Layer 0 (Base): 87 tiles
        // 7 rows of 12 (x=2..24, y=0..12) -> Rotated: x=0..12, y=2..24
        for (y in 0..12 step 2) {
            for (x in 2..24 step 2) {
                list.add(LayoutSlot(0, y, x)) // Swapped x/y for portrait as per original LevelGenerator
            }
        }
        // Ears (3 tiles) - Rotated
        list.add(LayoutSlot(0, 6, 0))  // Top (was Left)
        list.add(LayoutSlot(0, 6, 26)) // Bottom (was Right 1)
        list.add(LayoutSlot(0, 6, 28)) // Bottom (was Right 2)

        // Layer 1 (6x6): 36 tiles
        // Center is now (6, 14)
        // x in 9..19, y in 1..11 -> Rotated: x in 1..11, y in 9..19
        for (y in 1..11 step 2) {
            for (x in 9..19 step 2) {
                list.add(LayoutSlot(1, y, x))
            }
        }

        // Layer 2: x in 11..17, y in 3..9 -> Rotated
        for (y in 3..9 step 2) {
            for (x in 11..17 step 2) {
                list.add(LayoutSlot(2, y, x))
            }
        }

        // Layer 3: x in 13..15, y in 5..7 -> Rotated
        for (y in 5..7 step 2) {
            for (x in 13..15 step 2) {
                list.add(LayoutSlot(3, y, x))
            }
        }

        // Layer 4: x=14, y=6 -> Rotated (6, 14)
        list.add(LayoutSlot(4, 6, 14))

        return list
    }

    override fun isTileBlocked(tile: TilePosition, activeTiles: Collection<TilePosition>): Boolean {
        // 1. Check Top Cover (Layer + 1)
        // A tile is covered if any tile on the layer above overlaps it.
        // Coordinate system: Grid steps of 2. Tile size ~2x2.
        // Overlap condition: abs(x1 - x2) < 2 && abs(y1 - y2) < 2
        val covered = activeTiles.any { other ->
            other.layer == tile.layer + 1 &&
            abs(other.x - tile.x) < 2 &&
            abs(other.y - tile.y) < 2
        }
        if (covered) return true
        
        // 2. Check Sides (Left/Right) at same layer
        // A tile is blocked if it has neighbors on BOTH Left and Right.
        // Left Neighbor: x = tile.x - 2, overlapping Y
        // Right Neighbor: x = tile.x + 2, overlapping Y
        
        val hasLeft = activeTiles.any { other ->
            other.layer == tile.layer &&
            other.x == tile.x - 2 &&
            abs(other.y - tile.y) < 2
        }
        
        val hasRight = activeTiles.any { other ->
            other.layer == tile.layer &&
            other.x == tile.x + 2 &&
            abs(other.y - tile.y) < 2
        }
        
        // Blocked if it has BOTH (Left AND Right)
        return hasLeft && hasRight
    }
}
