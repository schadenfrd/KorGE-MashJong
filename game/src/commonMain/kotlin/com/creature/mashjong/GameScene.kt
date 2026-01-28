package com.creature.mashjong

import com.creature.mashjong.layout.TurtleLayoutStrategy
import korlibs.image.color.Colors
import korlibs.image.format.readBitmap
import korlibs.io.file.std.resourcesVfs
import korlibs.korge.input.onClick
import korlibs.korge.ui.uiButton
import korlibs.korge.view.Container
import korlibs.korge.view.align.alignBottomToBottomOf
import korlibs.korge.view.align.alignRightToLeftOf
import korlibs.korge.view.align.alignRightToRightOf
import korlibs.korge.view.align.alignTopToTopOf
import korlibs.korge.view.align.centerOn
import korlibs.korge.view.align.centerOnStage
import korlibs.korge.view.container
import korlibs.korge.view.position
import korlibs.korge.view.solidRect
import korlibs.korge.view.text
import korlibs.math.geom.Size

class GameScene(val onClose: () -> Unit) : Container() {

    suspend fun initialize() {
        val views = stage?.views ?: return
        val screenWidth = views.virtualWidthDouble
        val screenHeight = views.virtualHeightDouble

        // Layers
        val gameLayer = container()
        val hudLayer = container()
        hudLayer.solidRect(screenWidth, screenHeight, Colors.BLUE.withAd(0.2))
        gameLayer.solidRect(screenWidth, screenHeight, Colors.RED.withAd(0.2))

        // 1. Initialize Factory and load assets
        val tileFactory = TileFactory()
        val atlas = resourcesVfs["atlas_mish_mashjong.png"].readBitmap()
        tileFactory.loadAtlas(atlas = atlas)

        // 2. Create the Deck and Generate Level
        val deck = tileFactory.createDeck()
        val levelData = LevelGenerator.generateTurtleLayout(deck)

        // 3. Initialize Game Logic
        val game = MahjongGame(
            initialTiles = levelData,
            layoutStrategy = TurtleLayoutStrategy(),
            tileInfoProvider = tileFactory::getTileInfo
        )

        // 4. Create BoardView with interaction
        lateinit var boardView: BoardView

        boardView = BoardView(factory = tileFactory) { pos ->
            when (val result = game.onTileClick(pos)) {
                MatchResult.Ignored -> Unit
                MatchResult.Blocked -> {
                    println("Blocked: $pos")
                }

                MatchResult.Deselected -> {
                    boardView.setSelection(null)
                }

                is MatchResult.Selected -> {
                    boardView.setSelection(result.tile)
                }

                is MatchResult.Match -> {
                    boardView.removeTiles(listOf(result.tileA, result.tileB))
                    boardView.setSelection(null)

                    if (game.getActiveTiles().isEmpty()) {
                        showGameOver(victory = true)
                    } else if (!game.hasValidMoves()) {
                        showGameOver(victory = false)
                    }
                }
            }
        }

        gameLayer.addChild(boardView)
        boardView.renderBoard(tiles = levelData)

        // UI Scaling: Reference 720p short edge
        val shortEdge = if (screenWidth < screenHeight) screenWidth else screenHeight
        val uiScale = shortEdge / 720.0

        val topBarHeight = 80.0 * uiScale
        val bottomBarHeight = 100.0 * uiScale
        val safeAreaHeight = screenHeight - topBarHeight - bottomBarHeight

        // Scale and Position Board
        val boardBounds = boardView.getLocalBounds()
        // Avoid division by zero
        if (boardBounds.width > 0 && boardBounds.height > 0) {
            val scaleW = (screenWidth * 0.95) / boardBounds.width
            val scaleH = (safeAreaHeight * 0.95) / boardBounds.height
            val finalScale = if (scaleW < scaleH) scaleW else scaleH

            boardView.scaleX = finalScale
            boardView.scaleY = finalScale

            // Center in the safe area
            val safeCenterX = screenWidth / 2
            val safeCenterY = topBarHeight + (safeAreaHeight / 2)

            val scaledWidth = boardBounds.width * finalScale
            val scaledHeight = boardBounds.height * finalScale

            val newX = safeCenterX - (scaledWidth / 2) - (boardBounds.x * finalScale)
            val newY = safeCenterY - (scaledHeight / 2) - (boardBounds.y * finalScale)

            boardView.position(newX, newY)
        }

        // 5. HUD
        val btnWidth = 160.0 * uiScale
        val btnHeight = 70.0 * uiScale
        val padding = 40.0 * uiScale

        // Quit Button (Top Right)
        hudLayer.uiButton(label = "Quit", size = Size(btnWidth, btnHeight)) {
            textSize = 30.0 * uiScale
            alignRightToRightOf(hudLayer, padding = padding)
            alignTopToTopOf(hudLayer, padding = 100)
            onClick {
                onClose()
            }
        }

        // Hint Button (Bottom Right)
        val hintBtn =
            hudLayer.uiButton(label = "Hint (${game.hintsRemaining})", size = Size(btnWidth, btnHeight)) {
                textSize = 30.0 * uiScale
                alignRightToRightOf(hudLayer, padding = padding)
                alignBottomToBottomOf(hudLayer, padding = padding)
                onClick {
                    val hint = game.getHint()
                    if (hint != null) {
                        boardView.setSelection(hint.first)
                        text = "Hint (${game.hintsRemaining})"
                    }
                }
            }

        // Undo Button (Left of Hint)
        hudLayer.uiButton(label = "Undo (${game.undosRemaining})", size = Size(btnWidth, btnHeight)) {
            textSize = 30.0 * uiScale
            alignRightToLeftOf(hintBtn, padding = 20)
            alignBottomToBottomOf(hudLayer, padding = padding)
            onClick {
                val restored = game.undo()
                if (restored.isNotEmpty()) {
                    boardView.addTiles(restored)
                    text = "Undo (${game.undosRemaining})"
                }
            }
        }
    }

    private fun showGameOver(victory: Boolean) {
        container {
            val bg = solidRect(500.0, 400.0, Colors.BLACK.withAd(0.9))
            // Center on stage or parent
            centerOnStage()

            val msg = if (victory) "VICTORY!" else "No More Moves!"
            val color = if (victory) Colors.GOLD else Colors.RED

            text(msg, textSize = 50.0, color = color) {
                centerOn(bg)
                y -= 60
            }

            uiButton(label = "Back to Menu", size = Size(200, 70)) {
                centerOn(bg)
                y += 60
                onClick {
                    onClose()
                }
            }
        }
    }
}
