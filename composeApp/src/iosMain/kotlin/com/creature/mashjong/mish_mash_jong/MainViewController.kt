package com.creature.mashjong.mish_mash_jong

import androidx.compose.ui.window.ComposeUIViewController
import platform.UIKit.UIViewController

@Suppress("Unused")
fun mainViewController(): UIViewController = ComposeUIViewController {
    MainMenu(
        onPlayClick = {
            startGame()
        }
    )
}