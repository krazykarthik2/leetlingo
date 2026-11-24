package com.leetlingo.app.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leetlingo.app.data.model.ReelProblem
import com.leetlingo.app.ui.components.Duo3DButton
import com.leetlingo.app.ui.components.DuoNinjaMascot
import com.leetlingo.app.ui.components.DuoOptionCard
import com.leetlingo.app.ui.theme.*

enum class SolveLevel {
    REEL_CARD,
    L1_APPROACH,
    L2_PSEUDO,
    L2_WIN_PYTHON
}

@Composable
fun ReelFeedScreen(
    problems: List<ReelProblem>,
    onSaveToVault: (ReelProblem, String) -> Unit,
    onTooHardBail: (ReelProblem) -> Unit
) {
    if (problems.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize().background(DuoPolar),
            contentAlignment = Alignment.Center
        ) {
            Text("Ingesting algorithm katas...", color = DuoWolf, fontWeight = FontWeight.Bold)
        }
        return
    }

    var currentIndex by remember { mutableStateOf(0) }
    var currentLevel by remember { mutableStateOf(SolveLevel.REEL_CARD) }
    var userPseudoInput by remember { mutableStateOf("") }
    var feedbackMessage by remember { mutableStateOf<String?>(null) }
    val currentProblem = problems[currentIndex % problems.size]

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DuoPolar)
            .padding(16.dp)
    ) {
        when (currentLevel) {
            SolveLevel.REEL_CARD -> {
                ReelCardViewDuo(
                    problem = currentProblem,
                    onSwipeUpSkip = {
                        currentIndex++
                        currentLevel = SolveLevel.REEL_CARD
                    },
                    onSwipeRightTooHard = {
                        onTooHardBail(currentProblem)
                        currentIndex++
                        currentLevel = SolveLevel.REEL_CARD
                    },
                    onCrackIt = {
                        currentLevel = SolveLevel.L1_APPROACH
                    }
                )
            }

            SolveLevel.L1_APPROACH -> {
                L1ApproachViewDuo(
                    problem = currentProblem,
                    onCorrectApproach = {
                        currentLevel = SolveLevel.L2_PSEUDO
                    },
                    onClose = { currentLevel = SolveLevel.REEL_CARD }
                )
            }

            SolveLevel.L2_PSEUDO -> {
                L2PseudoViewDuo(
                    problem = currentProblem,
                    pseudoText = userPseudoInput,
                    feedback = feedbackMessage,
                    onPseudoChange = { userPseudoInput = it },
                    onVerify = {
                        val lower = userPseudoInput.lowercase()
                        val matches = currentProblem.pseudoKeywords.count { lower.contains(it) }
                        if (matches >= 2) {
                            feedbackMessage = "Correct logic! You unlocked the clean code."
                            currentLevel = SolveLevel.L2_WIN_PYTHON
                        } else {
                            feedbackMessage = "Needs work. Hint: ${currentProblem.pseudoHint}"
                        }
                    },
                    onClose = { currentLevel = SolveLevel.REEL_CARD }
                )
            }

            SolveLevel.L2_WIN_PYTHON -> {
                L2WinPythonViewDuo(
                    problem = currentProblem,
                    onSaveVaultAndContinue = {
                        onSaveToVault(currentProblem, userPseudoInput)
                        userPseudoInput = ""
                        feedbackMessage = null
                        currentIndex++
                        currentLevel = SolveLevel.REEL_CARD
                    }
                )
            }
        }
    }
}

@Composable
fun ReelCardViewDuo(
    problem: ReelProblem,
    onSwipeUpSkip: () -> Unit,
    onSwipeRightTooHard: () -> Unit,
    onCrackIt: () -> Unit
) {
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .offset(x = (offsetX / 3).dp, y = (offsetY / 3).dp)
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragEnd = {
                        if (offsetY < -150f) onSwipeUpSkip()
                        else if (offsetX > 150f) onSwipeRightTooHard()
                        offsetX = 0f
                        offsetY = 0f
                    },
                    onDrag = { change, dragAmount ->
                        change.consume()
                        offsetX += dragAmount.x
                        offsetY += dragAmount.y
                    }
                )
            },
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Duolingo Top Header
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("🔥", fontSize = 20.sp)
                Spacer(modifier = Modifier.width(6.dp))
                Text("7", color = DuoBeeYellow, fontWeight = FontWeight.Black, fontSize = 18.sp)
            }
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFEBF7FD))
                    .border(2.dp, DuoMacawBlue, RoundedCornerShape(12.dp))
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(problem.topic.uppercase(), color = DuoMacawBlue, fontWeight = FontWeight.Black, fontSize = 11.sp)
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("💎", fontSize = 18.sp)
                Spacer(modifier = Modifier.width(4.dp))
                Text("420", color = DuoMacawBlue, fontWeight = FontWeight.Black, fontSize = 18.sp)
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Mascot speaking the problem
        DuoNinjaMascot(
            mood = "FOCUS",
            dialogText = problem.statement
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Example Card Box
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .clip(RoundedCornerShape(20.dp))
                .background(DuoSnow)
                .border(2.dp, DuoSwan, RoundedCornerShape(20.dp))
                .padding(18.dp)
        ) {
            Column(verticalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxSize()) {
                Column {
                    Text(problem.title, color = DuoEel, fontSize = 24.sp, fontWeight = FontWeight.Black)
                    Spacer(modifier = Modifier.height(10.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(DuoPolar)
                            .border(1.dp, DuoSwan, RoundedCornerShape(12.dp))
                            .padding(12.dp)
                    ) {
                        Column {
                            Text("INPUT", color = DuoWolf, fontSize = 11.sp, fontWeight = FontWeight.Black)
                            Text(problem.inputSample, color = DuoEel, fontFamily = FontFamily.Monospace, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(6.dp))
                            Text("TARGET OUTPUT", color = DuoWolf, fontSize = 11.sp, fontWeight = FontWeight.Black)
                            Text(problem.outputSample, color = DuoFeatherGreen, fontFamily = FontFamily.Monospace, fontSize = 14.sp, fontWeight = FontWeight.Black)
                        }
                    }
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                    Text(
                        "Swipe UP to skip • Swipe RIGHT if too hard",
                        color = DuoWolf,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Authentic Duolingo Chunky 3D Button (Reel -> Blue)
        Duo3DButton(
            text = "Solve Kata",
            buttonColor = ReelBlue,
            shadowColor = ReelBlueDark,
            onClick = onCrackIt
        )
    }
}

@Composable
fun L1ApproachViewDuo(
    problem: ReelProblem,
    onCorrectApproach: () -> Unit,
    onClose: () -> Unit
) {
    var selectedOptionId by remember { mutableStateOf<String?>(null) }
    var selectedOption by remember { mutableStateOf<com.leetlingo.app.data.model.Option?>(null) }
    var showResultSheet by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            // Header Progress Bar
            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "✕",
                    color = DuoWolf,
                    fontWeight = FontWeight.Black,
                    fontSize = 20.sp,
                    modifier = Modifier.clickable { onClose() }.padding(end = 12.dp)
                )
                LinearProgressIndicator(
                    progress = { 0.5f },
                    modifier = Modifier.weight(1f).height(12.dp).clip(RoundedCornerShape(6.dp)),
                    color = ReelBlue,
                    trackColor = DuoSwan
                )
            }

            DuoNinjaMascot(
                mood = "FOCUS",
                dialogText = "Select the optimal strategy to master ${problem.title}!"
            )

            Spacer(modifier = Modifier.height(18.dp))

            // Duolingo 3D Choice Options
            problem.options.forEachIndexed { idx, option ->
                val isSelected = selectedOptionId == option.id
                DuoOptionCard(
                    indexNumber = idx + 1,
                    title = option.title,
                    description = option.description,
                    isSelected = isSelected,
                    statusColor = if (showResultSheet && isSelected) {
                        if (option.isCorrect) ReelBlue else ReflectRed
                    } else null,
                    onClick = {
                        if (!showResultSheet) {
                            selectedOptionId = option.id
                            selectedOption = option
                        }
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        // Bottom Check / Continue Button Area
        Column {
            if (showResultSheet && selectedOption != null) {
                val isCorrect = selectedOption!!.isCorrect
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(if (isCorrect) Color(0xFFD7FFB8) else Color(0xFFFFDFE0))
                        .padding(14.dp)
                ) {
                    Column {
                        Text(
                            if (isCorrect) "EXCELLENT!" else "NOT QUITE!",
                            color = if (isCorrect) DuoFeatherGreenDark else DuoCardinalRedDark,
                            fontWeight = FontWeight.Black,
                            fontSize = 16.sp
                        )
                        Text(
                            selectedOption!!.explanation,
                            color = DuoEel,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
            }

            if (!showResultSheet) {
                Duo3DButton(
                    text = "Check",
                    buttonColor = ReelBlue,
                    shadowColor = ReelBlueDark,
                    enabled = selectedOptionId != null,
                    onClick = {
                        showResultSheet = true
                    }
                )
            } else {
                Duo3DButton(
                    text = if (selectedOption?.isCorrect == true) "Continue" else "Try Again",
                    buttonColor = if (selectedOption?.isCorrect == true) ReelBlue else ReflectRed,
                    shadowColor = if (selectedOption?.isCorrect == true) ReelBlueDark else ReflectRedDark,
                    onClick = {
                        if (selectedOption?.isCorrect == true) {
                            onCorrectApproach()
                        } else {
                            showResultSheet = false
                            selectedOptionId = null
                            selectedOption = null
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun L2PseudoViewDuo(
    problem: ReelProblem,
    pseudoText: String,
    feedback: String?,
    onPseudoChange: (String) -> Unit,
    onVerify: () -> Unit,
    onClose: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "✕",
                    color = DuoWolf,
                    fontWeight = FontWeight.Black,
                    fontSize = 20.sp,
                    modifier = Modifier.clickable { onClose() }.padding(end = 12.dp)
                )
                LinearProgressIndicator(
                    progress = { 0.85f },
                    modifier = Modifier.weight(1f).height(12.dp).clip(RoundedCornerShape(6.dp)),
                    color = DuoFeatherGreen,
                    trackColor = DuoSwan
                )
            }

            DuoNinjaMascot(
                mood = "HAPPY",
                dialogText = "Draft the algorithm logic in your own words. No rigid syntax needed!"
            )

            Spacer(modifier = Modifier.height(16.dp))

            // White Card Input Area
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(DuoSnow)
                    .border(2.dp, DuoSwan, RoundedCornerShape(18.dp))
                    .padding(14.dp)
            ) {
                TextField(
                    value = pseudoText,
                    onValueChange = onPseudoChange,
                    modifier = Modifier.fillMaxSize(),
                    placeholder = { Text("e.g. initialize map seen, iterate nums, check complement in map...", color = DuoHare) },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = DuoSnow,
                        unfocusedContainerColor = DuoSnow,
                        focusedTextColor = DuoEel,
                        unfocusedTextColor = DuoEel,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    )
                )
            }

            feedback?.let {
                Spacer(modifier = Modifier.height(12.dp))
                val isGood = it.contains("Correct")
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isGood) Color(0xFFD7FFB8) else Color(0xFFFFF2B2))
                        .padding(12.dp)
                ) {
                    Text(
                        it,
                        color = if (isGood) DuoFeatherGreenDark else Color(0xFFB28000),
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
            }
        }

        Duo3DButton(
            text = "Verify Logic",
            buttonColor = ReelBlue,
            shadowColor = ReelBlueDark,
            enabled = pseudoText.isNotBlank(),
            onClick = onVerify
        )
    }
}

@Composable
fun L2WinPythonViewDuo(
    problem: ReelProblem,
    onSaveVaultAndContinue: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            DuoNinjaMascot(
                mood = "HAPPY",
                dialogText = "Ninja victory! Here is the clean Python 3 implementation."
            )

            Spacer(modifier = Modifier.height(18.dp))

            // Duolingo Code Scroll Container
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(DuoSnow)
                    .border(2.dp, DuoFeatherGreen, RoundedCornerShape(20.dp))
                    .padding(16.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("PYTHON 3", color = DuoFeatherGreen, fontWeight = FontWeight.Black, fontSize = 12.sp)
                        Text("O(N) TIME", color = DuoWolf, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = problem.pythonSolution,
                        color = DuoEel,
                        fontFamily = FontFamily.Monospace,
                        fontSize = 13.sp,
                        lineHeight = 19.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        Duo3DButton(
            text = "Save to Vault & Next",
            buttonColor = ReelBlue,
            shadowColor = ReelBlueDark,
            onClick = onSaveVaultAndContinue
        )
    }
}
