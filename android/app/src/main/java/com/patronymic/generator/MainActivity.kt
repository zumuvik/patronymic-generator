package com.patronymic.generator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.patronymic.generator.ui.screens.MainScreen
import com.patronymic.generator.ui.theme.PatronymicTheme

/**
 * Главная Activity приложения.
 * SplashScreen + Edge-to-Edge + PatronymicTheme + MainScreen.
 */
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            PatronymicTheme {
                MainScreen()
            }
        }
    }
}
