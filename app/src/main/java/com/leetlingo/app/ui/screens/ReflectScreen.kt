package com.leetlingo.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Code
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leetlingo.app.data.model.WeaknessProfile
import com.leetlingo.app.ui.components.Duo3DButton
import com.leetlingo.app.ui.components.DuoNinjaMascot
import com.leetlingo.app.ui.components.DuoOptionCard
import com.leetlingo.app.ui.theme.*

enum class GapAttackPhase {
    OVERVIEW,
    PHASE_1_MENTAL_MODEL,
    PHASE_2_INVARIANT,
    PHASE_3_TRAP,
    PHASE_4_DRILL,
    PHASE_5_PATCH_CODE,
    PHASE_COMPLETE
}

data class TopicDrillContent(
    val mentalModel: String,
    val keyTrick: String,
    val commonTrap: String,
    val drillQuestion: String,
    val options: List<Triple<String, String, Boolean>>,
    val drillExplanation: String,
    val patchPrompt: String,
    val patchHint: String,
    val patchKeywords: List<String>,
    val pythonSnippet: String
)

fun getDrillContentForTopic(topicName: String): TopicDrillContent {
    return when {
        topicName.contains("Bit", ignoreCase = true) -> TopicDrillContent(
            mentalModel = "Treat integers as 32-bit arrays. Operations happen in O(1) across all bits simultaneously.",
            keyTrick = "x ^ x = 0 (cancels duplicates) and x & (x - 1) clears the lowest set bit.",
            commonTrap = "Forgetting operator precedence: == binds tighter than & in Python/Java/C++.",
            drillQuestion = "You have an array where every element appears twice except one. How do you find it in O(1) space?",
            options = listOf(
                Triple("Hash Set", "Store seen numbers and remove on second visit", false),
                Triple("XOR Accumulator", "XOR all elements together; pairs cancel to 0", true),
                Triple("Sort & Scan", "Sort array and check adjacent pairs", false),
                Triple("Prefix Sums", "Compare total sum with unique sum", false)
            ),
            drillExplanation = "XOR is associative and commutative: a ^ a = 0 and a ^ 0 = a, leaving only the single number.",
            patchPrompt = "Write pseudo-code to count the number of 1-bits (Hamming Weight) using Brian Kernighan's trick.",
            patchHint = "while n > 0: n = n & (n - 1), increment count",
            patchKeywords = listOf("while", "n - 1", "count"),
            pythonSnippet = "def hammingWeight(n: int) -> int:\n    count = 0\n    while n:\n        n &= (n - 1)\n        count += 1\n    return count"
        )
        topicName.contains("Dynamic", ignoreCase = true) -> TopicDrillContent(
            mentalModel = "Break a problem into overlapping subproblems and cache each state's optimal answer.",
            keyTrick = "Define state dp[i] clearly in plain English before writing any transition.",
            commonTrap = "Confusing top-down memoization base cases with bottom-up loop order.",
            drillQuestion = "In House Robber, you cannot rob two adjacent houses. What is the state transition for dp[i]?",
            options = listOf(
                Triple("dp[i-1] + nums[i]", "Always rob current and previous", false),
                Triple("max(dp[i-1], dp[i-2] + nums[i])", "Skip current or rob current + best from i-2", true),
                Triple("dp[i-2] + nums[i]", "Always rob every second house", false),
                Triple("sum(nums[0..i])", "Take all houses up to i", false)
            ),
            drillExplanation = "At house i, you either skip it (keep dp[i-1]) or rob it (add nums[i] to dp[i-2]).",
            patchPrompt = "Write pseudo-code for 1D DP space optimization using two rolling variables (prev1, prev2).",
            patchHint = "for num in nums: temp = max(prev1, prev2 + num); prev2 = prev1; prev1 = temp",
            patchKeywords = listOf("max", "prev", "for"),
            pythonSnippet = "def rob(nums: list[int]) -> int:\n    prev2, prev1 = 0, 0\n    for num in nums:\n        prev2, prev1 = prev1, max(prev1, prev2 + num)\n    return prev1"
        )
        else -> TopicDrillContent(
            mentalModel = "Identify the core invariant that shrinks the search space on every step.",
            keyTrick = "Track visited states or boundaries so each element is processed at most once.",
            commonTrap = "Mutating the state before checking termination or boundary conditions.",
            drillQuestion = "Which approach guarantees O(V + E) traversal without revisiting nodes in $topicName?",
            options = listOf(
                Triple("Brute Force Backtracking", "Explore all paths without visited set", false),
                Triple("Visited Set + Queue/Stack", "Mark nodes visited upon discovery", true),
                Triple("Nested Loops", "Check all pairs on every iteration", false),
                Triple("Random Walk", "Sample neighbors until target found", false)
            ),
            drillExplanation = "Marking nodes as visited when enqueuing/pushing prevents infinite loops and redundant work.",
            patchPrompt = "Write pseudo-code for a clean BFS loop using a queue and visited set.",
            patchHint = "while queue: pop node, for neighbor in graph[node]: if not visited, add and enqueue",
            patchKeywords = listOf("while", "visited", "queue"),
            pythonSnippet = "from collections import deque\ndef bfs(start, graph):\n    q, seen = deque([start]), {start}\n    while q:\n        node = q.popleft()\n        for nxt in graph[node]:\n            if nxt not in seen:\n                seen.add(nxt)\n                q.append(nxt)"
        )
    }
}

@Composable
fun ReflectScreen(
    profile: WeaknessProfile,
    onAttackBleed: (topicName: String) -> Unit
) {
    val theme = getAppThemeColors()
    val initialBleed = profile.topics.firstOrNull { it.isBiggestBleed } ?: profile.topics.firstOrNull()
    var selectedTopicName by remember { mutableStateOf(initialBleed?.name ?: "") }
    val activeTopic = profile.topics.firstOrNull { it.name == selectedTopicName } ?: initialBleed

    var currentPhase by remember { mutableStateOf(GapAttackPhase.OVERVIEW) }
    var patchedBoosts by remember { mutableStateOf<Map<String, Int>>(emptyMap()) }

    if (currentPhase != GapAttackPhase.OVERVIEW && activeTopic != null) {
        GapAttackPhaseFlow(
            topicName = activeTopic.name,
            currentPhase = currentPhase,
            onPhaseChange = { currentPhase = it },
            onCompletePatch = {
                val prev = patchedBoosts[activeTopic.name] ?: 0
                patchedBoosts = patchedBoosts + (activeTopic.name to (prev + 12))
                currentPhase = GapAttackPhase.OVERVIEW
            },
            onClose = { currentPhase = GapAttackPhase.OVERVIEW }
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
                Text("Reflect", color = ReflectRed, style = HelveticaTitle)
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(ReflectRedLight)
                        .border(2.dp, ReflectRed, RoundedCornerShape(12.dp))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text("Diagnostic", color = ReflectRed, style = HelveticaSmall)
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            DuoNinjaMascot(
                mood = "FOCUS",
                dialogText = "Weakness diagnostic complete. Tap any gap to inspect your failure patterns."
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Topic Mastery List with interactive selection
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(theme.cardBackground)
                    .border(2.dp, theme.border, RoundedCornerShape(20.dp))
                    .padding(16.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    profile.topics.forEach { topic ->
                        val isSelected = topic.name == selectedTopicName
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .clickable { selectedTopicName = topic.name }
                                .padding(vertical = 4.dp, horizontal = 2.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    if (topic.isBiggestBleed) {
                                        Icon(
                                            imageVector = Icons.Filled.Warning,
                                            contentDescription = "Bleed",
                                            tint = ReflectRed,
                                            modifier = Modifier.size(16.dp).padding(end = 4.dp)
                                        )
                                    }
                                    Text(
                                        text = topic.name,
                                        color = if (isSelected) ReflectRed else theme.textPrimary,
                                        style = HelveticaSubtitle
                                    )
                                }
                                Text(
                                    text = "${topic.masteryScore}%",
                                    color = if (topic.masteryScore < 50) ReflectRed else theme.textPrimary,
                                    style = HelveticaSmall
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            LinearProgressIndicator(
                                progress = { topic.masteryScore / 100f },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(10.dp)
                                    .clip(RoundedCornerShape(5.dp)),
                                color = if (topic.isBiggestBleed) ReflectRed else if (topic.masteryScore > 70) AscendGreen else VaultYellowDark,
                                trackColor = theme.surfaceHighlight
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Dynamic Gap Diagnostic Card
            activeTopic?.let { topic ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(if (topic.isBiggestBleed) ReflectRed.copy(alpha = 0.12f) else theme.surfaceHighlight)
                        .border(1.5.dp, if (topic.isBiggestBleed) ReflectRed else theme.border, RoundedCornerShape(16.dp))
                        .padding(14.dp)
                ) {
                    Column {
                        Text(
                            text = if (topic.isBiggestBleed) "Critical Gap: ${topic.name}" else "Diagnostic: ${topic.name}",
                            color = if (topic.isBiggestBleed) ReflectRed else theme.textPrimary,
                            style = HelveticaSmall
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = if (topic.isBiggestBleed) {
                                profile.summary
                            } else {
                                "Mastery score is currently at ${topic.masteryScore}%. Solve 3 targeted reels to patch this concept."
                            },
                            color = theme.textPrimary,
                            style = HelveticaBody
                        )
                    }
                }
            }
        }

        Duo3DButton(
            text = "Attack Gap: ${activeTopic?.name ?: "Drill"}",
            buttonColor = ReflectRed,
            shadowColor = ReflectRedDark,
            onClick = {
                if (activeTopic != null) {
                    currentPhase = GapAttackPhase.PHASE_1_MENTAL_MODEL
                }
            }
        )
    }
}

@Composable
fun GapAttackPhaseFlow(
    topicName: String,
    currentPhase: GapAttackPhase,
    onPhaseChange: (GapAttackPhase) -> Unit,
    onCompletePatch: () -> Unit,
    onClose: () -> Unit
) {
    val theme = getAppThemeColors()
    val drill = remember(topicName) { getDrillContentForTopic(topicName) }

    val progressFraction = when (currentPhase) {
        GapAttackPhase.PHASE_1_MENTAL_MODEL -> 0.17f
        GapAttackPhase.PHASE_2_INVARIANT -> 0.34f
        GapAttackPhase.PHASE_3_TRAP -> 0.50f
        GapAttackPhase.PHASE_4_DRILL -> 0.70f
        GapAttackPhase.PHASE_5_PATCH_CODE -> 0.88f
        GapAttackPhase.PHASE_COMPLETE -> 1.0f
        else -> 0f
    }

    val phaseLabel = when (currentPhase) {
        GapAttackPhase.PHASE_1_MENTAL_MODEL -> "Step 1 of 5: Mental Model"
        GapAttackPhase.PHASE_2_INVARIANT -> "Step 2 of 5: Invariant"
        GapAttackPhase.PHASE_3_TRAP -> "Step 3 of 5: Common Trap"
        GapAttackPhase.PHASE_4_DRILL -> "Step 4 of 5: Pattern Check"
        GapAttackPhase.PHASE_5_PATCH_CODE -> "Step 5 of 5: Patch Code"
        GapAttackPhase.PHASE_COMPLETE -> "Gap Patched"
        else -> ""
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(theme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Top Progress Header
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
                    color = ReflectRed,
                    trackColor = theme.border
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(topicName, color = theme.textPrimary, style = HelveticaSubtitle)
                Text(phaseLabel, color = ReflectRed, style = HelveticaSmall)
            }
        }

        // Phase Content
        Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
            when (currentPhase) {
                GapAttackPhase.PHASE_1_MENTAL_MODEL -> {
                    SingleConceptPhaseScreen(
                        mascotDialog = "Lock in the Core Mental Model for $topicName.",
                        label = "Core Mental Model",
                        labelColor = ReflectRed,
                        body = drill.mentalModel,
                        ctaText = "Next: Signature Invariant",
                        onNext = { onPhaseChange(GapAttackPhase.PHASE_2_INVARIANT) }
                    )
                }

                GapAttackPhase.PHASE_2_INVARIANT -> {
                    SingleConceptPhaseScreen(
                        mascotDialog = "Memorize this Signature Invariant.",
                        label = "Signature Invariant",
                        labelColor = AscendGreen,
                        body = drill.keyTrick,
                        ctaText = "Next: Common Trap",
                        onNext = { onPhaseChange(GapAttackPhase.PHASE_3_TRAP) }
                    )
                }

                GapAttackPhase.PHASE_3_TRAP -> {
                    SingleConceptPhaseScreen(
                        mascotDialog = "Watch out for this Common Interview Trap.",
                        label = "Common Interview Trap",
                        labelColor = VaultYellowDark,
                        body = drill.commonTrap,
                        ctaText = "Start Pattern Check",
                        onNext = { onPhaseChange(GapAttackPhase.PHASE_4_DRILL) }
                    )
                }

                GapAttackPhase.PHASE_4_DRILL -> {
                    var selectedIdx by remember { mutableStateOf<Int?>(null) }
                    var checked by remember { mutableStateOf(false) }

                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            DuoNinjaMascot(
                                mood = "FOCUS",
                                dialogText = drill.drillQuestion
                            )
                            Spacer(modifier = Modifier.height(14.dp))

                            drill.options.forEachIndexed { idx, opt ->
                                val isSelected = selectedIdx == idx
                                val statusColor = if (checked && isSelected) {
                                    if (opt.third) AscendGreen else ReflectRed
                                } else null

                                DuoOptionCard(
                                    indexNumber = idx + 1,
                                    title = opt.first,
                                    description = opt.second,
                                    isSelected = isSelected,
                                    statusColor = statusColor,
                                    onClick = { if (!checked) selectedIdx = idx }
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }

                        Column {
                            if (checked && selectedIdx != null) {
                                val isCorrect = drill.options[selectedIdx!!].third
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(if (isCorrect) AscendGreen.copy(alpha = 0.15f) else ReflectRed.copy(alpha = 0.15f))
                                        .padding(14.dp)
                                ) {
                                    Column {
                                        Text(
                                            if (isCorrect) "Cracked Pattern" else "Missed Pattern",
                                            color = if (isCorrect) AscendGreen else ReflectRed,
                                            style = HelveticaSubtitle
                                        )
                                        Text(drill.drillExplanation, color = theme.textPrimary, style = HelveticaBody)
                                    }
                                }
                                Spacer(modifier = Modifier.height(10.dp))
                            }

                            if (!checked) {
                                Duo3DButton(
                                    text = "Check Pattern",
                                    buttonColor = ReflectRed,
                                    shadowColor = ReflectRedDark,
                                    enabled = selectedIdx != null,
                                    onClick = { checked = true }
                                )
                            } else {
                                val isCorrect = drill.options[selectedIdx!!].third
                                Duo3DButton(
                                    text = if (isCorrect) "Continue to Patch Code" else "Try Again",
                                    buttonColor = if (isCorrect) AscendGreen else ReflectRed,
                                    shadowColor = if (isCorrect) AscendGreenDark else ReflectRedDark,
                                    onClick = {
                                        if (isCorrect) {
                                            onPhaseChange(GapAttackPhase.PHASE_5_PATCH_CODE)
                                        } else {
                                            checked = false
                                            selectedIdx = null
                                        }
                                    }
                                )
                            }
                        }
                    }
                }

                GapAttackPhase.PHASE_5_PATCH_CODE -> {
                    var pseudoInput by remember { mutableStateOf("") }
                    var feedback by remember { mutableStateOf<String?>(null) }

                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            DuoNinjaMascot(
                                mood = "HAPPY",
                                dialogText = drill.patchPrompt
                            )
                            Spacer(modifier = Modifier.height(12.dp))

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(170.dp)
                                    .clip(RoundedCornerShape(18.dp))
                                    .background(theme.cardBackground)
                                    .border(2.dp, theme.border, RoundedCornerShape(18.dp))
                                    .padding(12.dp)
                            ) {
                                TextField(
                                    value = pseudoInput,
                                    onValueChange = { pseudoInput = it },
                                    modifier = Modifier.fillMaxSize(),
                                    placeholder = { Text("Draft your logic here...", color = theme.textMuted) },
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

                            Spacer(modifier = Modifier.height(10.dp))

                            // Quick-fill hint chip
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(theme.surfaceHighlight)
                                    .border(1.dp, theme.border, RoundedCornerShape(10.dp))
                                    .clickable { pseudoInput = drill.patchHint }
                                    .padding(horizontal = 12.dp, vertical = 8.dp)
                            ) {
                                Text("Tap to autofill hint", color = theme.textSecondary, style = HelveticaSmall)
                            }

                            feedback?.let {
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(it, color = VaultYellowDark, style = HelveticaSmall)
                            }
                        }

                        Duo3DButton(
                            text = "Patch Leak",
                            buttonColor = ReflectRed,
                            shadowColor = ReflectRedDark,
                            enabled = pseudoInput.isNotBlank(),
                            onClick = {
                                val lower = pseudoInput.lowercase()
                                val hits = drill.patchKeywords.count { lower.contains(it) }
                                if (hits >= 2 || pseudoInput.length > 15) {
                                    onPhaseChange(GapAttackPhase.PHASE_COMPLETE)
                                } else {
                                    feedback = "Hint: ${drill.patchHint}"
                                }
                            }
                        )
                    }
                }

                GapAttackPhase.PHASE_COMPLETE -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            DuoNinjaMascot(
                                mood = "HAPPY",
                                dialogText = "Leak patched! You unlocked the clean Python implementation for $topicName."
                            )
                            Spacer(modifier = Modifier.height(16.dp))

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(theme.cardBackground)
                                    .border(2.dp, AscendGreen, RoundedCornerShape(20.dp))
                                    .padding(16.dp)
                            ) {
                                Column {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text("Python 3", color = AscendGreen, style = HelveticaSmall)
                                        Text("+12% Mastery", color = AscendGreen, style = HelveticaSmall)
                                    }
                                    Spacer(modifier = Modifier.height(10.dp))
                                    Text(
                                        text = drill.pythonSnippet,
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
                            text = "Finish & Return",
                            buttonColor = AscendGreen,
                            shadowColor = AscendGreenDark,
                            onClick = onCompletePatch
                        )
                    }
                }

                else -> {}
            }
        }
    }
}

@Composable
fun SingleConceptPhaseScreen(
    mascotDialog: String,
    label: String,
    labelColor: Color,
    body: String,
    ctaText: String,
    onNext: () -> Unit
) {
    val theme = getAppThemeColors()
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            DuoNinjaMascot(
                mood = "FOCUS",
                dialogText = mascotDialog
            )
            Spacer(modifier = Modifier.height(20.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(theme.cardBackground)
                    .border(2.dp, labelColor, RoundedCornerShape(20.dp))
                    .padding(22.dp)
            ) {
                Column {
                    Text(label, color = labelColor, style = HelveticaSmall)
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(body, color = theme.textPrimary, style = HelveticaSubtitle)
                }
            }
        }

        Duo3DButton(
            text = ctaText,
            buttonColor = ReflectRed,
            shadowColor = ReflectRedDark,
            onClick = onNext
        )
    }
}
