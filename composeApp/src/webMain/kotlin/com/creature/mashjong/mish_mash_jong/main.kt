package com.creature.mashjong.mish_mash_jong

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import com.creature.mashjong.gameStart
import korlibs.math.geom.Scale
import kotlinx.coroutines.delay

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    ComposeViewport(viewportContainerId = "compose-root") {
        var isGameRunning by remember { mutableStateOf(false) }

        if (!isGameRunning) {
            MainMenu(onPlayClick = {
                isGameRunning = true
            })
        }

        // When isGameRunning becomes true, this starts the game
        LaunchedEffect(isGameRunning) {
            if (isGameRunning) {
                // We use a small delay to let Compose clear the DOM
                delay(timeMillis = 100)
                gameStart(
                    fullScreen = false, // MUST BE FALSE ON WEB
                    scale = Scale(scale = 0.4)
                )
            }
        }
    }
}