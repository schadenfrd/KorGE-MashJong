package com.creature.mashjong.mish_mash_jong

import com.creature.mashjong.domain.model.DeckStyle

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import com.creature.mashjong.createGameWindow
import korlibs.image.format.readBitmap
import korlibs.io.stream.openAsync
import korlibs.math.geom.Scale
import kotlinx.coroutines.delay
import mish_mash_jong.composeapp.generated.resources.Res
import org.jetbrains.compose.resources.ExperimentalResourceApi

@OptIn(ExperimentalComposeUiApi::class, ExperimentalResourceApi::class)
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
                val atlasBytes = Res.readBytes(DeckStyle.MishMash.resourcePath)
                val atlasBitmap = atlasBytes.openAsync().readBitmap()

                // We use a small delay to let Compose clear the DOM
                delay(timeMillis = 100)
                createGameWindow(
                    atlas = atlasBitmap,
                    fullScreen = false, // MUST BE FALSE ON WEB
                    scale = Scale(scale = 0.4)
                )
            }
        }
    }
}