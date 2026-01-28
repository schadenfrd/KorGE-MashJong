package com.creature.mashjong

import com.creature.mashjong.data.LevelGenerator
import com.creature.mashjong.domain.logic.MahjongGame
import com.creature.mashjong.domain.model.*
import com.creature.mashjong.layout.TurtleLayoutStrategy
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class MahjongGameTest {

    private val testProvider: (Int) -> TileInfo? = { id ->
        when (id) {
            0 -> TileInfo(0, Suited(StandardSuit.BAMBOO, 1))
            1 -> TileInfo(1, Suited(StandardSuit.BAMBOO, 1)) // Match for 0
            2 -> TileInfo(2, Suited(StandardSuit.BAMBOO, 2)) // Mismatch value
            3 -> TileInfo(3, Suited(StandardSuit.DOTS, 1))   // Mismatch suit
            
            100 -> TileInfo(100, Flower.PLUM)
            101 -> TileInfo(101, Flower.ORCHID) // Match for 100
            
            200 -> TileInfo(200, Season.SPRING)
            201 -> TileInfo(201, Season.AUTUMN) // Match for 200
            
            else -> null
        }
    }

    @Test
    fun testFreedom_TopCover() {
        // Base tile at (0, 0, 0)
        // Cover tile at (1, 0, 0) -> Overlap
        val t1 = TilePosition(0, 0, 0, 0)
        val t2 = TilePosition(1, 0, 0, 1)
        val game = MahjongGame(listOf(t1, t2), testProvider)
        
        assertFalse(game.isTileFree(t1), "Tile covered by layer above should be blocked")
        assertTrue(game.isTileFree(t2), "Top tile should be free")
    }
    
    @Test
    fun testFreedom_Sides() {
        // T1 (0, 2, 0)
        // Left (0, 0, 0) - T2
        // Right (0, 4, 0) - T3
        val t1 = TilePosition(0, 2, 0, 0)
        val left = TilePosition(0, 0, 0, 1)
        val right = TilePosition(0, 4, 0, 2)
        
        // Case 1: Blocked by both
        val gameFull = MahjongGame(listOf(t1, left, right), testProvider)
        assertFalse(gameFull.isTileFree(t1), "Blocked by both sides")
        
        // Case 2: Left only
        val gameLeft = MahjongGame(listOf(t1, left), testProvider)
        assertTrue(gameLeft.isTileFree(t1), "Free if only left blocked")
        
        // Case 3: Right only
        val gameRight = MahjongGame(listOf(t1, right), testProvider)
        assertTrue(gameRight.isTileFree(t1), "Free if only right blocked")
    }

    @Test
    fun testMatching() {
        val t1 = TilePosition(0,0,0, 0) // Bamboo 1
        val t2 = TilePosition(0,2,0, 1) // Bamboo 1
        val t3 = TilePosition(0,4,0, 2) // Bamboo 2
        val game = MahjongGame(listOf(t1, t2, t3), testProvider)
        
        assertTrue(game.isMatch(t1, t2))
        assertFalse(game.isMatch(t1, t3))
    }
    
    @Test
    fun testFlowersAndSeasons() {
        val f1 = TilePosition(0,0,0, 100)
        val f2 = TilePosition(0,2,0, 101)
        val s1 = TilePosition(0,4,0, 200)
        val s2 = TilePosition(0,6,0, 201)
        
        val game = MahjongGame(listOf(f1, f2, s1, s2), testProvider)
        
        assertTrue(game.isMatch(f1, f2), "Flowers should match")
        assertTrue(game.isMatch(s1, s2), "Seasons should match")
        assertFalse(game.isMatch(f1, s1), "Flower and Season should not match")
    }
    
    @Test
    fun testInteractionFlow() {
        // Arrange tiles vertically so they don't block each other horizontally
        val t1 = TilePosition(0,0,0, 0)
        val t2 = TilePosition(0,0,4, 1) // Match
        val t3 = TilePosition(0,0,8, 2) // No match
        
        val game = MahjongGame(listOf(t1, t2, t3), testProvider)
        
        // 1. Select first
        val r1 = game.onTileClick(t1)
        assertTrue(r1 is MatchResult.Selected)
        assertEquals(t1, game.selectedTile)
        
        // 2. Click same (Deselect)
        val r2 = game.onTileClick(t1)
        assertTrue(r2 is MatchResult.Deselected)
        assertEquals(null, game.selectedTile)
        
        // 3. Select first again
        game.onTileClick(t1)
        
        // 4. Click mismatch (t3)
        val r3 = game.onTileClick(t3)
        assertTrue(r3 is MatchResult.Selected)
        assertEquals(t3, game.selectedTile) // Selection moves to t3
        
        // 5. Click match (t1 matches t2, but currently t3 selected)
        // Need to select t1 or t2.
        game.onTileClick(t1) // Select t1
        val r4 = game.onTileClick(t2) // Select t2 (match t1)
        
        assertTrue(r4 is MatchResult.Match)
        assertEquals(null, game.selectedTile)
        assertEquals(1, game.getActiveTiles().size) // Only t3 remains
    }

    @Test
    fun testGeneratedLevel_Blockage() {
        // Create a fake deck
        val deck = List(144) { i ->
            TileInfo(i, Suited(StandardSuit.BAMBOO, 1))
        }
        
        val level = LevelGenerator.generateLayout(deck, TurtleLayoutStrategy())
        val game = MahjongGame(level, testProvider)
        
        // Find a tile on Layer 1 that is in the middle of the block
        // Based on analysis: L1 has x=1,3,5,7,9,11 and y=9,11,13,15,17,19
        // Pick x=5, y=9.
        // It should have neighbors at x=3 and x=7.
        // It is at y=9, so not covered by L2 (starts at y=11).
        
        val target = level.find { it.layer == 1 && it.x == 5 && it.y == 9 }
        assertNotNull(target, "Target tile (1, 5, 9) not found in generated level")
        
        val left = level.find { it.layer == 1 && it.x == 3 && it.y == 9 }
        assertNotNull(left, "Left neighbor (1, 3, 9) not found")
        
        val right = level.find { it.layer == 1 && it.x == 7 && it.y == 9 }
        assertNotNull(right, "Right neighbor (1, 7, 9) not found")
        
        // Check freedom
        val isFree = game.isTileFree(target)
        assertFalse(isFree, "Tile at (1, 5, 9) should be blocked by neighbors at 3 and 7")
    }

    @Test
    fun testAdvancedFeatures() {
        // Arrange vertically so they are all free (no horizontal blocking)
        val t1 = TilePosition(0,0,0, 0)
        val t2 = TilePosition(0,0,4, 1) // Match
        val t3 = TilePosition(0,0,8, 2) // No match for t1/t2
        val t4 = TilePosition(0,0,12, 3) // No match

        val game = MahjongGame(listOf(t1, t2, t3, t4), testProvider)

        // 1. Check Hints
        assertEquals(3, game.hintsRemaining)
        val hint = game.getHint()
        assertNotNull(hint)
        assertTrue( (hint.first == t1 && hint.second == t2) || (hint.first == t2 && hint.second == t1) )
        assertEquals(2, game.hintsRemaining)

        // 2. Check Undo
        // Perform match
        game.onTileClick(t1)
        game.onTileClick(t2)
        assertEquals(2, game.getActiveTiles().size)
        
        // Undo
        assertEquals(3, game.undosRemaining)
        assertTrue(game.undo().isNotEmpty())
        assertEquals(4, game.getActiveTiles().size)
        assertEquals(2, game.undosRemaining)
        
        // 3. Check No Moves
        val gameNoMoves = MahjongGame(listOf(t3, t4), testProvider)
        assertFalse(gameNoMoves.hasValidMoves())
        
        val gameWithMoves = MahjongGame(listOf(t1, t2), testProvider)
        assertTrue(gameWithMoves.hasValidMoves())
    }

    @Test
    fun testCustomSettings() {
        val game = MahjongGame(emptyList(), testProvider, maxUndos = 10, maxHints = 5)
        assertEquals(10, game.undosRemaining)
        assertEquals(5, game.hintsRemaining)
    }
}
