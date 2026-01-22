package com.creature.mashjong.mish_mash_jong

import android.R.anim.fade_in
import android.R.anim.fade_out
import com.creature.mashjong.gameStart
import korlibs.render.GameWindowCreationConfig
import korlibs.render.KorgwActivity

class AndroidGameActivity : KorgwActivity(
    config = GameWindowCreationConfig(fullscreen = true)
) {
    override suspend fun activityMain() {
        gameStart()
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
