package com.creature.mashjong.mish_mash_jong

import android.R.anim.fade_in
import android.R.anim.fade_out
import com.creature.mashjong.createGameWindow
import korlibs.math.geom.Size
import korlibs.render.GameWindowCreationConfig
import korlibs.render.KorgwActivity

class AndroidGameActivity : KorgwActivity(
    config = GameWindowCreationConfig(fullscreen = true)
) {
    override suspend fun activityMain() {
        val displayMetrics = resources.displayMetrics
        val width = displayMetrics.widthPixels
        val height = displayMetrics.heightPixels

        createGameWindow(
            windowSize = Size(width = width.toDouble(), height = height.toDouble()),
            onClose = { finish() }
        )
    }

    override fun finish() {
        super.finish()

        overrideActivityTransition(
            OVERRIDE_TRANSITION_CLOSE,
            fade_in,
            fade_out,
        )
    }
}
