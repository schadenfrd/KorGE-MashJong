package com.creature.mashjong

import korlibs.image.color.Colors
import korlibs.image.color.RGBA
import korlibs.image.text.TextAlignment
import korlibs.korge.Korge
import korlibs.korge.KorgeDisplayMode
import korlibs.korge.internal.DefaultViewport
import korlibs.korge.view.Stage
import korlibs.korge.view.align.centerOnStage
import korlibs.korge.view.text
import korlibs.logger.Logger
import korlibs.math.geom.Anchor
import korlibs.math.geom.Scale
import korlibs.math.geom.ScaleMode
import korlibs.math.geom.Size
import korlibs.render.GameWindow

val LOG_LEVEL = Logger.Level.DEBUG

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

private fun Stage.mainStage() {
    val helloText = text(
        text = "Hello Mish-Mash!",
        textSize = 128.0,
        color = Colors.CYAN,
        alignment = TextAlignment.MIDDLE_CENTER
    ).centerOnStage()

    addChild(helloText)
}