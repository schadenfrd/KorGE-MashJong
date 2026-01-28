package com.creature.mashjong

import korlibs.image.color.Colors
import korlibs.image.color.RGBA
import korlibs.korge.Korge
import korlibs.korge.KorgeDisplayMode
import korlibs.korge.internal.DefaultViewport
import korlibs.korge.view.Stage
import korlibs.math.geom.Anchor
import korlibs.math.geom.Scale
import korlibs.math.geom.ScaleMode
import korlibs.math.geom.Size
import korlibs.render.GameWindow

suspend fun main() = createGameWindow()

suspend fun createGameWindow(
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

    title: String = "Mish-MashJong",
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
    startMashjong(onClose = onClose)
}

suspend fun Stage.startMashjong(onClose: () -> Unit) {
    removeChildren()

    // Use the Orchestrator to manage the game session
    val orchestrator = GameOrchestrator(onClose = onClose)

    // Add to stage BEFORE calling start, so it has access to stage/views
    addChild(view = orchestrator)

    orchestrator.start()
}