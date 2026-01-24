package com.creature.mashjong

import korlibs.korge.view.Container
import korlibs.korge.view.xy
import korlibs.math.geom.Point

// Simple data class to hold a tile's logical position
data class TilePosition(val layer: Int, val x: Int, val y: Int, val tileId: Int)

class BoardView(val factory: TileFactory) : Container() {
    
    // Constants for positioning
    private val overlapX = Tile.WIDTH / 2
    private val overlapY = Tile.HEIGHT / 2
    private val layerOffset = Point(x = -4.0, y = -6.0)

    fun renderBoard(tiles: List<TilePosition>) {
        removeChildren() // Clear existing board

        // CRITICAL: Sort tiles so bottom layers are drawn first!
        // Sort by Layer (ascending), then Y (ascending), then X (ascending)
        val sortedTiles = tiles.sortedWith(
            compareBy<TilePosition> { it.layer }
                .thenBy { it.y }
                .thenBy { it.x }
        )

        for (pos in sortedTiles) {
            val tile = factory.createTile(pos.tileId)
            
            // Calculate 2D screen position from 3D grid
            var drawX = pos.x * overlapX
            var drawY = pos.y * overlapY
            
            // Apply layer depth shift
            drawX += pos.layer * layerOffset.x
            drawY += pos.layer * layerOffset.y

            tile.xy(drawX, drawY)
            
            // Store the logical position in the view for gameplay logic later (clicking)
            tile.name = "Tile_${pos.layer}_${pos.x}_${pos.y}"
            
            addChild(tile)
        }
    }
}
