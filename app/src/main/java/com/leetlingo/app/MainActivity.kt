package com.leetlingo.app

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import com.google.firebase.auth.FirebaseAuth
import com.leetlingo.app.ui.screens.LeetLingoMainScreen
import com.leetlingo.app.ui.screens.OnboardingScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(Color.BLACK)
        )
        val prefs = getSharedPreferences("leetlingo_prefs", Context.MODE_PRIVATE)
        val alreadyOnboarded = prefs.getBoolean("has_completed_onboarding", false) ||
            FirebaseAuth.getInstance().currentUser != null

        setContent {
            var hasCompletedOnboarding by remember { mutableStateOf(alreadyOnboarded) }

            if (!hasCompletedOnboarding) {
                OnboardingScreen(
                    onComplete = { _, _, _, _ ->
                        prefs.edit().putBoolean("has_completed_onboarding", true).apply()
                        hasCompletedOnboarding = true
                    }
                )
            } else {
                LeetLingoMainScreen()
            }
        }
    }
}
