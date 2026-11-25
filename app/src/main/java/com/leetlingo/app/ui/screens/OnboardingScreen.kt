package com.leetlingo.app.ui.screens

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.leetlingo.app.ui.components.Duo3DButton
import com.leetlingo.app.ui.components.DuoNinjaMascot
import com.leetlingo.app.ui.components.DuoOptionCard
import com.leetlingo.app.ui.theme.*
import kotlinx.coroutines.launch

enum class OnboardingStep {
    WELCOME,
    AUTH,
    PERMISSIONS,
    LEETCODE_URL,
    SURVEY_LEVEL,
    SURVEY_GOAL,
    SURVEY_TARGET
}

@Composable
fun OnboardingScreen(
    onComplete: (leetCodeUrl: String, level: String, dailyGoal: String, target: String) -> Unit
) {
    val theme = getAppThemeColors()
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val auth = remember { FirebaseAuth.getInstance() }
    var step by remember { mutableStateOf(OnboardingStep.WELCOME) }

    // Auth state
    var isSignUp by remember { mutableStateOf(true) }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isAuthLoading by remember { mutableStateOf(false) }
    var authError by remember { mutableStateOf<String?>(null) }

    // Profile & Survey state
    var leetCodeUrl by remember { mutableStateOf("") }
    var selectedLevel by remember { mutableStateOf<String?>(null) }
    var selectedGoal by remember { mutableStateOf<String?>(null) }
    var selectedTarget by remember { mutableStateOf<String?>(null) }

    val progressFraction = when (step) {
        OnboardingStep.WELCOME -> 0.10f
        OnboardingStep.AUTH -> 0.22f
        OnboardingStep.PERMISSIONS -> 0.36f
        OnboardingStep.LEETCODE_URL -> 0.50f
        OnboardingStep.SURVEY_LEVEL -> 0.68f
        OnboardingStep.SURVEY_GOAL -> 0.85f
        OnboardingStep.SURVEY_TARGET -> 1.0f
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) {
        step = OnboardingStep.LEETCODE_URL
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(theme.background)
            .padding(20.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Top Progress Bar (hidden on Welcome)
        if (step != OnboardingStep.WELCOME) {
            LinearProgressIndicator(
                progress = { progressFraction },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp, bottom = 16.dp)
                    .height(10.dp)
                    .clip(RoundedCornerShape(5.dp)),
                color = AscendGreen,
                trackColor = theme.border
            )
        } else {
            Spacer(modifier = Modifier.height(12.dp))
        }

        // Main Step Content
        Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
            when (step) {
                OnboardingStep.WELCOME -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        DuoNinjaMascot(
                            mood = "HAPPY",
                            dialogText = "Master algorithms like scrolling reels. Zero dread, pure momentum."
                        )
                        Spacer(modifier = Modifier.height(28.dp))
                        Text("LeetLingo", color = theme.textPrimary, style = HelveticaTitle)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "Bite-sized coding katas, instant pattern drills, and spaced repetition.",
                            color = theme.textSecondary,
                            style = HelveticaBody
                        )
                    }
                }

                OnboardingStep.AUTH -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Top
                    ) {
                        Text(
                            if (isSignUp) "Create Account" else "Welcome Back",
                            color = theme.textPrimary,
                            style = HelveticaTitle
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            if (isSignUp) "Save your streak and conquered katas." else "Log in to continue your streak.",
                            color = theme.textSecondary,
                            style = HelveticaBody
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        // Continue with Google (Credential Manager + Firebase Auth)
                        Duo3DButton(
                            text = if (isAuthLoading) "Signing in..." else "Continue with Google",
                            buttonColor = theme.cardBackground,
                            shadowColor = theme.border,
                            textColor = theme.textPrimary,
                            enabled = !isAuthLoading,
                            onClick = {
                                authError = null
                                val webClientIdRes = context.resources.getIdentifier(
                                    "default_web_client_id",
                                    "string",
                                    context.packageName
                                )
                                val webClientId = if (webClientIdRes != 0) context.getString(webClientIdRes) else ""
                                if (webClientId.isBlank()) {
                                    authError = "Enable Google Sign-In in Firebase Console & update google-services.json."
                                    return@Duo3DButton
                                }
                                isAuthLoading = true
                                coroutineScope.launch {
                                    try {
                                        val credentialManager = CredentialManager.create(context)
                                        val googleIdOption = GetGoogleIdOption.Builder()
                                            .setFilterByAuthorizedAccounts(false)
                                            .setServerClientId(webClientId)
                                            .build()
                                        val request = GetCredentialRequest.Builder()
                                            .addCredentialOption(googleIdOption)
                                            .build()
                                        val result = credentialManager.getCredential(context, request)
                                        val googleCred = GoogleIdTokenCredential.createFrom(result.credential.data)
                                        val firebaseCred = GoogleAuthProvider.getCredential(googleCred.idToken, null)
                                        auth.signInWithCredential(firebaseCred)
                                            .addOnSuccessListener {
                                                isAuthLoading = false
                                                step = OnboardingStep.PERMISSIONS
                                            }
                                            .addOnFailureListener { e ->
                                                isAuthLoading = false
                                                authError = e.localizedMessage ?: "Google Sign-In failed."
                                            }
                                    } catch (e: Exception) {
                                        isAuthLoading = false
                                        authError = e.localizedMessage ?: "Google Sign-In cancelled."
                                    }
                                }
                            }
                        )

                        authError?.let { err ->
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(err, color = ReflectRed, style = HelveticaSmall)
                        }

                        Spacer(modifier = Modifier.height(18.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            HorizontalDivider(modifier = Modifier.weight(1f), color = theme.border)
                            Text("  or  ", color = theme.textMuted, style = HelveticaSmall)
                            HorizontalDivider(modifier = Modifier.weight(1f), color = theme.border)
                        }

                        Spacer(modifier = Modifier.height(18.dp))

                        OutlinedTextField(
                            value = email,
                            onValueChange = { email = it },
                            label = { Text("Email", style = HelveticaSmall) },
                            leadingIcon = { Icon(Icons.Filled.Mail, contentDescription = null) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = password,
                            onValueChange = { password = it },
                            label = { Text("Password", style = HelveticaSmall) },
                            leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = null) },
                            visualTransformation = PasswordVisualTransformation(),
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = if (isSignUp) "Already have an account? Log In" else "New here? Sign Up",
                            color = ReelBlue,
                            style = HelveticaSmall,
                            modifier = Modifier
                                .clickable { isSignUp = !isSignUp }
                                .padding(vertical = 6.dp)
                        )
                    }
                }

                OnboardingStep.PERMISSIONS -> {
                    Column(modifier = Modifier.fillMaxSize()) {
                        DuoNinjaMascot(
                            mood = "HAPPY",
                            dialogText = "Grant permissions so LeetLingo can protect your daily streak."
                        )
                        Spacer(modifier = Modifier.height(20.dp))

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(20.dp))
                                .background(theme.cardBackground)
                                .border(2.dp, theme.border, RoundedCornerShape(20.dp))
                                .padding(20.dp)
                        ) {
                            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                Text("Notifications", color = AscendGreen, style = HelveticaSubtitle)
                                Text(
                                    "Used for daily streak reminders and spaced-repetition nudges.",
                                    color = theme.textSecondary,
                                    style = HelveticaBody
                                )
                            }
                        }
                    }
                }

                OnboardingStep.LEETCODE_URL -> {
                    Column(modifier = Modifier.fillMaxSize()) {
                        DuoNinjaMascot(
                            mood = "FOCUS",
                            dialogText = "Drop your LeetCode profile URL so we can calibrate your feed."
                        )
                        Spacer(modifier = Modifier.height(20.dp))

                        OutlinedTextField(
                            value = leetCodeUrl,
                            onValueChange = { leetCodeUrl = it },
                            label = { Text("LeetCode Profile URL", style = HelveticaSmall) },
                            placeholder = { Text("https://leetcode.com/u/username", color = theme.textMuted) },
                            leadingIcon = { Icon(Icons.Filled.Code, contentDescription = null) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "Skip for now",
                            color = theme.textMuted,
                            style = HelveticaSmall,
                            modifier = Modifier
                                .clickable { step = OnboardingStep.SURVEY_LEVEL }
                                .padding(vertical = 6.dp)
                        )
                    }
                }

                OnboardingStep.SURVEY_LEVEL -> {
                    val levels = listOf(
                        "Beginner" to "Just starting out with arrays & loops",
                        "Intermediate" to "Comfortable with hash maps, trees & BFS",
                        "Interview Ready" to "Grinding DP, graphs & hard patterns",
                        "Competitive" to "Targeting top-tier speed & optimality"
                    )
                    SurveyStepView(
                        question = "What is your current coding level?",
                        options = levels,
                        selected = selectedLevel,
                        onSelect = { selectedLevel = it }
                    )
                }

                OnboardingStep.SURVEY_GOAL -> {
                    val goals = listOf(
                        "5 mins / day" to "Casual daily momentum",
                        "15 mins / day" to "Steady interview prep",
                        "30 mins / day" to "Serious pattern mastery",
                        "60 mins / day" to "Hardcore shinobi grind"
                    )
                    SurveyStepView(
                        question = "Pick your daily commitment.",
                        options = goals,
                        selected = selectedGoal,
                        onSelect = { selectedGoal = it }
                    )
                }

                OnboardingStep.SURVEY_TARGET -> {
                    val targets = listOf(
                        "FAANG / Big Tech" to "Crack top-tier technical loops",
                        "High-Growth Startups" to "Ship fast & ace practical coding",
                        "Internships / Campus" to "Ace OA rounds & placements",
                        "Sharpness & Fun" to "Keep algorithmic instincts razor-sharp"
                    )
                    SurveyStepView(
                        question = "What are you training for?",
                        options = targets,
                        selected = selectedTarget,
                        onSelect = { selectedTarget = it }
                    )
                }
            }
        }

        // Bottom Single Primary CTA
        when (step) {
            OnboardingStep.WELCOME -> {
                Duo3DButton(
                    text = "Get Started",
                    buttonColor = AscendGreen,
                    shadowColor = AscendGreenDark,
                    onClick = { step = OnboardingStep.AUTH }
                )
            }

            OnboardingStep.AUTH -> {
                Duo3DButton(
                    text = if (isSignUp) "Sign Up" else "Log In",
                    buttonColor = AscendGreen,
                    shadowColor = AscendGreenDark,
                    enabled = email.isNotBlank() && password.isNotBlank() && !isAuthLoading,
                    onClick = {
                        authError = null
                        isAuthLoading = true
                        val task = if (isSignUp) {
                            auth.createUserWithEmailAndPassword(email.trim(), password)
                        } else {
                            auth.signInWithEmailAndPassword(email.trim(), password)
                        }
                        task.addOnSuccessListener {
                            isAuthLoading = false
                            step = OnboardingStep.PERMISSIONS
                        }.addOnFailureListener { e ->
                            isAuthLoading = false
                            authError = e.localizedMessage ?: "Authentication failed."
                        }
                    }
                )
            }

            OnboardingStep.PERMISSIONS -> {
                Duo3DButton(
                    text = "Allow Permissions",
                    buttonColor = AscendGreen,
                    shadowColor = AscendGreenDark,
                    onClick = {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            permissionLauncher.launch(arrayOf(Manifest.permission.POST_NOTIFICATIONS))
                        } else {
                            step = OnboardingStep.LEETCODE_URL
                        }
                    }
                )
            }

            OnboardingStep.LEETCODE_URL -> {
                Duo3DButton(
                    text = "Continue",
                    buttonColor = AscendGreen,
                    shadowColor = AscendGreenDark,
                    enabled = leetCodeUrl.isNotBlank(),
                    onClick = { step = OnboardingStep.SURVEY_LEVEL }
                )
            }

            OnboardingStep.SURVEY_LEVEL -> {
                Duo3DButton(
                    text = "Continue",
                    buttonColor = AscendGreen,
                    shadowColor = AscendGreenDark,
                    enabled = selectedLevel != null,
                    onClick = { step = OnboardingStep.SURVEY_GOAL }
                )
            }

            OnboardingStep.SURVEY_GOAL -> {
                Duo3DButton(
                    text = "Continue",
                    buttonColor = AscendGreen,
                    shadowColor = AscendGreenDark,
                    enabled = selectedGoal != null,
                    onClick = { step = OnboardingStep.SURVEY_TARGET }
                )
            }

            OnboardingStep.SURVEY_TARGET -> {
                Duo3DButton(
                    text = "Start Scrolling",
                    buttonColor = AscendGreen,
                    shadowColor = AscendGreenDark,
                    enabled = selectedTarget != null,
                    onClick = {
                        onComplete(
                            leetCodeUrl,
                            selectedLevel ?: "Intermediate",
                            selectedGoal ?: "15 mins / day",
                            selectedTarget ?: "FAANG / Big Tech"
                        )
                    }
                )
            }
        }
    }
}

@Composable
fun SurveyStepView(
    question: String,
    options: List<Pair<String, String>>,
    selected: String?,
    onSelect: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        DuoNinjaMascot(
            mood = "FOCUS",
            dialogText = question
        )
        Spacer(modifier = Modifier.height(16.dp))

        options.forEachIndexed { idx, (title, desc) ->
            DuoOptionCard(
                indexNumber = idx + 1,
                title = title,
                description = desc,
                isSelected = selected == title,
                statusColor = if (selected == title) AscendGreen else null,
                onClick = { onSelect(title) }
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}
