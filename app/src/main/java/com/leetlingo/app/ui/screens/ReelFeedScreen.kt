package com.leetlingo.app.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.TrendingFlat
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Warning
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
    val theme = getAppThemeColors()

    if (problems.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize().background(theme.background),
            contentAlignment = Alignment.Center
        ) {
            Text("Ingesting Katas...", color = theme.textMuted, style = HelveticaSubtitle)
        }
        return
    }

    // Instagram Reel style vertical pager state with preloading
    val pagerState = rememberPagerState(pageCount = { problems.size })
    var activeSolveProblem by remember { mutableStateOf<ReelProblem?>(null) }
    var currentLevel by remember { mutableStateOf(SolveLevel.REEL_CARD) }
    var userPseudoInput by remember { mutableStateOf("") }
    var feedbackMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(theme.background)
    ) {
        // =================================================================
        // PINNED INSTAGRAM-STYLE TOP BAR (NEVER MOVES DURING VERTICAL SCROLL)
        // =================================================================
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(theme.topBarBackground)
                .padding(horizontal = 20.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Filled.LocalFireDepartment,
                    contentDescription = "Streak",
                    tint = VaultYellow,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text("7", color = VaultYellow, style = HelveticaSubtitle)
            }
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(ReelBlue.copy(alpha = 0.15f))
                    .border(2.dp, ReelBlue, RoundedCornerShape(12.dp))
                    .padding(horizontal = 14.dp, vertical = 6.dp)
            ) {
                Text("REELS", color = ReelBlue, style = HelveticaSmall)
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Filled.Diamond,
                    contentDescription = "Gems",
                    tint = ReelBlue,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("420", color = ReelBlue, style = HelveticaSubtitle)
            }
        }

        Divider(color = theme.border, thickness = 1.dp)

        // =================================================================
        // FLUID VERTICAL PAGER (INSTAGRAM / TIKTOK REEL MECHANICS)
        // Scrolls fluidly; preloads adjacent items; stays locked to page
        // =================================================================
        Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
            if (activeSolveProblem == null) {
                VerticalPager(
                    state = pagerState,
                    beyondViewportPageCount = 2, // Preload next 2 reels
                    modifier = Modifier.fillMaxSize()
                ) { page ->
                    val problem = problems[page]
                    ReelItemFluid(
                        problem = problem,
                        onSwipeRightTooHard = {
                            onTooHardBail(problem)
                        },
                        onCrackIt = {
                            activeSolveProblem = problem
                            currentLevel = SolveLevel.L1_APPROACH
                        }
                    )
                }
            } else {
                // Interactive Modal Sheet for L1, L2 and Victory
                val activeProblem = activeSolveProblem!!
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(theme.background)
                        .padding(16.dp)
                ) {
                    when (currentLevel) {
                        SolveLevel.REEL_CARD -> activeSolveProblem = null

                        SolveLevel.L1_APPROACH -> {
                            L1ApproachViewDuo(
                                problem = activeProblem,
                                onCorrectApproach = {
                                    currentLevel = SolveLevel.L2_PSEUDO
                                },
                                onClose = { activeSolveProblem = null }
                            )
                        }

                        SolveLevel.L2_PSEUDO -> {
                            L2PseudoViewDuo(
                                problem = activeProblem,
                                pseudoText = userPseudoInput,
                                feedback = feedbackMessage,
                                onPseudoChange = { userPseudoInput = it },
                                onVerify = {
                                    val lower = userPseudoInput.lowercase()
                                    val matches = activeProblem.pseudoKeywords.count { lower.contains(it) }
                                    if (matches >= 2) {
                                        feedbackMessage = "Correct logic! You unlocked the clean code."
                                        currentLevel = SolveLevel.L2_WIN_PYTHON
                                    } else {
                                        feedbackMessage = "Needs work. Hint: ${activeProblem.pseudoHint}"
                                    }
                                },
                                onClose = { activeSolveProblem = null }
                            )
                        }

                        SolveLevel.L2_WIN_PYTHON -> {
                            L2WinPythonViewDuo(
                                problem = activeProblem,
                                onSaveVaultAndContinue = {
                                    onSaveToVault(activeProblem, userPseudoInput)
                                    userPseudoInput = ""
                                    feedbackMessage = null
                                    activeSolveProblem = null
                                    currentLevel = SolveLevel.REEL_CARD
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Individual Full-Screen Reel View item inside VerticalPager.
 * When swiping right:
 * - The card itself DOES NOT MOVE (no horizontal translation).
 * - A red overlay covering the problem card fades in from 0 opacity up to full redness
 *   with the text "I DON'T KNOW YET" as the drag progresses towards completion.
 * - On release past the threshold, the bail action triggers.
 */
@Composable
fun ReelItemFluid(
    problem: ReelProblem,
    onSwipeRightTooHard: () -> Unit,
    onCrackIt: () -> Unit
) {
    val theme = getAppThemeColors()
    var dragAccumulated by remember { mutableStateOf(0f) }
    var isDragging by remember { mutableStateOf(false) }

    // Threshold in pixels to complete the "I don't know yet" bail
    val swipeThreshold = 300f

    // Calculate normalized progress (0.0f to 1.0f)
    val targetProgress = (dragAccumulated / swipeThreshold).coerceIn(0f, 1f)
    val animatedProgress by animateFloatAsState(
        targetValue = targetProgress,
        animationSpec = tween(durationMillis = if (isDragging) 0 else 250),
        label = "SwipeRednessProgress"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(problem.id) {
                detectHorizontalDragGestures(
                    onDragStart = {
                        isDragging = true
                    },
                    onDragEnd = {
                        isDragging = false
                        if (dragAccumulated >= swipeThreshold * 0.75f) {
                            onSwipeRightTooHard()
                        }
                        dragAccumulated = 0f
                    },
                    onDragCancel = {
                        isDragging = false
                        dragAccumulated = 0f
                    },
                    onHorizontalDrag = { change, dragAmount ->
                        change.consume()
                        // Only swipe RIGHT (positive drag) accumulates
                        val newTotal = dragAccumulated + dragAmount
                        dragAccumulated = newTotal.coerceAtLeast(0f)
                    }
                )
            }
    ) {
        // Base Content (Card stays stationary, DOES NOT MOVE)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                // Topic Pill
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(ReelBlue.copy(alpha = 0.15f))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(problem.topic.uppercase(), color = ReelBlue, style = HelveticaSmall)
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Ninja Mascot Speech
                DuoNinjaMascot(
                    mood = "FOCUS",
                    dialogText = problem.statement
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Example Box
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(theme.cardBackground)
                        .border(2.dp, theme.border, RoundedCornerShape(20.dp))
                        .padding(18.dp)
                ) {
                    Column {
                        Text(problem.title, color = theme.textPrimary, style = HelveticaTitle)
                        Spacer(modifier = Modifier.height(10.dp))

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(theme.surfaceHighlight)
                                .border(1.dp, theme.border, RoundedCornerShape(12.dp))
                                .padding(12.dp)
                        ) {
                            Column {
                                Text("INPUT", color = theme.textMuted, style = HelveticaSmall)
                                Text(problem.inputSample, color = theme.textPrimary, fontFamily = FontFamily.Monospace, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.height(8.dp))
                                Text("OUTPUT TARGET", color = theme.textMuted, style = HelveticaSmall)
                                Text(problem.outputSample, color = ReelBlue, fontFamily = FontFamily.Monospace, fontSize = 14.sp, fontWeight = FontWeight.Black)
                            }
                        }
                    }
                }
            }

            Column(
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Scroll UP for next reel  •  Swipe RIGHT if too hard",
                    color = theme.textSecondary,
                    style = HelveticaSmall,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                // Chunky 3D CTA (Reel -> Blue)
                Duo3DButton(
                    text = "Crack Approach",
                    buttonColor = ReelBlue,
                    shadowColor = ReelBlueDark,
                    onClick = onCrackIt
                )
            }
        }

        // RED OVERLAY: Covers problem card with redness that increases in opacity as swipe completes
        if (animatedProgress > 0.01f) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(ReflectRed.copy(alpha = animatedProgress * 0.92f)),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Warning,
                        contentDescription = "I Don't Know Yet",
                        tint = Color.White.copy(alpha = animatedProgress),
                        modifier = Modifier.size(56.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "I DON'T KNOW YET",
                        color = Color.White.copy(alpha = animatedProgress),
                        style = HelveticaTitle,
                        fontWeight = FontWeight.Black
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Saving to Reflection Diagnostic Gap...",
                        color = Color.White.copy(alpha = animatedProgress * 0.85f),
                        style = HelveticaSubtitle
                    )
                }
            }
        }
    }
}

@Composable
fun L1ApproachViewDuo(
    problem: ReelProblem,
    onCorrectApproach: () -> Unit,
    onClose: () -> Unit
) {
    val theme = getAppThemeColors()
    var selectedOptionId by remember { mutableStateOf<String?>(null) }
    var selectedOption by remember { mutableStateOf<com.leetlingo.app.data.model.Option?>(null) }
    var showResultSheet by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "Close",
                    tint = theme.textMuted,
                    modifier = Modifier.size(24.dp).clickable { onClose() }
                )
                LinearProgressIndicator(
                    progress = { 0.5f },
                    modifier = Modifier.weight(1f).height(12.dp).clip(RoundedCornerShape(6.dp)),
                    color = ReelBlue,
                    trackColor = theme.border
                )
            }

            DuoNinjaMascot(
                mood = "FOCUS",
                dialogText = "Which algorithmic pattern solves ${problem.title}?"
            )

            Spacer(modifier = Modifier.height(18.dp))

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

        Column {
            if (showResultSheet && selectedOption != null) {
                val isCorrect = selectedOption!!.isCorrect
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(if (isCorrect) ReelBlue.copy(alpha = 0.15f) else ReflectRed.copy(alpha = 0.15f))
                        .padding(14.dp)
                ) {
                    Column {
                        Text(
                            if (isCorrect) "CRACKED!" else "MISSED PATTERN!",
                            color = if (isCorrect) ReelBlue else ReflectRed,
                            style = HelveticaSubtitle
                        )
                        Text(
                            selectedOption!!.explanation,
                            color = theme.textPrimary,
                            style = HelveticaBody
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
                    onClick = { showResultSheet = true }
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
    val theme = getAppThemeColors()

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "Close",
                    tint = theme.textMuted,
                    modifier = Modifier.size(24.dp).clickable { onClose() }
                )
                LinearProgressIndicator(
                    progress = { 0.85f },
                    modifier = Modifier.weight(1f).height(12.dp).clip(RoundedCornerShape(6.dp)),
                    color = ReelBlue,
                    trackColor = theme.border
                )
            }

            DuoNinjaMascot(
                mood = "HAPPY",
                dialogText = "Draft your pseudo-code in plain words. No syntax stress!"
            )

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(theme.cardBackground)
                    .border(2.dp, theme.border, RoundedCornerShape(18.dp))
                    .padding(14.dp)
            ) {
                TextField(
                    value = pseudoText,
                    onValueChange = onPseudoChange,
                    modifier = Modifier.fillMaxSize(),
                    placeholder = { Text("e.g. initialize map seen, iterate nums, check complement in map...", color = theme.textMuted) },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = theme.cardBackground,
                        unfocusedContainerColor = theme.cardBackground,
                        focusedTextColor = theme.textPrimary,
                        unfocusedTextColor = theme.textPrimary,
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
                        .background(if (isGood) ReelBlue.copy(alpha = 0.15f) else VaultYellow.copy(alpha = 0.15f))
                        .padding(12.dp)
                ) {
                    Text(
                        it,
                        color = if (isGood) ReelBlue else VaultYellowDark,
                        style = HelveticaSmall
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
    val theme = getAppThemeColors()

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            DuoNinjaMascot(
                mood = "HAPPY",
                dialogText = "Kata mastered! Here is the clean Python 3 implementation."
            )

            Spacer(modifier = Modifier.height(18.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(theme.cardBackground)
                    .border(2.dp, ReelBlue, RoundedCornerShape(20.dp))
                    .padding(16.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("PYTHON 3", color = ReelBlue, style = HelveticaSmall)
                        Text("OPTIMAL RUNTIME", color = theme.textMuted, style = HelveticaSmall)
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = problem.pythonSolution,
                        color = theme.textPrimary,
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
