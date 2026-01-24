package com.creature.mashjong

import korlibs.image.bitmap.Bitmap
import korlibs.image.color.Colors
import korlibs.korge.view.Container
import korlibs.korge.view.align.centerOn
import korlibs.korge.view.image
import korlibs.korge.view.roundRect
import korlibs.korge.view.scale
import korlibs.math.geom.RectCorners
import korlibs.math.geom.Size

class Tile(
    val id: Int,
    val face: Bitmap
) : Container() {

    companion object {
        const val WIDTH = 150.0
        const val HEIGHT = 170.0

        const val PADDING = 40.0

        const val FACE_WIDTH = WIDTH - PADDING
        const val FACE_HEIGHT = HEIGHT - PADDING
    }

    init {
        // 1. Draw the tile background (White rounded rectangle)
        roundRect(
            size = Size(width = WIDTH, height = HEIGHT),
            radius = RectCorners(corner = 8.0), // Rounded corners
            fill = Colors.WHITESMOKE,
            stroke = Colors.BLACK,
            strokeThickness = 2.0
        )

        // 2. Draw the face image centered on the tile
        image(texture = face).apply {
            // Optional: Scale image if it's too large
            if (width > WIDTH) scale(WIDTH / width)

            centerOn(other = this@Tile)
        }

        // Optional: Display ID for debugging
        // text("$id", color = Colors.BLACK).alignTopLeft(this@Tile)
    }
}
