package com.leetlingo.app.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
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
import com.leetlingo.app.data.model.Option
import com.leetlingo.app.data.model.ReelProblem
import com.leetlingo.app.ui.components.DuoChunkyButton
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
            modifier = Modifier
                .fillMaxSize()
                .background(ReelBackground),
            contentAlignment = Alignment.Center
        ) {
            Text("Ingesting new AI problems...", color = ReelBlueGlow)
        }
        return
    }

    var currentIndex by remember { mutableStateOf(0) }
    var currentLevel by remember { mutableStateOf(SolveLevel.REEL_CARD) }
    var userPseudoInput by remember { mutableStateOf("") }
    var feedbackMessage by remember { mutableStateOf<String?>(null) }
    var isCheckingPseudo by remember { mutableStateOf(false) }

    val currentProblem = problems[currentIndex % problems.size]

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ReelBackground)
            .padding(16.dp)
    ) {
        when (currentLevel) {
            SolveLevel.REEL_CARD -> {
                ReelCardView(
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
                L1ApproachView(
                    problem = currentProblem,
                    onCorrectApproach = {
                        currentLevel = SolveLevel.L2_PSEUDO
                    },
                    onClose = { currentLevel = SolveLevel.REEL_CARD }
                )
            }

            SolveLevel.L2_PSEUDO -> {
                L2PseudoView(
                    problem = currentProblem,
                    pseudoText = userPseudoInput,
                    feedback = feedbackMessage,
                    onPseudoChange = { userPseudoInput = it },
                    onVerify = {
                        val lower = userPseudoInput.lowercase()
                        val matches = currentProblem.pseudoKeywords.count { lower.contains(it) }
                        if (matches >= 2) {
                            feedbackMessage = "Correct logic! You cracked the structure."
                            currentLevel = SolveLevel.L2_WIN_PYTHON
                        } else {
                            feedbackMessage = "Missing key components. Hint: ${currentProblem.pseudoHint}"
                        }
                    },
                    onClose = { currentLevel = SolveLevel.REEL_CARD }
                )
            }

            SolveLevel.L2_WIN_PYTHON -> {
                L2WinPythonView(
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
fun ReelCardView(
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
                        if (offsetY < -150f) {
                            onSwipeUpSkip()
                        } else if (offsetX > 150f) {
                            onSwipeRightTooHard()
                        }
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
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("🎬 REEL", color = ReelBlueGlow, fontWeight = FontWeight.Bold, fontSize = 20.sp)
            Text("🔥 7 Day Streak", color = Color(0xFFFFC800), fontWeight = FontWeight.SemiBold)
        }

        // Problem Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(vertical = 16.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = ReelCardBackground),
            border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(ReelBlue.copy(alpha = 0.5f)))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = problem.title,
                            color = Color.White,
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Black
                        )
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(ReelBlue.copy(alpha = 0.2f))
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(problem.topic, color = ReelBlueGlow, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = problem.statement,
                        color = Color(0xFFCBD5E1),
                        fontSize = 17.sp,
                        lineHeight = 24.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFF070B14))
                            .padding(12.dp)
                    ) {
                        Column {
                            Text("Example:", color = Color(0xFF64748B), fontSize = 12.sp)
                            Text(problem.inputSample, color = Color.White, fontFamily = FontFamily.Monospace, fontSize = 13.sp)
                            Text("➔ ${problem.outputSample}", color = ReelBlueGlow, fontFamily = FontFamily.Monospace, fontSize = 13.sp)
                        }
                    }
                }

                // Action area
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        "⬆️ Swipe Up to Pass  |  ➡️ Swipe Right if Too Hard",
                        color = Color(0xFF64748B),
                        fontSize = 12.sp,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    // Single CTA
                    DuoChunkyButton(
                        text = "⚡ CRACK THE APPROACH",
                        buttonColor = ReelBlue,
                        onClick = onCrackIt
                    )
                }
            }
        }
    }
}

@Composable
fun L1ApproachView(
    problem: ReelProblem,
    onCorrectApproach: () -> Unit,
    onClose: () -> Unit
) {
    var selectedOptionId by remember { mutableStateOf<String?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("L1: IDENTIFY APPROACH", color = ReelBlueGlow, fontWeight = FontWeight.Black, fontSize = 16.sp)
                Text("✕", color = Color.White, modifier = Modifier.clickable { onClose() }, fontSize = 20.sp)
            }

            Spacer(modifier = Modifier.height(12.dp))
            Text(problem.title, color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
            Text("Pick the optimal algorithmic strategy:", color = Color(0xFF94A3B8), fontSize = 14.sp)
            Spacer(modifier = Modifier.height(16.dp))

            // 4 Option Cards
            problem.options.forEach { option ->
                val isSelected = selectedOptionId == option.id
                val borderCol = if (isSelected) {
                    if (option.isCorrect) AscendGreen else ReflectRed
                } else Color(0xFF1E293B)

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(ReelCardBackground)
                        .border(2.dp, borderCol, RoundedCornerShape(16.dp))
                        .clickable {
                            selectedOptionId = option.id
                            if (option.isCorrect) {
                                errorMessage = null
                                onCorrectApproach()
                            } else {
                                errorMessage = option.explanation
                            }
                        }
                        .padding(16.dp)
                ) {
                    Column {
                        Text(option.title, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Text(option.description, color = Color(0xFF94A3B8), fontSize = 13.sp)
                    }
                }
            }

            errorMessage?.let {
                Spacer(modifier = Modifier.height(8.dp))
                Text("⚠️ $it", color = ReflectRed, fontSize = 13.sp)
            }
        }
    }
}

@Composable
fun L2PseudoView(
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
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("L2: WRITE PSEUDO CODE", color = ReelBlueGlow, fontWeight = FontWeight.Black, fontSize = 16.sp)
                Text("✕", color = Color.White, modifier = Modifier.clickable { onClose() }, fontSize = 20.sp)
            }

            Spacer(modifier = Modifier.height(12.dp))
            Text("Outline your algo logic in plain words:", color = Color(0xFF94A3B8), fontSize = 14.sp)
            Spacer(modifier = Modifier.height(8.dp))

            TextField(
                value = pseudoText,
                onValueChange = onPseudoChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(16.dp)),
                placeholder = { Text("e.g. init hashmap, iterate numbers, check complement...", color = Color(0xFF475569)) },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = ReelCardBackground,
                    unfocusedContainerColor = ReelCardBackground,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedIndicatorColor = ReelBlue
                )
            )

            feedback?.let {
                Spacer(modifier = Modifier.height(12.dp))
                Text(it, color = if (it.contains("Correct")) AscendGreen else Color(0xFFFFC800), fontSize = 14.sp)
            }
        }

        // Single primary Action
        DuoChunkyButton(
            text = "⚡ VERIFY PSEUDO CODE",
            buttonColor = ReelBlue,
            onClick = onVerify
        )
    }
}

@Composable
fun L2WinPythonView(
    problem: ReelProblem,
    onSaveVaultAndContinue: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text("🎉 YOU CRACKED IT!", color = AscendGreen, fontSize = 24.sp, fontWeight = FontWeight.Black)
            Text("Here is the optimal Python 3 solution:", color = Color(0xFF94A3B8), fontSize = 14.sp)
            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFF030712))
                    .padding(16.dp)
            ) {
                Text(
                    text = problem.pythonSolution,
                    color = Color(0xFF38BDF8),
                    fontFamily = FontFamily.Monospace,
                    fontSize = 14.sp,
                    lineHeight = 20.sp
                )
            }
        }

        // Single forward CTA
        DuoChunkyButton(
            text = "🏛️ VAULT & NEXT REEL",
            buttonColor = AscendGreen,
            onClick = onSaveVaultAndContinue
        )
    }
}
