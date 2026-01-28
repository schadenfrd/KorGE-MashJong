package com.creature.mashjong

import com.creature.mashjong.presentation.view.Tile
import korlibs.image.bitmap.Bitmap32
import korlibs.image.bitmap.sliceWithSize
import korlibs.image.color.Colors
import kotlin.test.Test
import kotlin.test.assertEquals

class TileCreationTest {

    @Test
    fun testTileCreation() {
        val dummyBitmap = Bitmap32(10, 10, Colors.RED)
        val dummySlice = dummyBitmap.sliceWithSize(0, 0, 10, 10)
        val tile = Tile(1, 0, dummySlice)

        // Tile should have background (roundRect), face (image), and selectionView
        assertEquals(3, tile.numChildren)
    }
}
