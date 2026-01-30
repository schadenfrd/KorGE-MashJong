package com.creature.mashjong.mish_mash_jong

import android.R.anim.fade_in
import android.R.anim.fade_out
import com.creature.mashjong.createGameWindow
import com.creature.mashjong.domain.model.DeckStyle
import korlibs.image.format.readBitmap
import korlibs.io.stream.openAsync
import korlibs.math.geom.Size
import korlibs.render.GameWindowCreationConfig
import korlibs.render.KorgwActivity
import mish_mash_jong.composeapp.generated.resources.Res
import org.jetbrains.compose.resources.ExperimentalResourceApi

class AndroidGameActivity : KorgwActivity(
    config = GameWindowCreationConfig(fullscreen = true)
) {
    @OptIn(ExperimentalResourceApi::class)
    override suspend fun activityMain() {
        val displayMetrics = resources.displayMetrics
        val width = displayMetrics.widthPixels
        val height = displayMetrics.heightPixels

        val atlasBytes = Res.readBytes(DeckStyle.MishMash.resourcePath)
        val atlasBitmap = atlasBytes.openAsync().readBitmap()

        createGameWindow(
            atlas = atlasBitmap,
            windowSize = Size(width = width.toDouble(), height = height.toDouble()),
            onClose = ::finish
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
