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

    private val _shufflesRemaining = MutableStateFlow(game.shufflesRemaining)
    val shufflesRemaining = _shufflesRemaining.asStateFlow()

    private val _gameState = MutableStateFlow(GameState.PLAYING)
    val gameState = _gameState.asStateFlow()

    fun onUndo(): List<TilePosition> {
        val restored = game.undo()
        _undosRemaining.value = game.undosRemaining
        
        // After undo, we might have moves again
        if (game.hasValidMoves()) {
            _gameState.value = GameState.PLAYING
        }
        
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
        _shufflesRemaining.value = game.shufflesRemaining
        
        // After shuffle, check if we have moves (usually yes, but theoretically could still be stuck if bad luck? 
        // Although shuffle keeps same tiles, just moves them? No, shuffle rearranges IDs on positions.
        // So configuration changes. Check moves again.)
        if (game.hasValidMoves()) {
            _gameState.value = GameState.PLAYING
        } else {
            // Still stuck after shuffle? Unlikely but possible.
            // Recursively check game over logic?
             if (game.shufflesRemaining > 0 || game.undosRemaining > 0) {
                 _gameState.value = GameState.NO_MOVES
             } else {
                 _gameState.value = GameState.LOST
             }
        }
        
        return game.getActiveTiles()
    }

    fun onTileClick(tile: TilePosition): MatchResult {
        val result = game.onTileClick(tile)
        
        if (game.getActiveTiles().isEmpty()) {
            _gameState.value = GameState.WON
        } else if (!game.hasValidMoves()) {
            if (game.shufflesRemaining > 0 || game.undosRemaining > 0) {
                _gameState.value = GameState.NO_MOVES
            } else {
                _gameState.value = GameState.LOST
            }
        }
        
        return result
    }
}
