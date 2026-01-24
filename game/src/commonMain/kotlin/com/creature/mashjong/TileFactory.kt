package com.creature.mashjong

import korlibs.image.bitmap.Bitmap
import korlibs.image.bitmap.Bitmap32
import korlibs.image.color.Colors
import korlibs.image.format.readBitmap
import korlibs.io.file.std.resourcesVfs

class TileFactory {
    private val textureCache = mutableMapOf<Int, Bitmap>()

    // Call this before starting the game level
    suspend fun loadAssets() {
        // In a real app, you might iterate a range or load a sprite sheet
        // For now, let's just cache our single test image for ID 1
        if (!textureCache.containsKey(1)) {
            try {
                textureCache[1] = resourcesVfs["tile_face.png"].readBitmap()
            } catch (e: Exception) {
                println("Warning: Could not load tile_face.png: $e")
            }
        }
    }

    fun createTile(id: Int): Tile {
        val bitmap = textureCache[id] ?: Bitmap32(
            width = Tile.FACE_WIDTH.toInt(),
            height = Tile.FACE_HEIGHT.toInt(),
            value = Colors.CYAN
        )

        return Tile(id = id, face = bitmap)
    }
}
