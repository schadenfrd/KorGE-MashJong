@file:Suppress("INVISIBLE_REFERENCE", "INVISIBLE_MEMBER")

package com.creature.mashjong.mish_mash_jong

import com.creature.mashjong.createGameWindow
import com.creature.mashjong.domain.model.DeckStyle
import korlibs.image.format.readBitmap
import korlibs.io.stream.openAsync
import korlibs.math.geom.Size
import korlibs.render.IosGameWindow
import korlibs.render.MyGLKViewController
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import mish_mash_jong.composeapp.generated.resources.Res
import org.jetbrains.compose.resources.ExperimentalResourceApi
import platform.UIKit.UIApplication
import platform.UIKit.UIModalPresentationFullScreen
import platform.UIKit.UIModalTransitionStyleCrossDissolve
import platform.UIKit.UIScreen
import platform.UIKit.UIViewController

// Use a dedicated Scope for the game transition
private val launcherScope = CoroutineScope(Dispatchers.Main + SupervisorJob())

@OptIn(ExperimentalForeignApi::class, ExperimentalResourceApi::class)
fun startGame() {
    launcherScope.launch {
        val atlasBytes = Res.readBytes(DeckStyle.MishMash.resourcePath)
        val atlasBitmap = atlasBytes.openAsync().readBitmap()

        // 1. Manually initialize the GameWindow to satisfy the dependency
        val gameWindow = IosGameWindow()

        // 2. Setup the Controller with the explicit window provider
        lateinit var gameViewController: UIViewController

        gameViewController = MyGLKViewController(
            gameWindowProvider = { gameWindow },
            entry = {
                val screen = UIScreen.mainScreen
                val bounds = screen.nativeBounds
                val width = bounds.useContents { size.width }
                val height = bounds.useContents { size.height }

                // This is your commonMain game entry
                createGameWindow(
                    atlas = atlasBitmap,
                    gameWindow = gameWindow,
                    windowSize = Size(width, height),
                    onClose = {
                        gameViewController.dismissViewControllerAnimated(true, null)
                    }
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
                println("Mish-MashJong: Handover complete.")
            }
        )
    }
}