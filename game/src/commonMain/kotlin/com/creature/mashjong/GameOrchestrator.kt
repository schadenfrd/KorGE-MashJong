package com.creature.mashjong

import com.creature.mashjong.data.LevelGenerator
import com.creature.mashjong.domain.logic.MahjongGame
import com.creature.mashjong.domain.model.GameState
import com.creature.mashjong.domain.model.MatchResult
import com.creature.mashjong.domain.model.TilePosition
import com.creature.mashjong.presentation.infrastructure.TileFactory
import com.creature.mashjong.presentation.view.BoardView
import com.creature.mashjong.presentation.view.GameScene
import com.creature.mashjong.presentation.view.HudScene
import com.creature.mashjong.presentation.viewmodel.GameViewModel
import korlibs.image.format.readBitmap
import korlibs.io.file.std.resourcesVfs
import korlibs.korge.view.Container

class GameOrchestrator(val onClose: () -> Unit) : Container() {
    private lateinit var viewModel: GameViewModel
    private lateinit var game: MahjongGame
    private lateinit var boardView: BoardView
    private lateinit var gameScene: GameScene
    private lateinit var hudScene: HudScene
    private var isGameOver = false

    suspend fun start(settings: GameSettings = GameSettings()) {
        // 1. Load Assets & Create Game
        val tileFactory = TileFactory()
        val atlas = resourcesVfs[settings.atlasPath].readBitmap()
        tileFactory.loadAtlas(atlas = atlas)

        val deck = tileFactory.createDeck()
        // Extract just the logic info for the generator
        val levelData = LevelGenerator
            .generateLayout(deck = deck, strategy = settings.layoutStrategy)
        val game = MahjongGame(
            initialTiles = levelData,
            tileInfoProvider = tileFactory::getTileInfo,
            maxUndos = settings.maxUndos,
            maxHints = settings.maxHints,
            maxShuffles = settings.maxShuffles
        )
        this.game = game
        viewModel = GameViewModel(game = game)

        // 2. Create Views
        // We pass a callback to the board view for tile clicks
        boardView = BoardView(
            factory = tileFactory,
            onTileClick = ::handleTileClick,
            isBlockedPredicate = game::isTileBlocked
        )

        // Pass boardView to GameScene so it only handles layout/rendering
        gameScene = GameScene(boardView = boardView)

        // 3. Create HUD with Callbacks
        hudScene = HudScene(
            viewModel = viewModel,
            onUndo = ::handleUndo,
            onHint = ::handleHint,
            onShuffle = ::handleShuffle,
            onClose = onClose
        )

        // 4. Add to hierarchy (Scenes)
        addChild(view = gameScene)
        addChild(view = hudScene)

        // 5. Initialize/Layout Views
        // Render the initial board state
        boardView.renderBoard(tiles = levelData)

        // Tell GameScene to layout the board (it handles scaling/centering)
        gameScene.layoutBoard()

        // Initialize HUD (it handles its own layout/background)
        hudScene.initialize()

        // Initial visual refresh to show blocked states
        boardView.refreshVisuals()
    }

    private fun handleTileClick(pos: TilePosition) {
        if (isGameOver) return

        when (val result = viewModel.onTileClick(pos)) {
            MatchResult.Ignored -> Unit
            MatchResult.Blocked -> println("Blocked: $pos")
            MatchResult.Deselected -> boardView.setSelection(null)
            is MatchResult.Selected -> boardView.setSelection(result.tile)
            is MatchResult.Match -> {
                boardView.removeTiles(listOf(result.tileA, result.tileB))
                boardView.setSelection(null)
                boardView.refreshVisuals()
                checkGameOver()
            }
        }
    }

    private fun handleUndo() {
        // If truly game over (WON/LOST), don't allow undo (unless we want to allow undoing the winning move? maybe, but let's stick to simple)
        if (isGameOver) return
        
        val restored = viewModel.onUndo()
        if (restored.isNotEmpty()) {
            boardView.addTiles(tilesToAdd = restored, animate = true)
            boardView.refreshVisuals()
        }
        
        // Check if state changed (e.g. back to PLAYING or stuck)
        checkGameOver()
    }

    private fun handleHint() {
        if (isGameOver) return
        val hint = viewModel.onHint()

        if (hint != null) {
            boardView.setSelection(hint.first)
            boardView.showHint(t1 = hint.first, t2 = hint.second)
        }
    }

    private fun handleShuffle() {
        if (isGameOver) return
        val newTiles = viewModel.onShuffle()
        boardView.renderBoard(tiles = newTiles)
        boardView.refreshVisuals()
        checkGameOver()
    }

    private fun checkGameOver() {
        val state = viewModel.gameState.value
        
        if (state == GameState.WON || state == GameState.LOST) {
            isGameOver = true
            hudScene.showGameEnd(state)
        } else if (state == GameState.NO_MOVES) {
            isGameOver = false // Allow interactions (Undo/Shuffle)
            hudScene.showGameEnd(state)
        }
    }
}
