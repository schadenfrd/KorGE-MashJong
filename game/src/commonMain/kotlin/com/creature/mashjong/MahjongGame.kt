package com.creature.mashjong

import com.creature.mashjong.layout.LayoutStrategy

sealed class MatchResult {
    data object Ignored : MatchResult()
    data object Blocked : MatchResult()
    data object Deselected : MatchResult()
    data class Selected(val tile: TilePosition) : MatchResult()
    data class Match(val tileA: TilePosition, val tileB: TilePosition) : MatchResult()
}

class MahjongGame(
    initialTiles: List<TilePosition>,
    private val layoutStrategy: LayoutStrategy,
    private val tileInfoProvider: (Int) -> TileInfo?
) {
    private val tiles = initialTiles.toMutableList()

    // New Features state
    val maxUndos = 3
    val maxHints = 3
    var undosRemaining = maxUndos
        private set
    var hintsRemaining = maxHints
        private set
    private val history = mutableListOf<Pair<TilePosition, TilePosition>>()

    var selectedTile: TilePosition? = null
        private set

    fun getActiveTiles(): List<TilePosition> = tiles.toList()

    fun undo(): List<TilePosition> {
        if (undosRemaining <= 0 || history.isEmpty()) return emptyList()

        val lastMove = history.removeAt(history.lastIndex)
        tiles.add(lastMove.first)
        tiles.add(lastMove.second)

        undosRemaining--
        selectedTile = null

        return listOf(lastMove.first, lastMove.second)
    }

    fun getHint(): Pair<TilePosition, TilePosition>? {
        if (hintsRemaining <= 0) return null

        val active = getActiveTiles()
        val freeTiles = active.filter { isTileFree(it) }

        for (i in freeTiles.indices) {
            for (j in i + 1 until freeTiles.size) {
                val t1 = freeTiles[i]
                val t2 = freeTiles[j]

                if (isMatch(t1, t2)) {
                    hintsRemaining--

                    return t1 to t2
                }
            }
        }

        return null
    }

    fun hasValidMoves(): Boolean {
        val active = getActiveTiles()
        val freeTiles = active.filter(predicate = ::isTileFree)

        for (i in freeTiles.indices) {
            for (j in i + 1 until freeTiles.size) {
                if (isMatch(freeTiles[i], freeTiles[j])) return true
            }
        }
        return false
    }

    fun isTileFree(tile: TilePosition): Boolean =
        !layoutStrategy.isTileBlocked(tile = tile, activeTiles = tiles)

    fun isMatch(tileA: TilePosition, tileB: TilePosition): Boolean {
        // If exact same instance (should be handled by caller usually, but logic here: same tile is not a match with itself)
        if (tileA == tileB) return false

        val infoA = tileInfoProvider(tileA.tileId) ?: return false
        val infoB = tileInfoProvider(tileB.tileId) ?: return false

        // 1. Suits must match (except special cases logic below handles suit grouping)
        // Actually, Flowers and Seasons have their own suits.
        if (infoA.suit != infoB.suit) return false

        // 2. Value logic
        return when (infoA.suit) {
            TileSuit.FLOWERS -> true // Any Flower matches any Flower
            TileSuit.SEASONS -> true // Any Season matches any Season
            else -> infoA.value == infoB.value // Standard: Value must match (e.g. 5 Bamboo == 5 Bamboo)
        }
    }

    fun onTileClick(tile: TilePosition): MatchResult {
        // Verify tile is still on board
        if (tile !in tiles) return MatchResult.Ignored

        // Verify tile is free
        if (!isTileFree(tile)) return MatchResult.Blocked

        val currentSelection = selectedTile

        if (currentSelection == null) {
            selectedTile = tile

            return MatchResult.Selected(tile)
        } else {
            if (currentSelection == tile) {
                // Clicking the selected tile again -> Deselect
                selectedTile = null

                return MatchResult.Deselected
            }

            if (isMatch(tileA = currentSelection, tileB = tile)) {
                // Valid Match
                tiles.remove(currentSelection)
                tiles.remove(tile)
                history.add(currentSelection to tile)
                selectedTile = null

                return MatchResult.Match(tileA = currentSelection, tileB = tile)
            } else {
                // Not a match -> Select the new tile
                selectedTile = tile

                return MatchResult.Selected(tile)
            }
        }
    }
}
