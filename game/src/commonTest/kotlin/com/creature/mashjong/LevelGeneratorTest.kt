package com.creature.mashjong

import korlibs.image.bitmap.Bitmap32
import korlibs.image.bitmap.slice
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class LevelGeneratorTest {
    @Test
    fun testTurtleLayoutGeneration() {
        // Prepare a dummy deck of 144 tiles
        val dummyBitmap = Bitmap32(1, 1)
        val dummySlice = dummyBitmap.slice()
        val dummyInfo = TileInfo(0, TileSuit.DOTS, 1, "test")
        val deck = List(144) { dummyInfo to dummySlice }

        // Generate Layout
        val positions = LevelGenerator.generateTurtleLayout(deck)

        // Verify count
        assertEquals(144, positions.size, "Should generate exactly 144 tile positions")

        // Verify layers
        val layerCounts = positions.groupingBy { it.layer }.eachCount()
        assertEquals(1, layerCounts[4], "Layer 4 should have 1 tile")
        assertEquals(4, layerCounts[3], "Layer 3 should have 4 tiles")
        assertEquals(16, layerCounts[2], "Layer 2 should have 16 tiles")
        assertEquals(36, layerCounts[1], "Layer 1 should have 36 tiles")
        assertEquals(87, layerCounts[0], "Layer 0 should have 87 tiles")
        
        // Verify bounds (Just sanity check)
        assertTrue(positions.all { it.x >= 0 }, "All x coordinates should be non-negative")
        assertTrue(positions.all { it.y >= 0 }, "All y coordinates should be non-negative")
    }
}
