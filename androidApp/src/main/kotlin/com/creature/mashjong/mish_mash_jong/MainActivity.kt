package com.creature.mashjong.mish_mash_jong

import android.content.Intent
import android.R.anim.fade_in
import android.R.anim.fade_out
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import org.jetbrains.compose.ui.tooling.preview.Preview

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            MainMenu(onPlayClick = {
                val intent = Intent(this, AndroidGameActivity::class.java)
                startActivity(intent)

                overrideActivityTransition(
                    OVERRIDE_TRANSITION_OPEN,
                    fade_in,
                    fade_out
                )
            })
        }
    }
}

@Preview
@Composable
fun MainMenuAndroidPreview() {
    MainMenu()
}