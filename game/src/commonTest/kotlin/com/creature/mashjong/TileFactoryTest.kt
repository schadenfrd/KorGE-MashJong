package com.creature.mashjong

import com.creature.mashjong.presentation.infrastructure.TileFactory
import korlibs.image.bitmap.Bitmap32
import kotlin.test.Test
import kotlin.test.assertEquals

class TileFactoryTest {
    @Test
    fun testCreateDeck() {
        val factory = TileFactory()
        // Create a dummy bitmap large enough to not crash slice logic
        // 5 rows * (~60px height) = ~300px
        // 9 cols * (~45px width) = ~400px
        val dummyAtlas = Bitmap32(500, 500)
        
        factory.loadAtlas(dummyAtlas)
        
        val deck = factory.createDeck()
        
        // Bamboo: 9 types * 4 = 36
        // Dots: 9 types * 4 = 36
        // Chars: 9 types * 4 = 36
        // Dragons: 3 types * 4 = 12
        // Winds: 4 types * 4 = 16
        // Seasons: 4 types * 1 = 4
        // Flowers: 4 types * 1 = 4
        // Total = 144
        
        assertEquals(144, deck.size)
    }
}
