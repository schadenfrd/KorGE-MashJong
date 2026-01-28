package com.creature.mashjong.domain.logic

import com.creature.mashjong.domain.model.Flower
import com.creature.mashjong.domain.model.Season
import com.creature.mashjong.domain.model.TileDefinition

interface RuleSet {
    fun isMatch(t1: TileDefinition, t2: TileDefinition): Boolean
}

object SolitaireMatchRule : RuleSet {
    override fun isMatch(t1: TileDefinition, t2: TileDefinition): Boolean {
        // Standard rule: Exact Semantic Equality
        if (t1 == t2) return true

        // Special Solitaire Rules:
        // Any Season matches any Season
        if (t1 is Season && t2 is Season) return true

        // Any Flower matches any Flower
        if (t1 is Flower && t2 is Flower) return true

        return false
    }
}
