package com.creature.mashjong.mish_mash_jong

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import kotlinx.coroutines.DelicateCoroutinesApi

@OptIn(DelicateCoroutinesApi::class)
fun main() = application {
    var isMenuVisible by remember { mutableStateOf(value = true) }

    Window(
        onCloseRequest = ::exitApplication,
        visible = isMenuVisible,
        title = "Mish Mash-Jong",
    ) {
        if (isMenuVisible) {
            MainMenu(onPlayClick = {
                startGame(
                    onClose = {
                        isMenuVisible = true
                    }
                )
                isMenuVisible = false
            })
        }
    }
}