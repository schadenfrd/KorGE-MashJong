package com.creature.mashjong

import korlibs.image.color.Colors
import korlibs.image.color.RGBA
import korlibs.image.format.readBitmap
import korlibs.io.file.std.resourcesVfs
import korlibs.korge.Korge
import korlibs.korge.KorgeDisplayMode
import korlibs.korge.input.onClick
import korlibs.korge.internal.DefaultViewport
import korlibs.korge.ui.uiButton
import korlibs.korge.view.Container
import korlibs.korge.view.align.*
import korlibs.korge.view.container
import korlibs.korge.view.position
import korlibs.korge.view.solidRect
import korlibs.korge.view.text
import korlibs.math.geom.Anchor
import korlibs.math.geom.Scale
import korlibs.math.geom.ScaleMode
import korlibs.math.geom.Size
import korlibs.render.GameWindow

suspend fun main() = gameStart()

suspend fun gameStart(
    gameWindow: GameWindow? = null,
    scale: Scale = Scale(scale = 1),
    windowSize: Size = DefaultViewport.SIZE,
    virtualSize: Size = Size(
        width = windowSize.width / scale.scaleX,
        height = windowSize.height / scale.scaleY
    ),
    displayMode: KorgeDisplayMode = KorgeDisplayMode(
        scaleMode = ScaleMode.SHOW_ALL,
        scaleAnchor = Anchor.CENTER,
        clipBorders = false,
    ),
    fullScreen: Boolean = true,

    title: String = "Mish Mash-Jong",
    backgroundColor: RGBA? = Colors.DARKOLIVEGREEN,
    quality: GameWindow.Quality = GameWindow.Quality.QUALITY,
    onClose: () -> Unit = {},
) = Korge(
    fullscreen = fullScreen,
    gameWindow = gameWindow,
    windowSize = windowSize,
    virtualSize = virtualSize,
    backgroundColor = backgroundColor,
    displayMode = displayMode,
    quality = quality,
    title = title,
) {
    startGame(onClose)
}

suspend fun Container.startGame(onClose: () -> Unit) {
    removeChildren()

    // 1. Initialize Factory and load assets
    val tileFactory = TileFactory()
    val atlas = resourcesVfs["atlas_mish_mashjong.png"].readBitmap()
    tileFactory.loadAtlas(atlas = atlas)

    // 2. Create the Deck and Generate Level
    val deck = tileFactory.createDeck()
    val levelData = LevelGenerator.generateTurtleLayout(deck)

    // 3. Initialize Game Logic
    val game = MahjongGame(levelData) { id -> tileFactory.getTileInfo(id) }

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
                    showGameOver(victory = true, onClose)
                } else if (!game.hasValidMoves()) {
                    showGameOver(victory = false, onClose)
                }
            }
        }
    }

    addChild(boardView)
    boardView.renderBoard(tiles = levelData)

    // Dynamic Layout
    val screenWidth = this.width
    val screenHeight = this.height

    // UI Scaling: Reference 720p short edge
    // Use the smaller dimension to scale UI elements consistently (Portrait or Landscape)
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
    val padding = 20.0 * uiScale
    val topPadding = 50.0 * uiScale

    // Quit Button (Top Right)
    uiButton(label = "Quit", size = Size(btnWidth, btnHeight)) {
        textSize = 30.0 * uiScale
        alignRightToRightOf(this@startGame, padding = padding)
        alignTopToTopOf(this@startGame, padding = topPadding)
        onClick {
            onClose()
        }
    }

    // Hint Button (Bottom Right)
    val hintBtn =
        uiButton(label = "Hint (${game.hintsRemaining})", size = Size(btnWidth, btnHeight)) {
            textSize = 30.0 * uiScale
            alignRightToRightOf(this@startGame, padding = padding)
            alignBottomToBottomOf(this@startGame, padding = padding)
            onClick {
                val hint = game.getHint()
                if (hint != null) {
                    boardView.setSelection(hint.first)
                    text = "Hint (${game.hintsRemaining})"
                }
            }
        }

    // Undo Button (Left of Hint)
    uiButton(label = "Undo (${game.undosRemaining})", size = Size(btnWidth, btnHeight)) {
        textSize = 30.0 * uiScale
        alignRightToLeftOf(hintBtn, padding = padding)
        alignBottomToBottomOf(this@startGame, padding = padding)
        onClick {
            if (game.undo()) {
                boardView.renderBoard(game.getActiveTiles())
                text = "Undo (${game.undosRemaining})"
            }
        }
    }
}

fun Container.showGameOver(victory: Boolean, onClose: () -> Unit) {
    container {
        val bg = solidRect(500.0, 400.0, Colors.BLACK.withAd(0.9))
        bg.centerOnStage()

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