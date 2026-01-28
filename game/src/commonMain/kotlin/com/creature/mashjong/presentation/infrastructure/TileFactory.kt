package com.creature.mashjong.presentation.infrastructure

import com.creature.mashjong.domain.model.Dragon
import com.creature.mashjong.domain.model.Flower
import com.creature.mashjong.domain.model.Season
import com.creature.mashjong.domain.model.StandardSuit
import com.creature.mashjong.domain.model.Suited
import com.creature.mashjong.domain.model.TileDefinition
import com.creature.mashjong.domain.model.TileInfo
import com.creature.mashjong.domain.model.Wind
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

        var idCounter = 0

        // Row 0: Bamboo 1-9
        val bambooSlices = sliceRow(rowIndex = 0, count = 9)
        for (i in 0..8) {
            val def = Suited(suit = StandardSuit.BAMBOO, number = i + 1)

            defineTile(id = idCounter++, definition = def, slice = bambooSlices[i])
        }

        // Row 1: Dots 1-9
        val dotsSlices = sliceRow(rowIndex = 1, count = 9)
        for (i in 0..8) {
            val def = Suited(suit = StandardSuit.DOTS, number = i + 1)

            defineTile(id = idCounter++, definition = def, slice = dotsSlices[i])
        }

        // Row 2: Characters 1-9
        val charSlices = sliceRow(rowIndex = 2, count = 9)
        for (i in 0..8) {
            val def = Suited(suit = StandardSuit.CHARACTERS, number = i + 1)

            defineTile(id = idCounter++, definition = def, slice = charSlices[i])
        }

        // Row 3: Dragons (3) -> Gap 17 -> Winds (4)
        val dragonSlices = sliceRow(rowIndex = 3, count = 3)
        Dragon.entries.forEachIndexed { index, dragon ->
            defineTile(id = idCounter++, definition = dragon, slice = dragonSlices[index])
        }

        // Winds
        val windSlices = sliceRow(rowIndex = 3, count = 4, startOffset = 146)
        Wind.entries.forEachIndexed { index, wind ->
            defineTile(id = idCounter++, definition = wind, slice = windSlices[index])
        }

        // Row 4: Seasons (4) -> Gap 47 -> Flowers (4)
        val seasonSlices = sliceRow(rowIndex = 4, count = 4)
        Season.entries.forEachIndexed { index, season ->
            defineTile(
                id = idCounter++,
                definition = season,
                slice = seasonSlices[index],
                quantity = 1
            )
        }

        // Flowers
        val flowerSlices = sliceRow(rowIndex = 4, count = 4, startOffset = 220)
        Flower.entries.forEachIndexed { index, flower ->
            defineTile(
                id = idCounter++,
                definition = flower,
                slice = flowerSlices[index],
                quantity = 1
            )
        }
    }

    private fun defineTile(
        id: Int,
        definition: TileDefinition,
        slice: BmpSlice,
        quantity: Int = 4
    ) {
        tileSlices[id] = slice
        tileDefinitions[id] = TileInfo(id, definition, quantity)
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

    fun getTileInfo(id: Int): TileInfo? = tileDefinitions[id]
}
