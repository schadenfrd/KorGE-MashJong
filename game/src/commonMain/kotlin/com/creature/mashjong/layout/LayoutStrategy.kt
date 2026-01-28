package com.creature.mashjong.layout

interface LayoutStrategy {
    /**
     * Returns a list of available slots (Layer, X, Y) for this layout.
     * Does not assign Tile IDs.
     */
    fun getLayoutSlots(): List<LayoutSlot>

    /**
     * Checks if a specific tile is blocked by other active tiles.
     */
}

data class LayoutSlot(val layer: Int, val x: Int, val y: Int)
