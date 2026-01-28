package com.creature.mashjong.presentation.infrastructure

import com.creature.mashjong.domain.model.TileInfo
import com.creature.mashjong.domain.model.TileSuit
import com.creature.mashjong.presentation.view.Tile
import korlibs.image.bitmap.Bitmap
import korlibs.image.bitmap.BmpSlice
import korlibs.image.bitmap.sliceWithSize

class TileFactory {
    private val tileSlices = mutableMapOf<Int, BmpSlice>()
    private val tileDefinitions = mutableMapOf<Int, TileInfo>()

    /**
     * Load the atlas with classic Mahjong layout
     *
     * tileSize = 41x53px
     * horizontalSpacing = 3px
     * verticalSpacing = 8px
     * dragonsXWindsSpacing = 17px
     * seasonsXFlowersSpacing = 47px
     */
    fun loadAtlas(
        atlas: Bitmap,
        startX: Int = 0,
        startY: Int = 0,
        tileWidth: Int = 41,
        tileHeight: Int = 53,
        gapX: Int = 3,
        gapY: Int = 8
    ) {
        val bamboosRowY = startY
        val dotsRowY = bamboosRowY + tileHeight + gapY
        val charactersRowY = dotsRowY + tileHeight + gapY
        val dragonWindRowY = charactersRowY + tileHeight + gapY
        val seasonFlowerRowY = dragonWindRowY + tileHeight + gapY

        val rowY = listOf(bamboosRowY, dotsRowY, charactersRowY, dragonWindRowY, seasonFlowerRowY)

        tileSlices.clear()
        tileDefinitions.clear()

        // Helper to slice a row
        fun sliceRow(rowIndex: Int, count: Int, startOffset: Int = 0): List<BmpSlice> {
            val y = rowY.getOrElse(rowIndex) { 0 }
            val list = mutableListOf<BmpSlice>()

            for (i in 0 until count) {
                // x = BaseStart(1) + GroupOffset + TileIndex * (Width + Gap)
                val x = startX + startOffset + i * (tileWidth + gapX)
                list.add(atlas.sliceWithSize(x, y, tileWidth, tileHeight))
            }

            return list
        }

        // Row 0: Bamboo 1-9 (IDs 0-8)
        val bambooSlices = sliceRow(0, 9)
        for (i in 0..8) {
            val id = i
            defineTile(id, TileSuit.BAMBOO, i + 1, "${i + 1} of Bamboo", bambooSlices[i])
        }

        // Row 1: Dots 1-9 (IDs 9-17)
        val dotsSlices = sliceRow(1, 9)
        for (i in 0..8) {
            val id = i + 9
            defineTile(id, TileSuit.DOTS, i + 1, "${i + 1} of Dots", dotsSlices[i])
        }

        // Row 2: Characters 1-9 (IDs 18-26)
        val charSlices = sliceRow(2, 9)
        for (i in 0..8) {
            val id = i + 18
            defineTile(id, TileSuit.CHARACTERS, i + 1, "${i + 1} of Characters", charSlices[i])
        }

        // Row 3: Dragons (3) -> Gap 17 -> Winds (4)
        // Dragons (IDs 31-33): Red, Green, White
        val dragonSlices = sliceRow(3, 3, startOffset = 0)
        val dragons = listOf("Red Dragon", "Green Dragon", "White Dragon")
        for (i in dragons.indices) {
            defineTile(31 + i, TileSuit.HONORS, i + 4, dragons[i], dragonSlices[i])
        }

        // Winds (IDs 27-30): East, South, West, North
        // Offset: Dragons Width (3*41 + 2*3 = 129) + Gap 17 = 146
        val windSlices = sliceRow(3, 4, startOffset = 146)
        val winds = listOf("East", "South", "West", "North")
        for (i in winds.indices) {
            defineTile(27 + i, TileSuit.HONORS, i, winds[i], windSlices[i])
        }

        // Row 4: Seasons (4) -> Gap 47 -> Flowers (4)
        // Seasons (IDs 38-41): Spring, Summer, Autumn, Winter
        val seasonSlices = sliceRow(4, 4, startOffset = 0)
        val seasons = listOf("Spring", "Summer", "Autumn", "Winter")
        for (i in seasons.indices) {
            defineTile(38 + i, TileSuit.SEASONS, i + 1, seasons[i], seasonSlices[i], quantity = 1)
        }

        // Flowers (IDs 34-37): Plum, Orchid, Chrysanthemum, Bamboo
        // Offset: Seasons Width (4*41 + 3*3 = 173) + Gap 47 = 220
        val flowerSlices = sliceRow(4, 4, startOffset = 220)
        val flowers = listOf("Plum", "Orchid", "Chrysanthemum", "Bamboo")
        for (i in flowers.indices) {
            defineTile(34 + i, TileSuit.FLOWERS, i + 1, flowers[i], flowerSlices[i], quantity = 1)
        }
    }

    private fun defineTile(
        id: Int,
        suit: TileSuit,
        value: Int,
        name: String,
        slice: BmpSlice,
        quantity: Int = 4
    ) {
        tileSlices[id] = slice
        tileDefinitions[id] = TileInfo(id, suit, value, name, quantity)
    }

    // Get a visual tile for a specific ID
    fun createTile(id: Int): Tile {
        val slice = tileSlices[id]
            ?: throw IllegalStateException("Tile ID $id not found or atlas not loaded")

        return Tile(id, slice)
    }

    // Generate a full game deck (only logical info needed)
    fun createDeck(): List<TileInfo> {
        val fullSet = mutableListOf<TileInfo>()

        for (tileInfo in tileDefinitions.values) {
            repeat(tileInfo.quantity) {
                fullSet.add(tileInfo)
            }
        }

        return fullSet.shuffled()
    }

    fun getTileInfo(id: Int): TileInfo? {
        return tileDefinitions[id]
    }
}
