package com.creature.mashjong

import korlibs.image.bitmap.BmpSlice
import korlibs.korge.view.Container
import korlibs.korge.view.align.centerOn
import korlibs.korge.view.image
import korlibs.korge.view.scale

class Tile(
    val id: Int,
    face: BmpSlice
) : Container() {

    companion object {
        const val WIDTH = 140.0
        const val HEIGHT = 180.0

        const val PADDING = 0

        const val FACE_WIDTH = WIDTH - PADDING
        const val FACE_HEIGHT = HEIGHT - PADDING
    }

    init {
//        // 1. Draw the physical tile body
//        roundRect(
//            size = Size(WIDTH, HEIGHT),
//            radius = RectCorners(8.0),
//            fill = Colors.WHITESMOKE,
//            stroke = Colors.BLACK,
//            strokeThickness = 1.5
//        )

        // 2. Draw the face image centered on the tile
        image(texture = face).apply {
            // Scale image to fit FACE dimensions (scale up or down)
            val scaleX = FACE_WIDTH / width
            val scaleY = FACE_HEIGHT / height
            val s = if (scaleX < scaleY) scaleX else scaleY
            scale(s)

            centerOn(other = this@Tile)
        }

        // Optional: Display ID for debugging
        // text("$id", color = Colors.BLACK).alignTopLeft(this@Tile)
    }
}
