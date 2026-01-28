package com.creature.mashjong.presentation.view

import com.creature.mashjong.domain.model.TilePosition
import com.creature.mashjong.presentation.infrastructure.TileFactory
import korlibs.korge.input.onClick
import korlibs.korge.input.onOut
import korlibs.korge.input.onOver
import korlibs.korge.tween.get
import korlibs.korge.tween.tween
import korlibs.korge.view.Container
import korlibs.korge.view.xy
import korlibs.math.geom.Point
import korlibs.time.milliseconds
import korlibs.io.async.launchImmediately
import kotlinx.coroutines.Dispatchers

class BoardView(
    val factory: TileFactory,
    val onTileClick: (TilePosition) -> Unit,
    val isBlockedPredicate: (TilePosition) -> Boolean
) : Container() {

    // Constants for positioning
    private val overlapX = Tile.WIDTH / 2
    private val overlapY = Tile.HEIGHT / 2
    private val layerOffset = Point(x = -10.0, y = -14.0)

    private val tileViews = mutableMapOf<TilePosition, Tile>()

    fun renderBoard(tiles: List<TilePosition>) {
        removeChildren() // Clear existing board
        tileViews.clear()

        // CRITICAL: Sort tiles so bottom layers are drawn first!
        // Sort by Layer (ascending), then Y (ascending), then X (ascending)
        val sortedTiles = tiles.sortedWith(
            comparator = compareBy<TilePosition> { it.layer }
                .thenBy { it.y }
                .thenBy { it.x }
        )

        addTiles(tilesToAdd = sortedTiles)
    }

    fun addTiles(tilesToAdd: List<TilePosition>, animate: Boolean = false) {
        for (pos in tilesToAdd) {
            if (tileViews.containsKey(pos)) continue

            val tile = factory.createTile(pos.tileId, pos.layer)

            // Calculate 2D screen position from 3D grid
            var drawX = pos.x * overlapX
            var drawY = pos.y * overlapY

            // Apply layer depth shift
            drawX += pos.layer * layerOffset.x
            drawY += pos.layer * layerOffset.y

            tile.xy(x = drawX, y = drawY)

            // Store the logical position in the view for gameplay logic later (clicking)
            tile.name = "Tile_${pos.layer}_${pos.x}_${pos.y}"

            tile.onClick {
                onTileClick(pos)
            }

            tile.onOver {
                if (!isBlockedPredicate(pos)) {
                    tile.scale = 1.05
                }
            }

            tile.onOut {
                tile.scale = 1.0
            }

            tileViews[pos] = tile
            addChild(tile)

            if (animate) {
                tile.alpha = 0.0
                tile.scale = 0.5

                launchImmediately(Dispatchers.Unconfined) {
                    tile.tween(tile::alpha[1.0], tile::scale[1.0], time = 300.milliseconds)
                }
            }
        }
    }

    fun setSelection(tilePos: TilePosition?) {
        // Unselect all
        tileViews.values.forEach { it.setSelect(false) }

        if (tilePos != null) {
            tileViews[tilePos]?.setSelect(true)
        }
    }

    fun removeTiles(tilesToRemove: List<TilePosition>) {
        for (pos in tilesToRemove) {
            val tile = tileViews[pos]

            if (tile != null) {
                tile.removeFromParent()
                tileViews.remove(pos)
            }
        }
    }

    fun refreshVisuals() {
        for ((pos, tile) in tileViews) {
            val blocked = isBlockedPredicate(pos)
            tile.setBlocked(blocked)
        }
    }

    fun showHint(t1: TilePosition, t2: TilePosition) {
        val view1 = tileViews[t1]
        val view2 = tileViews[t2]

        launchImmediately(Dispatchers.Unconfined) { view1?.pulse() }
        launchImmediately(Dispatchers.Unconfined) { view2?.pulse() }
    }
}
