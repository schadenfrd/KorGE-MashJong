package com.creature.mashjong.presentation.view

import com.creature.mashjong.presentation.viewmodel.GameViewModel

import com.creature.mashjong.domain.model.GameState
import korlibs.image.color.Colors
import korlibs.korge.input.onClick
import korlibs.korge.ui.uiButton
import korlibs.korge.view.Container
import korlibs.korge.view.addUpdater
import korlibs.korge.view.align.alignBottomToBottomOf
import korlibs.korge.view.align.alignRightToLeftOf
import korlibs.korge.view.align.alignRightToRightOf
import korlibs.korge.view.align.alignTopToTopOf
import korlibs.korge.view.align.centerOn
import korlibs.korge.view.align.centerOnStage
import korlibs.korge.view.container
import korlibs.korge.view.solidRect
import korlibs.korge.view.text
import korlibs.math.geom.Size

class HudScene(
    val viewModel: GameViewModel,
    val onUndo: () -> Unit,
    val onHint: () -> Unit,
    val onShuffle: () -> Unit,
    val onClose: () -> Unit
) : Container() {

    fun initialize() {
        removeChildren()

        val views = stage?.views ?: return
        val screenWidth = views.virtualWidthDouble
        val screenHeight = views.virtualHeightDouble

        // Transparent background for sizing/alignment
        // Disable mouse interaction so clicks pass through to the game board
        solidRect(
            width = screenWidth,
            height = screenHeight,
            color = Colors.TRANSPARENT
        ).mouseEnabled = false

        // UI Scaling: Reference 720p short edge
        val shortEdge = if (screenWidth < screenHeight) screenWidth else screenHeight
        val uiScale = shortEdge / 720.0

        val btnWidth = 160.0 * uiScale
        val btnHeight = 70.0 * uiScale
        val padding = 40.0 * uiScale

        // Quit Button (Top Right)
        uiButton(label = "Quit", size = Size(btnWidth, btnHeight)) {
            textSize = 30.0 * uiScale
            alignRightToRightOf(this@HudScene, padding = padding)
            alignTopToTopOf(this@HudScene, padding = 100)
            onClick {
                onClose()
            }
        }

        // Hint Button (Bottom Right)
        val hintBtn =
            uiButton(label = "Hint", size = Size(btnWidth, btnHeight)) {
                textSize = 30.0 * uiScale
                alignRightToRightOf(this@HudScene, padding = padding)
                alignBottomToBottomOf(this@HudScene, padding = padding)
                onClick {
                    onHint()
                }
                addUpdater {
                    text = "Hint (${viewModel.hintsRemaining.value})"
                    enabled = viewModel.hintsRemaining.value > 0
                }
            }

        // Undo Button (Left of Hint)
        val undoBtn = uiButton(label = "Undo", size = Size(btnWidth, btnHeight)) {
            textSize = 30.0 * uiScale
            alignRightToLeftOf(hintBtn, padding = 20)
            alignBottomToBottomOf(this@HudScene, padding = padding)
            onClick {
                onUndo()
            }
            addUpdater {
                text = "Undo (${viewModel.undosRemaining.value})"
                enabled = viewModel.undosRemaining.value > 0
            }
        }

        // Shuffle Button (Left of Undo)
        uiButton(label = "Shuffle", size = Size(btnWidth, btnHeight)) {
            textSize = 30.0 * uiScale
            alignRightToLeftOf(undoBtn, padding = 20)
            alignBottomToBottomOf(this@HudScene, padding = padding)
            onClick {
                onShuffle()
            }
            addUpdater {
                text = "Shuffle (${viewModel.shufflesRemaining.value})"
                enabled = viewModel.shufflesRemaining.value > 0
            }
        }
    }

    fun showGameEnd(state: GameState) {
        container {
            val bgWidth = 500.0
            val bgHeight = 400.0
            val bg = solidRect(width = bgWidth, height = bgHeight, color = Colors.FORESTGREEN)
            centerOnStage()

            // Block clicks on background
            mouseEnabled = true

            val msg = when (state) {
                GameState.WON -> "VICTORY!"
                GameState.NO_MOVES -> "No More Moves!"
                else -> "Game Over!"
            }
            
            val msgColor = if (state == GameState.WON) Colors.GOLD else Colors.RED

            text(text = msg, textSize = 50.0, color = msgColor) {
                centerOn(bg)
                y -= 80
            }

            if (state == GameState.NO_MOVES) {
                // Show Options
                var currentY = -10.0
                
                if (viewModel.undosRemaining.value > 0) {
                     uiButton(label = "Undo", size = Size(width = 200, height = 50)) {
                        centerOn(bg)
                        y = currentY
                        onClick {
                            this@container.removeFromParent()
                            onUndo()
                        }
                    }
                    currentY += 60
                }
                
                if (viewModel.shufflesRemaining.value > 0) {
                     uiButton(label = "Shuffle", size = Size(width = 200, height = 50)) {
                        centerOn(bg)
                        y = currentY
                        onClick {
                            this@container.removeFromParent()
                            onShuffle()
                        }
                    }
                    currentY += 60
                }
                
                uiButton(label = "Quit", size = Size(width = 200, height = 50)) {
                    centerOn(bg)
                    y = currentY + 30 // Extra spacing
                    onClick {
                        onClose()
                    }
                }
            } else {
                // Victory or Defeat (Standard)
                uiButton(label = "Back to Menu", size = Size(width = 200, height = 70)) {
                    textSize = 30.0
                    centerOn(bg)
                    y += 60
                    onClick {
                        onClose()
                    }
                }
            }
        }
    }
}
