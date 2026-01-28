package com.creature.mashjong

import com.creature.mashjong.layout.TurtleLayoutStrategy
import korlibs.image.format.readBitmap
import korlibs.io.file.std.resourcesVfs
import korlibs.korge.view.Container

class GameOrchestrator(val onClose: () -> Unit) : Container() {
    private lateinit var viewModel: GameViewModel
    private lateinit var boardView: BoardView
    private lateinit var gameScene: GameScene
    private lateinit var hudScene: HudScene
    private var isGameOver = false

    suspend fun start() {
        // 1. Load Assets & Create Game
        val tileFactory = TileFactory()
        val atlas = resourcesVfs["atlas_mish_mashjong.png"].readBitmap()
        tileFactory.loadAtlas(atlas = atlas)

        val deck = tileFactory.createDeck()
        val strategy = TurtleLayoutStrategy()
        val levelData = LevelGenerator.generateLayout(deck, strategy)

        val game = MahjongGame(
            initialTiles = levelData,
            tileInfoProvider = tileFactory::getTileInfo
        )
        viewModel = GameViewModel(game)

        // 2. Create Views
        // We pass a callback to the board view for tile clicks
        boardView = BoardView(
            factory = tileFactory,
            onTileClick = ::handleTileClick
        )

        // Pass boardView to GameScene so it only handles layout/rendering
        gameScene = GameScene(boardView = boardView)

        // 3. Create HUD with Callbacks
        hudScene = HudScene(
            viewModel = viewModel,
            onUndo = ::handleUndo,
            onHint = ::handleHint,
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
                checkGameOver()
            }
        }
    }

    private fun handleUndo() {
        if (isGameOver) return
        val restored = viewModel.onUndo()
        if (restored.isNotEmpty()) {
            boardView.addTiles(restored)
        }
    }

    private fun handleHint() {
        if (isGameOver) return
        val hint = viewModel.onHint()
        if (hint != null) {
            boardView.setSelection(hint.first)
        }
    }

    private fun checkGameOver() {
        val state = viewModel.gameState.value
        if (state != GameState.PLAYING) {
            isGameOver = true
            hudScene.showGameOver(victory = state == GameState.WON)
        }
    }
}
