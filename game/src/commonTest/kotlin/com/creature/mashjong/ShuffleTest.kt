package com.creature.mashjong

import com.creature.mashjong.domain.logic.MahjongGame
import com.creature.mashjong.domain.model.TilePosition
import kotlin.test.Test
import kotlin.test.assertEquals

class ShuffleTest {

    @Test
    fun testShuffle() {
        // Setup a small board
        val t1 = TilePosition(tileId = 1, layer = 0, x = 0, y = 0)
        val t2 = TilePosition(tileId = 2, layer = 0, x = 2, y = 0)
        val t3 = TilePosition(tileId = 3, layer = 0, x = 4, y = 0)

        val initialTiles = listOf(t1, t2, t3)

        val game = MahjongGame(
            initialTiles = initialTiles,
            tileInfoProvider = { null } // Not needed for shuffle
        )

        // Shuffle
        game.shuffle()

        val active = game.getActiveTiles()

        // Should have same number of tiles
        assertEquals(3, active.size)

        // Positions (x,y,layer) should match the initial set (regardless of order, but since we iterate in order, let's check set of coords)
        val initialCoords = initialTiles.map { Triple(it.x, it.y, it.layer) }.toSet()
        val newCoords = active.map { Triple(it.x, it.y, it.layer) }.toSet()

        assertEquals(initialCoords, newCoords)

        // IDs should be the same set (just potentially moved)
        val initialIds = initialTiles.map { it.tileId }.toSet()
        val newIds = active.map { it.tileId }.toSet()

        assertEquals(initialIds, newIds)
    }
}
