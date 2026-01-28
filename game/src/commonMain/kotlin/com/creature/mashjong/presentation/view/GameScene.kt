package com.creature.mashjong.presentation.view

import korlibs.image.color.Colors
import korlibs.korge.view.Container
import korlibs.korge.view.position
import korlibs.korge.view.solidRect
import kotlin.math.min

class GameScene(val boardView: BoardView) : Container() {

    fun layoutBoard() {
        removeChildren()

        val views = stage?.views ?: return
        val screenWidth = views.virtualWidthDouble
        val screenHeight = views.virtualHeightDouble

        // Background
        solidRect(screenWidth, screenHeight, Colors.TRANSPARENT)

        addChild(view = boardView)

        // UI Scaling: Reference 720p short edge
        val shortEdge = min(screenWidth, screenHeight)
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
    }
}
