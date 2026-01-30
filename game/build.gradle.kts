import korlibs.korge.gradle.GameCategory

plugins {
    alias(libs.plugins.korge)
}

val ID = "com.creature.mashjong"
val NAME = "Mish-MashJong"
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

// Fix for: Xcode does not support simulator tests for tvos_simulator_arm64.
// We disable tvOS tests since the SDK is not available in the current environment.
tasks.matching { it.name.contains("tvos") && it.name.endsWith("Test") }.configureEach {
    enabled = false
}
