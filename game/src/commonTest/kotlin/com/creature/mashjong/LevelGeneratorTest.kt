package com.creature.mashjong

import com.creature.mashjong.data.LevelGenerator
import com.creature.mashjong.domain.model.StandardSuit
import com.creature.mashjong.domain.model.Suited
import com.creature.mashjong.domain.model.TileInfo
import com.creature.mashjong.layout.CatLayoutStrategy
import com.creature.mashjong.layout.DragonLayoutStrategy
import com.creature.mashjong.layout.LayoutRegistry
import com.creature.mashjong.layout.TurtleLayoutStrategy
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class LevelGeneratorTest {
    @Test
    fun testTurtleLayoutGeneration() {
        // Prepare a dummy deck of 144 tiles
        val dummyInfo = TileInfo(0, Suited(StandardSuit.DOTS, 1))
        val deck = List(144) { dummyInfo }

        // Generate Layout
        val positions = LevelGenerator.generateLayout(deck, TurtleLayoutStrategy())

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

    @Test
    fun testNewLayoutsFitting() {
        val dummyInfo = TileInfo(0, Suited(StandardSuit.DOTS, 1))
        val deck = List(144) { dummyInfo }
        
        for ((name, strategy) in LayoutRegistry.availableLayouts) {
            val positions = LevelGenerator.generateLayout(deck, strategy)
            assertEquals(144, positions.size, "$name should have 144 tiles")
            
            val minX = positions.minOf { it.x }
            val maxX = positions.maxOf { it.x }
            val minY = positions.minOf { it.y }
            val maxY = positions.maxOf { it.y }

            val width = maxX - minX
            val height = maxY - minY
            
            assertTrue(width <= 24, "$name width too large: $width")
            assertTrue(height <= 36, "$name height too large: $height")
            
            assertTrue(minX >= 0, "$name has negative X")
            assertTrue(minY >= 0, "$name has negative Y")
        }
    }
}
