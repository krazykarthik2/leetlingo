package com.leetlingo.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.leetlingo.app.data.model.Concept
import com.leetlingo.app.ui.components.Duo3DButton
import com.leetlingo.app.ui.components.DuoNinjaMascot
import com.leetlingo.app.ui.theme.*

enum class AscendPhase {
    OVERVIEW,
    STEP_1_INVARIANT,
    STEP_2_WHEN_TO_USE,
    STEP_3_PREREQUISITES,
    STEP_COMPLETE
}

@Composable
fun AscendScreen(
    concepts: List<Concept>,
    onAbsorbConcept: (Concept) -> Unit
) {
    val theme = getAppThemeColors()
    val currentConcept = remember(concepts) { concepts.randomOrNull() }
    var currentPhase by remember { mutableStateOf(AscendPhase.OVERVIEW) }

    if (currentPhase != AscendPhase.OVERVIEW && currentConcept != null) {
        AscendLearningFlow(
            concept = currentConcept,
            currentPhase = currentPhase,
            onPhaseChange = { currentPhase = it },
            onFinish = {
                currentPhase = AscendPhase.OVERVIEW
                onAbsorbConcept(currentConcept)
            },
            onClose = { currentPhase = AscendPhase.OVERVIEW }
        )
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(theme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Ascend", color = AscendGreen, style = HelveticaTitle)
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(AscendGreenLight)
                        .border(2.dp, AscendGreen, RoundedCornerShape(12.dp))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text("Random Encounter", color = AscendGreenDark, style = HelveticaSmall)
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            DuoNinjaMascot(
                mood = "HAPPY",
                dialogText = "Today's pattern is locked in. Absorb it one step at a time."
            )

            Spacer(modifier = Modifier.height(20.dp))

            currentConcept?.let { concept ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(theme.cardBackground)
                        .border(2.dp, theme.border, RoundedCornerShape(20.dp))
                        .padding(22.dp)
                ) {
                    Column {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(AscendGreen.copy(alpha = 0.15f))
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                concept.topic.replaceFirstChar { it.uppercase() },
                                color = AscendGreen,
                                style = HelveticaSmall
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))
                        Text(concept.name, color = theme.textPrimary, style = HelveticaTitle)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(concept.tagline, color = theme.textSecondary, style = HelveticaSubtitle)
                    }
                }
            }
        }

        Duo3DButton(
            text = "Absorb Pattern & Drill",
            buttonColor = AscendGreen,
            shadowColor = AscendGreenDark,
            onClick = {
                if (currentConcept != null) {
                    currentPhase = AscendPhase.STEP_1_INVARIANT
                }
            }
        )
    }
}

@Composable
fun AscendLearningFlow(
    concept: Concept,
    currentPhase: AscendPhase,
    onPhaseChange: (AscendPhase) -> Unit,
    onFinish: () -> Unit,
    onClose: () -> Unit
) {
    val theme = getAppThemeColors()

    val progressFraction = when (currentPhase) {
        AscendPhase.STEP_1_INVARIANT -> 0.25f
        AscendPhase.STEP_2_WHEN_TO_USE -> 0.50f
        AscendPhase.STEP_3_PREREQUISITES -> 0.75f
        AscendPhase.STEP_COMPLETE -> 1.0f
        else -> 0f
    }

    val stepLabel = when (currentPhase) {
        AscendPhase.STEP_1_INVARIANT -> "Step 1 of 3: Core Invariant"
        AscendPhase.STEP_2_WHEN_TO_USE -> "Step 2 of 3: When to Deploy"
        AscendPhase.STEP_3_PREREQUISITES -> "Step 3 of 3: Prerequisites"
        AscendPhase.STEP_COMPLETE -> "Pattern Absorbed"
        else -> ""
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(theme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Top Progress Bar
        Column {
            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "Close",
                    tint = theme.textMuted,
                    modifier = Modifier.size(26.dp).clickable { onClose() }
                )
                Spacer(modifier = Modifier.width(12.dp))
                LinearProgressIndicator(
                    progress = { progressFraction },
                    modifier = Modifier.weight(1f).height(12.dp).clip(RoundedCornerShape(6.dp)),
                    color = AscendGreen,
                    trackColor = theme.border
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(concept.name, color = theme.textPrimary, style = HelveticaSubtitle)
                Text(stepLabel, color = AscendGreen, style = HelveticaSmall)
            }
        }

        // Single-Purpose Step Content
        Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
            when (currentPhase) {
                AscendPhase.STEP_1_INVARIANT -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            DuoNinjaMascot(
                                mood = "FOCUS",
                                dialogText = "Here is the single core idea behind ${concept.name}."
                            )
                            Spacer(modifier = Modifier.height(20.dp))

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(theme.cardBackground)
                                    .border(2.dp, AscendGreen, RoundedCornerShape(20.dp))
                                    .padding(22.dp)
                            ) {
                                Column {
                                    Text("Core Invariant", color = AscendGreen, style = HelveticaSmall)
                                    Spacer(modifier = Modifier.height(10.dp))
                                    Text(concept.coreIdea, color = theme.textPrimary, style = HelveticaSubtitle)
                                }
                            }
                        }

                        Duo3DButton(
                            text = "Next: When to Deploy",
                            buttonColor = AscendGreen,
                            shadowColor = AscendGreenDark,
                            onClick = { onPhaseChange(AscendPhase.STEP_2_WHEN_TO_USE) }
                        )
                    }
                }

                AscendPhase.STEP_2_WHEN_TO_USE -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            DuoNinjaMascot(
                                mood = "HAPPY",
                                dialogText = "Spot these interview triggers to deploy ${concept.name}."
                            )
                            Spacer(modifier = Modifier.height(20.dp))

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(theme.cardBackground)
                                    .border(2.dp, AscendGreen, RoundedCornerShape(20.dp))
                                    .padding(22.dp)
                            ) {
                                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                    Text("When to Deploy", color = AscendGreen, style = HelveticaSmall)
                                    concept.whenToUse.forEach { bullet ->
                                        Text("• $bullet", color = theme.textPrimary, style = HelveticaBody)
                                    }
                                }
                            }
                        }

                        Duo3DButton(
                            text = "Next: Prerequisites",
                            buttonColor = AscendGreen,
                            shadowColor = AscendGreenDark,
                            onClick = { onPhaseChange(AscendPhase.STEP_3_PREREQUISITES) }
                        )
                    }
                }

                AscendPhase.STEP_3_PREREQUISITES -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            DuoNinjaMascot(
                                mood = "FOCUS",
                                dialogText = "Make sure you have these building blocks ready."
                            )
                            Spacer(modifier = Modifier.height(20.dp))

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(theme.cardBackground)
                                    .border(2.dp, AscendGreen, RoundedCornerShape(20.dp))
                                    .padding(22.dp)
                            ) {
                                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                    Text("Prerequisites", color = AscendGreen, style = HelveticaSmall)
                                    concept.prerequisites.forEach { prereq ->
                                        Text("• $prereq", color = theme.textPrimary, style = HelveticaSubtitle)
                                    }
                                }
                            }
                        }

                        Duo3DButton(
                            text = "Complete Pattern",
                            buttonColor = AscendGreen,
                            shadowColor = AscendGreenDark,
                            onClick = { onPhaseChange(AscendPhase.STEP_COMPLETE) }
                        )
                    }
                }

                AscendPhase.STEP_COMPLETE -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth().padding(top = 32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Filled.CheckCircle,
                                contentDescription = "Absorbed",
                                tint = AscendGreen,
                                modifier = Modifier.size(72.dp)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(concept.name, color = theme.textPrimary, style = HelveticaTitle)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                "Pattern absorbed. Ready to drill in Reels.",
                                color = theme.textSecondary,
                                style = HelveticaSubtitle
                            )
                        }

                        Duo3DButton(
                            text = "Drill in Reels",
                            buttonColor = AscendGreen,
                            shadowColor = AscendGreenDark,
                            onClick = onFinish
                        )
                    }
                }

                else -> {}
            }
        }
    }
}
