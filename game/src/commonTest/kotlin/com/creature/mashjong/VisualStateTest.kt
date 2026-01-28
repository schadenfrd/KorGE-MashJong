package com.creature.mashjong

import com.creature.mashjong.domain.model.TilePosition
import com.creature.mashjong.presentation.infrastructure.TileFactory
import com.creature.mashjong.presentation.view.BoardView
import com.creature.mashjong.presentation.view.Tile
import korlibs.image.bitmap.Bitmap32
import korlibs.image.bitmap.sliceWithSize
import korlibs.image.color.Colors
import korlibs.korge.view.Image
import kotlin.test.Test
import kotlin.test.assertEquals

class VisualStateTest {

    @Test
    fun testTileBlockedVisuals() {
        val dummyBitmap = Bitmap32(10, 10, Colors.RED)
        val dummySlice = dummyBitmap.sliceWithSize(0, 0, 10, 10)
        val tile = Tile(1, 0, dummySlice)

        // Identify the face view (index 1)
        val faceView = tile.getChildAt(1) as Image

        // Initial state
        assertEquals(Colors.WHITE, faceView.colorMul, "Initial color should be WHITE")

        // Blocked
        tile.setBlocked(true)
        assertEquals(Colors.DARKGRAY, faceView.colorMul, "Blocked color should be DARKGRAY")

        // Unblocked
        tile.setBlocked(false)
        assertEquals(Colors.WHITE, faceView.colorMul, "Unblocked color should be WHITE")
    }

    @Test
    fun testBoardViewRefreshVisuals() {
        // Mock factory
        val factory = TileFactory()
        val dummyAtlas = Bitmap32(100, 100)
        factory.loadAtlas(dummyAtlas)

        var blockedState = false
        val boardView = BoardView(factory, { }, { blockedState })

        val pos = TilePosition(0, 0, 0, 0)
        boardView.addTiles(listOf(pos))

        // Get the tile
        // Access private tileViews? No, can't.
        // But we can check children of boardView.
        // BoardView adds tiles as children.
        assertEquals(1, boardView.numChildren)
        val tile = boardView.getChildAt(0) as Tile
        val faceView = tile.getChildAt(1) as Image

        // Refresh with blocked = true
        blockedState = true
        boardView.refreshVisuals()
        assertEquals(Colors.DARKGRAY, faceView.colorMul)

        // Refresh with blocked = false
        blockedState = false
        boardView.refreshVisuals()
        assertEquals(Colors.WHITE, faceView.colorMul)
    }
}
