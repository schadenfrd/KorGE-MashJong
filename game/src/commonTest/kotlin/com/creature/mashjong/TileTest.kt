package com.creature.mashjong

import korlibs.image.bitmap.Bitmap32
import korlibs.image.bitmap.sliceWithSize
import korlibs.image.color.Colors
import kotlin.test.Test
import kotlin.test.assertEquals

class TileTest {
    @Test
    fun testTileCreation() {
        val dummyBitmap = Bitmap32(10, 10, Colors.RED)
        val dummySlice = dummyBitmap.sliceWithSize(0, 0, 10, 10)
        val tile = Tile(1, dummySlice)
        
        // Tile should have background (roundRect) and face (image)
        assertEquals(2, tile.numChildren)
    }
}
