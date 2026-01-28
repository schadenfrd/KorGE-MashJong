package com.creature.mashjong.layout

class DragonLayoutStrategy : LayoutStrategy {
    // A "Step Pyramid" or Ziggurat shape.
    // Total tiles: 144
    override fun getLayoutSlots(): List<LayoutSlot> {
        val list = mutableListOf<LayoutSlot>()

        // Layer 0: 8x8 = 64
        // X: 2..16, Y: 4..18
        for (y in 4..18 step 2) {
            for (x in 2..16 step 2) {
                list.add(LayoutSlot(0, x, y))
            }
        }

        // Layer 1: 6x8 = 48
        // X: 4..14, Y: 4..18
        for (y in 4..18 step 2) {
            for (x in 4..14 step 2) {
                list.add(LayoutSlot(1, x, y))
            }
        }

        // Layer 2: 4x6 = 24
        // X: 6..12, Y: 6..16
        for (y in 6..16 step 2) {
            for (x in 6..12 step 2) {
                list.add(LayoutSlot(2, x, y))
            }
        }

        // Layer 3: 2x4 = 8
        // X: 8..10, Y: 8..14
        for (y in 8..14 step 2) {
            for (x in 8..10 step 2) {
                list.add(LayoutSlot(3, x, y))
            }
        }

        return list
    }
}
