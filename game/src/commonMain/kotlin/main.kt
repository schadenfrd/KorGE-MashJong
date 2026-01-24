package com.creature.mashjong

import korlibs.image.color.Colors
import korlibs.image.color.RGBA
import korlibs.image.format.readBitmap
import korlibs.io.file.std.resourcesVfs
import korlibs.korge.Korge
import korlibs.korge.KorgeDisplayMode
import korlibs.korge.internal.DefaultViewport
import korlibs.korge.view.Stage
import korlibs.korge.view.align.centerOnStage
import korlibs.math.geom.Anchor
import korlibs.math.geom.Scale
import korlibs.math.geom.ScaleMode
import korlibs.math.geom.Size
import korlibs.render.GameWindow

suspend fun main() = gameStart()

suspend fun gameStart(
    gameWindow: GameWindow? = null,
    scale: Scale = Scale(scale = 1),
    windowSize: Size = Size(
        width = DefaultViewport.SIZE.width.toInt(),
        height = DefaultViewport.SIZE.height.toInt()
    ),
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
    mainStage()
}

private suspend fun Stage.mainStage() {
    // 1. Initialize Factory and load assets
    val tileFactory = TileFactory()
    val atlas = resourcesVfs["atlas_classic_mahjong.png"].readBitmap()
    tileFactory.loadAtlas(atlas = atlas)

    // 2. Create BoardView
    val boardView = BoardView(factory = tileFactory)
    addChild(view = boardView)

    // 3. Create the Deck and Generate Level
    val deck = tileFactory.createDeck()
    val levelData = LevelGenerator.generateTurtleLayout(deck)

    // 4. Render
    boardView.renderBoard(tiles = levelData)

    // 5. Center on stage
    boardView.centerOnStage()
}