package com.creature.mashjong.layout

object LayoutRegistry {
    enum class Layout {
        Turtle, Dragon, Cat
    }

    val availableLayouts: Map<Layout, LayoutStrategy> = mapOf(
        Layout.Turtle to TurtleLayoutStrategy(),
        Layout.Dragon to DragonLayoutStrategy(),
        Layout.Cat to CatLayoutStrategy()
    )

    fun getStrategy(name: String): LayoutStrategy =
        availableLayouts[name.toLayout()] ?: TurtleLayoutStrategy()

    fun getStrategy(layout: Layout): LayoutStrategy =
        availableLayouts[layout] ?: TurtleLayoutStrategy()

    private fun String.toLayout() =
        Layout.entries.firstOrNull { it.name.equals(this, ignoreCase = true) }
}
