package com.creature.mashjong.presentation.view

import korlibs.image.bitmap.BmpSlice
import korlibs.image.color.Colors
import korlibs.korge.view.Container
import korlibs.korge.view.Image
import korlibs.korge.view.RoundRect
import korlibs.korge.view.align.centerOn
import korlibs.korge.view.image
import korlibs.korge.view.roundRect
import korlibs.korge.view.scale
import korlibs.korge.view.xy
import korlibs.math.geom.RectCorners
import korlibs.math.geom.Size

class Tile(
    val id: Int,
    val layer: Int,
    face: BmpSlice
) : Container() {

    companion object {
        const val WIDTH = 140.0
        const val HEIGHT = 180.0
        const val PADDING = 0
        const val FACE_WIDTH = WIDTH - PADDING
        const val FACE_HEIGHT = HEIGHT - PADDING
    }

    private val selectionView: RoundRect
    private val faceView: Image

    init {
        // 1. Drop Shadow (for depth)
        roundRect(
            size = Size(WIDTH, HEIGHT),
            radius = RectCorners(corner = 8.0),
            fill = Colors.BLACK.withAd(v = 0.7),
        ).xy(x = 4.0 + layer * 2.0, y = 4.0 + layer * 2.0)

        // 2. Draw the face image centered on the tile
        faceView = image(texture = face).apply {
            // Scale image to fit FACE dimensions (scale up or down)
            val scaleX = FACE_WIDTH / width
            val scaleY = FACE_HEIGHT / height
            val s = if (scaleX < scaleY) scaleX else scaleY
            scale(sx = s)

            centerOn(other = this@Tile)
        }

        // 3. Selection Border (On Top)
        selectionView = roundRect(
            size = Size(WIDTH, HEIGHT),
            radius = RectCorners(corner = 8.0),
            fill = Colors.TRANSPARENT,
            stroke = Colors.HOTPINK,
            strokeThickness = 8.0
        ).also { it.visible = false }
    }

    fun setSelect(selected: Boolean) {
        selectionView.visible = selected
    }

    fun setBlocked(blocked: Boolean) {
        faceView.colorMul = if (blocked) Colors.DARKGRAY else Colors.WHITE
    }
}
