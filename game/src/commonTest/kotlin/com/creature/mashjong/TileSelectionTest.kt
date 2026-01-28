package com.creature.mashjong

import com.creature.mashjong.domain.logic.MahjongGame
import com.creature.mashjong.domain.model.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class TileSelectionTest {

    private val testProvider: (Int) -> TileInfo? = { id ->
        TileInfo(id, Suited(StandardSuit.BAMBOO, 1))
    }

    @Test
    fun testSelectionLagScenario() {
        // Setup: Two tiles, side by side, not blocking each other.
        // T1 at (0, 0, 0)
        // T2 at (0, 4, 0) - distance > 2, so safe.
        val t1 = TilePosition(0, 0, 0, 1)
        val t2 = TilePosition(0, 4, 0, 2)

        val game = MahjongGame(listOf(t1, t2), testProvider)

        // Initial State
        assertEquals(null, game.selectedTile, "Initial selection should be null")

        // Action 1: Click T1
        val result1 = game.onTileClick(t1)

        // Assert 1
        assertIs<MatchResult.Selected>(result1, "First click should return Selected")
        assertEquals(t1, (result1).tile, "Should select T1")
        assertEquals(t1, game.selectedTile, "Game state should reflect selection of T1")

        // Action 2: Click T2 (Match)
        val result2 = game.onTileClick(t2)

        // Assert 2
        assertIs<MatchResult.Match>(result2, "Second click should return Match")
        assertEquals(t1, result2.tileA)
        assertEquals(t2, result2.tileB)
        assertEquals(null, game.selectedTile, "Game state should be cleared after match")
    }

    @Test
    fun testSelectionSwitching() {
        // T1, T2, T3 (Mismatch)
        val t1 = TilePosition(0, 0, 0, 1) // Bamboo 1
        val t2 = TilePosition(0, 4, 0, 2) // Bamboo 1
        val t3 = TilePosition(
            0,
            8,
            0,
            3
        ) // Bamboo 1 (Same type for provider, but let's assume we treat them as distinct for selection test)

        val game = MahjongGame(listOf(t1, t2, t3), testProvider)

        // 1. Click T1
        game.onTileClick(t1)
        assertEquals(t1, game.selectedTile)

        // 2. Click T3 (Assume NO match logic override for this test, wait, provider returns same info)
        // If they match, it will match. I need different info to force switch.
        // Actually, if they match, it matches.

        // Let's make T3 different.
        val providerWithMismatch: (Int) -> TileInfo? = { id ->
            if (id == 3) TileInfo(id, Suited(StandardSuit.DOTS, 1)) else TileInfo(id, Suited(StandardSuit.BAMBOO, 1))
        }
        val gameMismatch = MahjongGame(listOf(t1, t2, t3), providerWithMismatch)

        // 1. Click T1
        val r1 = gameMismatch.onTileClick(t1)
        assertIs<MatchResult.Selected>(r1)
        assertEquals(t1, gameMismatch.selectedTile)

        // 2. Click T3 (Mismatch)
        val r2 = gameMismatch.onTileClick(t3)
        // Should select T3 now
        assertIs<MatchResult.Selected>(r2)
        assertEquals(t3, r2.tile)
        assertEquals(t3, gameMismatch.selectedTile)

        // 3. Click T2 (Mismatch with T3)
        val r3 = gameMismatch.onTileClick(t2)
        assertIs<MatchResult.Selected>(r3)
        assertEquals(t2, gameMismatch.selectedTile)
    }

    @Test
    fun testBlockedTileClick() {
        // T1 (0, 0, 0)
        // T2 (1, 0, 0) covering T1
        val t1 = TilePosition(0, 0, 0, 1)
        val t2 = TilePosition(1, 0, 0, 2)

        val game = MahjongGame(listOf(t1, t2), testProvider)

        // Click T1 (Blocked)
        val result = game.onTileClick(t1)

        assertIs<MatchResult.Blocked>(result, "Clicking blocked tile should return Blocked")
        assertEquals(null, game.selectedTile, "Should not select blocked tile")
    }

    @Test
    fun testDoubleClickDeselection() {
        val t1 = TilePosition(0, 0, 0, 1)
        val game = MahjongGame(listOf(t1), testProvider)

        // Click 1: Select
        val r1 = game.onTileClick(t1)
        assertIs<MatchResult.Selected>(r1)
        assertEquals(t1, game.selectedTile)

        // Click 2 (Bounce): Deselect
        val r2 = game.onTileClick(t1)
        assertIs<MatchResult.Deselected>(r2)
        assertEquals(null, game.selectedTile)
    }
}
