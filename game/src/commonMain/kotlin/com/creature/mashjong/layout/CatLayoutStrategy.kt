package com.creature.mashjong.layout

class CatLayoutStrategy : LayoutStrategy {
    // Cat Face shape with ears.
    // Total tiles: 144
    override fun getLayoutSlots(): List<LayoutSlot> {
        val list = mutableListOf<LayoutSlot>()

        // Face Base (Layer 0): 8x8 = 64
        // X: 4..18, Y: 6..20
        for (y in 6..20 step 2) {
            for (x in 4..18 step 2) {
                list.add(LayoutSlot(0, x, y))
            }
        }

        // Face Layer 1: 6x6 = 36
        // X: 6..16, Y: 8..18
        for (y in 8..18 step 2) {
            for (x in 6..16 step 2) {
                list.add(LayoutSlot(1, x, y))
            }
        }

        // Face Layer 2: 4x4 = 16
        // X: 8..14, Y: 10..16
        for (y in 10..16 step 2) {
            for (x in 8..14 step 2) {
                list.add(LayoutSlot(2, x, y))
            }
        }

        // Face Layer 3: 2x2 = 4
        // X: 10..12, Y: 12..14
        for (y in 12..14 step 2) {
            for (x in 10..12 step 2) {
                list.add(LayoutSlot(3, x, y))
            }
        }
        // Subtotal Face: 120

        // Ears (Layer 0, 1, 2)
        // 4 positions per ear * 3 layers = 12 * 2 = 24
        
        // Left Ear: X: 4..6, Y: 2..4
        for (l in 0..2) {
            for (y in 2..4 step 2) {
                for (x in 4..6 step 2) {
                    list.add(LayoutSlot(l, x, y))
                }
            }
        }

        // Right Ear: X: 16..18, Y: 2..4
        for (l in 0..2) {
            for (y in 2..4 step 2) {
                for (x in 16..18 step 2) {
                    list.add(LayoutSlot(l, x, y))
                }
            }
        }
        
        return list
    }
}
