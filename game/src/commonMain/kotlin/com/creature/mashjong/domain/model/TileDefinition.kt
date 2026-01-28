package com.creature.mashjong.domain.model

// Root of the hierarchy
sealed interface TileDefinition {
    val displayName: String
}

// 1. Standard Suited Tiles (Bamboo, Dots, Characters)
enum class StandardSuit(val displayName: String) {
    BAMBOO(displayName = "Bamboo"),
    DOTS(displayName = "Dots"),
    CHARACTERS(displayName = "Characters");
}

sealed interface StandardTile : TileDefinition {
    val suit: StandardSuit
    val number: Int
}

data class Suited(override val suit: StandardSuit, override val number: Int) : StandardTile {
    init {
        require(number in 1..9) { "Standard tile number must be 1-9, was $number" }
    }

    override val displayName: String get() = "$number of ${suit.displayName}"
}

// 2. Honor Tiles (Winds & Dragons)
sealed interface HonorTile : TileDefinition

enum class Wind(val direction: String) : HonorTile {
    EAST(direction = "East"),
    SOUTH(direction = "South"),
    WEST(direction = "West"),
    NORTH(direction = "North");

    override val displayName: String = direction
}

enum class Dragon(val color: String) : HonorTile {
    RED(color = "Red"),
    GREEN(color = "Green"),
    WHITE(color = "White");

    override val displayName: String = "$color Dragon"
}

// 3. Bonus Tiles (Flowers & Seasons)
sealed interface BonusTile : TileDefinition

enum class Flower(val type: String) : BonusTile {
    PLUM(type = "Plum"),
    ORCHID(type = "Orchid"),
    CHRYSANTHEMUM(type = "Chrysanthemum"),
    BAMBOO(type = "Bamboo");

    override val displayName: String = type
}

enum class Season(val type: String) : BonusTile {
    SPRING(type = "Spring"),
    SUMMER(type = "Summer"),
    AUTUMN(type = "Autumn"),
    WINTER(type = "Winter");

    override val displayName: String = type
}
