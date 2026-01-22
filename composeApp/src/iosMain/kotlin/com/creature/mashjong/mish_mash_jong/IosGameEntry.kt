@file:Suppress("INVISIBLE_REFERENCE", "INVISIBLE_MEMBER")

package com.creature.mashjong.mish_mash_jong

import com.creature.mashjong.gameStart
import korlibs.image.color.Colors
import korlibs.render.IosGameWindow
import korlibs.render.MyGLKViewController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import platform.UIKit.UIApplication
import platform.UIKit.UIModalPresentationFullScreen
import platform.UIKit.UIModalTransitionStyleCrossDissolve

// Use a dedicated Scope for the game transition
private val launcherScope = CoroutineScope(Dispatchers.Main + SupervisorJob())

fun startGame() {
    launcherScope.launch {
        // 1. Manually initialize the GameWindow to satisfy the dependency
        val gameWindow = IosGameWindow()

        // 2. Setup the Controller with the explicit window provider
        val gameViewController = MyGLKViewController(
            gameWindowProvider = { gameWindow },
            entry = {
                // This is your commonMain game entry
                gameStart(
                    gameWindow = gameWindow,
                )
            }
        ).apply {
            modalPresentationStyle = UIModalPresentationFullScreen
            modalTransitionStyle = UIModalTransitionStyleCrossDissolve
        }

        // 3. Find the top-most view controller to present from
        val rootVC = UIApplication.sharedApplication.keyWindow?.rootViewController

        rootVC?.presentViewController(
            viewControllerToPresent = gameViewController,
            animated = true,
            completion = {
                println("Mish Mash-Jong: Handover complete.")
            }
        )
    }
}