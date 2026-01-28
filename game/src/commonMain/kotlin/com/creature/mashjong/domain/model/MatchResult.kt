package com.creature.mashjong.domain.model

sealed class MatchResult {
    data object Ignored : MatchResult()
    data object Blocked : MatchResult()
    data object Deselected : MatchResult()
    data class Selected(val tile: TilePosition) : MatchResult()
    data class Match(val tileA: TilePosition, val tileB: TilePosition) : MatchResult()
}
