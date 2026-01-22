import korlibs.korge.gradle.GameCategory

plugins {
    alias(libs.plugins.korge)
}

val ID = "com.creature.mashjong"
val NAME = "Mish Mash-Jong"
val DESCRIPTION = "Mahjong Game"
val ENTRY_POINT = "$ID.main"
val ICON_PATH = "icon/mish-mash-jong.png"

korge {
    id = ID
    name = NAME
    description = DESCRIPTION
    gameCategory = GameCategory.PUZZLE
    entryPoint = ENTRY_POINT

    exeBaseName = NAME
    icon = File(projectDir, ICON_PATH)
    banner = icon

    // Configure as library for Android to avoid conflict with composeApp application
    androidLibrary = true

    targetDefault()
    targetJvm()
    targetJs()
    targetWasmJs()
    targetIos()

    serializationJson()
}
