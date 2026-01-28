package com.creature.mashjong.presentation.viewmodel

import com.creature.mashjong.domain.logic.MahjongGame
import com.creature.mashjong.domain.model.GameState
import com.creature.mashjong.domain.model.MatchResult
import com.creature.mashjong.domain.model.TilePosition
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class GameViewModel(private val game: MahjongGame) {
    private val _undosRemaining = MutableStateFlow(game.undosRemaining)
    val undosRemaining = _undosRemaining.asStateFlow()

    private val _hintsRemaining = MutableStateFlow(game.hintsRemaining)
    val hintsRemaining = _hintsRemaining.asStateFlow()

    private val _gameState = MutableStateFlow(GameState.PLAYING)
    val gameState = _gameState.asStateFlow()

    fun onUndo(): List<TilePosition> {
        val restored = game.undo()
        _undosRemaining.value = game.undosRemaining
        return restored
    }

    fun onHint(): Pair<TilePosition, TilePosition>? {
        val hint = game.getHint()
        _hintsRemaining.value = game.hintsRemaining
        return hint
    }

    fun onShuffle(): List<TilePosition> {
        game.shuffle()
        // Shuffle clears history, so undos might not be relevant for *past* moves,
        // but let's update the state just in case.
        // Also if we reset undos to max, update it here.
        _undosRemaining.value = game.undosRemaining
        return game.getActiveTiles()
    }

    fun onTileClick(tile: TilePosition): MatchResult {
        val result = game.onTileClick(tile)
        
        if (game.getActiveTiles().isEmpty()) {
            _gameState.value = GameState.WON
        } else if (!game.hasValidMoves()) {
            _gameState.value = GameState.LOST
        }
        
        return result
    }
}
