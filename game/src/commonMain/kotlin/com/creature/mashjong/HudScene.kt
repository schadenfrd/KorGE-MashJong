package com.creature.mashjong

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
    val onClose: () -> Unit
) : Container() {

    fun initialize() {
        val views = stage?.views ?: return
        val screenWidth = views.virtualWidthDouble
        val screenHeight = views.virtualHeightDouble

        // Transparent background for sizing/alignment
        solidRect(screenWidth, screenHeight, Colors.TRANSPARENT)

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
        uiButton(label = "Undo", size = Size(btnWidth, btnHeight)) {
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
    }

    fun showGameOver(victory: Boolean) {
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
