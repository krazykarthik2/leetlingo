package com.leetlingo.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import com.leetlingo.app.ui.screens.LeetLingoMainScreen
import com.leetlingo.app.ui.screens.OnboardingScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var hasCompletedOnboarding by remember { mutableStateOf(false) }

            if (!hasCompletedOnboarding) {
                OnboardingScreen(
                    onComplete = { _, _, _, _ ->
                        hasCompletedOnboarding = true
                    }
                )
            } else {
                LeetLingoMainScreen()
            }
        }
    }
}
