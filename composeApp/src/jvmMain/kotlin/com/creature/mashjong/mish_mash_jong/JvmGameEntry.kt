package com.creature.mashjong.mish_mash_jong

import com.creature.mashjong.gameStart
import korlibs.event.DestroyEvent
import korlibs.event.DisposeEvent
import korlibs.event.StopEvent
import korlibs.korge.internal.DefaultViewport
import korlibs.logger.Logger
import korlibs.math.geom.Size2D
import korlibs.render.CreateDefaultGameWindow
import korlibs.render.GameWindowCreationConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

private val launcherScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
private val logger = Logger("JvmDesktopLogger")

//TODO: this doesn't work. Fix it!
// 1. With Dispatchers.Main the app doesn't show the text and clicking close on the window does nothing
// 2. With Dispatchers.Default the app closes as soon as you try to run it.
fun startGame(onClose: () -> Unit = {}) {
    Logger.defaultLevel = Logger.Level.DEBUG

    GlobalScope.launch {
        // 1. Manually initialize the GameWindow to satisfy the dependency
        val gameWindow = CreateDefaultGameWindow(
            config = GameWindowCreationConfig(
                fullscreen = false,
                title = "Mish Mash-Jong",
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
        gameStart(
            gameWindow = gameWindow,
            onClose = {
                gameWindow.close()
            }
        )
    }
}