package com.creature.mashjong.domain.logic

import com.creature.mashjong.domain.model.MatchResult
import com.creature.mashjong.domain.model.TileInfo
import com.creature.mashjong.domain.model.TilePosition
import kotlin.math.abs

class MahjongGame(
    initialTiles: List<TilePosition>,
    private val tileInfoProvider: (Int) -> TileInfo?,
    val maxUndos: Int = 3,
    val maxHints: Int = 3,
    private val ruleSet: RuleSet = SolitaireMatchRule
) {
    private val tiles = initialTiles.toMutableList()

    // New Features state
    var undosRemaining = maxUndos
        private set
    var hintsRemaining = maxHints
        private set
    private val commandHistory = mutableListOf<Command>()

    var selectedTile: TilePosition? = null
        private set

    fun getActiveTiles(): List<TilePosition> = tiles.toList()

    fun undo(): List<TilePosition> {
        if (undosRemaining <= 0 || commandHistory.isEmpty()) return emptyList()

        val command = commandHistory.removeAt(commandHistory.lastIndex)
        command.undo()

        undosRemaining--
        selectedTile = null

        return if (command is MatchCommand) {
            listOf(command.tileA, command.tileB)
        } else {
            emptyList()
        }
    }

    internal fun removeTile(tile: TilePosition) {
        tiles.remove(tile)
    }

    internal fun addTile(tile: TilePosition) {
        tiles.add(tile)
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
        !isTileBlocked(tile)

    fun isMatch(tileA: TilePosition, tileB: TilePosition): Boolean {
        // If exact same instance (should be handled by caller usually, but logic here: same tile is not a match with itself)
        if (tileA == tileB) return false

        val infoA = tileInfoProvider(tileA.tileId) ?: return false
        val infoB = tileInfoProvider(tileB.tileId) ?: return false

        return ruleSet.isMatch(infoA.definition, infoB.definition)
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
                val command = MatchCommand(this, currentSelection, tile)
                command.execute()
                commandHistory.add(command)

                selectedTile = null

                return MatchResult.Match(tileA = currentSelection, tileB = tile)
            } else {
                // Not a match -> Select the new tile
                selectedTile = tile

                return MatchResult.Selected(tile)
            }
        }
    }

    private fun isTileBlocked(tile: TilePosition): Boolean {
        val covered = tiles.any { other ->
            other.layer == tile.layer + 1 &&
                    abs(other.x - tile.x) < 2 &&
                    abs(other.y - tile.y) < 2
        }
        if (covered) return true

        val hasLeft = tiles.any { other ->
            other.layer == tile.layer &&
                    other.x == tile.x - 2 &&
                    abs(other.y - tile.y) < 2
        }

        val hasRight = tiles.any { other ->
            other.layer == tile.layer &&
                    other.x == tile.x + 2 &&
                    abs(other.y - tile.y) < 2
        }

        return hasLeft && hasRight
    }
}

