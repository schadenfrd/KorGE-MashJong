package com.creature.mashjong.mish_mash_jong

import com.creature.mashjong.domain.model.DeckStyle

import com.creature.mashjong.createGameWindow
import korlibs.event.DestroyEvent
import korlibs.event.DisposeEvent
import korlibs.event.StopEvent
import korlibs.image.format.readBitmap
import korlibs.io.stream.openAsync
import korlibs.korge.internal.DefaultViewport
import korlibs.logger.Logger
import korlibs.render.CreateDefaultGameWindow
import korlibs.render.GameWindowCreationConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import mish_mash_jong.composeapp.generated.resources.Res
import org.jetbrains.compose.resources.ExperimentalResourceApi

private val launcherScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
private val logger = Logger("JvmDesktopLogger")

//TODO: this doesn't work. Fix it!
// 1. With Dispatchers.Main the app doesn't show the text and clicking close on the window does nothing
// 2. With Dispatchers.Default the app closes as soon as you try to run it.
@OptIn(ExperimentalResourceApi::class)
fun startGame(onClose: () -> Unit = {}) {
    Logger.defaultLevel = Logger.Level.DEBUG

    GlobalScope.launch {
        val atlasBytes = Res.readBytes(DeckStyle.MishMash.resourcePath)
        val atlasBitmap = atlasBytes.openAsync().readBitmap()

        // 1. Manually initialize the GameWindow to satisfy the dependency
        val gameWindow = CreateDefaultGameWindow(
            config = GameWindowCreationConfig(
                fullscreen = false,
                title = "Mish-MashJong",
            )
        ).apply {
            setSize(
                width = DefaultViewport.SIZE.width.toInt(),
                height = DefaultViewport.SIZE.height.toInt()
            )
            onEvent(type = StopEvent) {
                logger.debug { "StopEvent triggered" }
                close()
                onClose()
            }
            onEvent(type = DisposeEvent) {
                logger.debug { "DisposeEvent triggered" }
                close()
                onClose()
            }
            onEvent(type = DestroyEvent) {
                logger.debug { "DestroyEvent triggered" }
                close()
                onClose()
            }
        }

        logger.debug { "Starting game..." }
        createGameWindow(
            atlas = atlasBitmap,
            gameWindow = gameWindow,
            onClose = {
                gameWindow.close()
            }
        )
    }
}