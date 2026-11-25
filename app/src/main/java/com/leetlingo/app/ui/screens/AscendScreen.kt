package com.leetlingo.app.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
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
import com.leetlingo.app.data.model.Concept
import com.leetlingo.app.ui.components.Duo3DButton
import com.leetlingo.app.ui.components.DuoNinjaMascot
import com.leetlingo.app.ui.components.DuoOptionCard
import com.leetlingo.app.ui.theme.*

enum class VisualType {
    MONOTONIC_STACK,
    SLIDING_WINDOW,
    TRIE_TREE,
    TOPO_SORT
}

sealed class AscendSlide {
    data class ConceptCard(
        val label: String,
        val mascotText: String,
        val body: String
    ) : AscendSlide()

    data class AlgoStepVisual(
        val label: String,
        val mascotText: String,
        val visualType: VisualType
    ) : AscendSlide()

    data class ComplexityCard(
        val label: String,
        val mascotText: String,
        val timeComplexity: String,
        val spaceComplexity: String,
        val explanation: String
    ) : AscendSlide()

    data class QuickCheck(
        val question: String,
        val options: List<Pair<String, Boolean>>,
        val explanation: String
    ) : AscendSlide()
}

fun buildSlidesForConcept(concept: Concept): List<AscendSlide> {
    return when {
        concept.name.contains("Monotonic", ignoreCase = true) -> listOf(
            AscendSlide.ConceptCard(
                label = "Core Invariant",
                mascotText = "A Monotonic Stack keeps elements strictly sorted.",
                body = concept.coreIdea
            ),
            AscendSlide.AlgoStepVisual(
                label = "Live Algorithm: Next Greater",
                mascotText = "Tap Step to watch elements push and pop when a larger number arrives.",
                visualType = VisualType.MONOTONIC_STACK
            ),
            AscendSlide.ConceptCard(
                label = "Why It Works",
                mascotText = "Notice how popped elements never return.",
                body = "When a larger element arrives, it immediately resolves the 'Next Greater' answer for every smaller element waiting on top of the stack."
            ),
            AscendSlide.ComplexityCard(
                label = "Amortized Complexity",
                mascotText = "Even with a nested while-loop, it is linear!",
                timeComplexity = "O(n)",
                spaceComplexity = "O(n)",
                explanation = "Each element is pushed onto the stack at most once and popped at most once."
            ),
            AscendSlide.QuickCheck(
                question = "In a decreasing Monotonic Stack, what happens when a LARGER element arrives?",
                options = listOf(
                    "Pop smaller elements until top >= incoming" to true,
                    "Clear the entire stack immediately" to false,
                    "Ignore the incoming element" to false,
                    "Sort the stack from scratch" to false
                ),
                explanation = "We pop all smaller elements so the stack maintains its decreasing order."
            )
        )

        concept.name.contains("Sliding", ignoreCase = true) -> listOf(
            AscendSlide.ConceptCard(
                label = "Core Invariant",
                mascotText = "Slide a window [L..R] without recomputing from scratch.",
                body = concept.coreIdea
            ),
            AscendSlide.AlgoStepVisual(
                label = "Live Algorithm: Window Glide",
                mascotText = "Tap Step to glide the window and track the maximum.",
                visualType = VisualType.SLIDING_WINDOW
            ),
            AscendSlide.ComplexityCard(
                label = "Window Complexity",
                mascotText = "Zero redundant scans.",
                timeComplexity = "O(n)",
                spaceComplexity = "O(k)",
                explanation = "Each index enters and leaves the deque at most once, giving O(1) amortized work per step."
            ),
            AscendSlide.QuickCheck(
                question = "Why do we store INDICES instead of values in a Sliding Window Deque?",
                options = listOf(
                    "To evict elements that fall outside the left boundary" to true,
                    "Because values cannot be compared" to false,
                    "To use less memory than integers" to false,
                    "To sort the window in O(1)" to false
                ),
                explanation = "Indices let us check if front <= i - k and evict expired elements."
            )
        )

        concept.name.contains("Trie", ignoreCase = true) -> listOf(
            AscendSlide.ConceptCard(
                label = "Core Invariant",
                mascotText = "Share prefixes so dictionary lookups take O(L) time.",
                body = concept.coreIdea
            ),
            AscendSlide.AlgoStepVisual(
                label = "Live Algorithm: Prefix Tree",
                mascotText = "Tap Step to trace 'cat' and 'car' sharing prefix 'ca'.",
                visualType = VisualType.TRIE_TREE
            ),
            AscendSlide.ComplexityCard(
                label = "Lookup Complexity",
                mascotText = "Independent of total words stored!",
                timeComplexity = "O(L)",
                spaceComplexity = "O(N * L)",
                explanation = "L is the word length. Searching 10 words or 10 million words takes the exact same L steps."
            )
        )

        else -> listOf(
            AscendSlide.ConceptCard(
                label = "Core Invariant",
                mascotText = "Process nodes with zero dependencies first.",
                body = concept.coreIdea
            ),
            AscendSlide.AlgoStepVisual(
                label = "Live Algorithm: Kahn's BFS",
                mascotText = "Tap Step to peel off zero in-degree nodes.",
                visualType = VisualType.TOPO_SORT
            ),
            AscendSlide.ComplexityCard(
                label = "Graph Complexity",
                mascotText = "Linear in vertices + edges.",
                timeComplexity = "O(V + E)",
                spaceComplexity = "O(V + E)",
                explanation = "Every vertex is enqueued once and every edge is relaxed once."
            )
        )
    }
}

@Composable
fun AscendScreen(
    concepts: List<Concept>,
    onAbsorbConcept: (Concept) -> Unit
) {
    val theme = getAppThemeColors()
    val currentConcept = remember(concepts) { concepts.randomOrNull() }
    var inLesson by remember { mutableStateOf(false) }

    if (inLesson && currentConcept != null) {
        val slides = remember(currentConcept.id) { buildSlidesForConcept(currentConcept) }
        AscendDynamicLessonFlow(
            concept = currentConcept,
            slides = slides,
            onFinish = {
                inLesson = false
                onAbsorbConcept(currentConcept)
            },
            onClose = { inLesson = false }
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
                dialogText = "Today's pattern is locked in. Learn the algorithm step-by-step."
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
                    inLesson = true
                }
            }
        )
    }
}

@Composable
fun AscendDynamicLessonFlow(
    concept: Concept,
    slides: List<AscendSlide>,
    onFinish: () -> Unit,
    onClose: () -> Unit
) {
    val theme = getAppThemeColors()
    var slideIndex by remember { mutableIntStateOf(0) }
    val isComplete = slideIndex >= slides.size
    val progressFraction = if (isComplete) 1f else (slideIndex + 1f) / (slides.size + 1f)

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
                Text(
                    text = if (isComplete) "Complete" else "Step ${slideIndex + 1} of ${slides.size}",
                    color = AscendGreen,
                    style = HelveticaSmall
                )
            }
        }

        // Dynamic Slide Content
        Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
            if (isComplete) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(top = 36.dp),
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
                            "Algorithm mastered. Ready to drill in Reels.",
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
            } else {
                when (val slide = slides[slideIndex]) {
                    is AscendSlide.ConceptCard -> {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                DuoNinjaMascot(mood = "FOCUS", dialogText = slide.mascotText)
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
                                        Text(slide.label, color = AscendGreen, style = HelveticaSmall)
                                        Spacer(modifier = Modifier.height(10.dp))
                                        Text(slide.body, color = theme.textPrimary, style = HelveticaSubtitle)
                                    }
                                }
                            }
                            Duo3DButton(
                                text = "Continue",
                                buttonColor = AscendGreen,
                                shadowColor = AscendGreenDark,
                                onClick = { slideIndex++ }
                            )
                        }
                    }

                    is AscendSlide.AlgoStepVisual -> {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                DuoNinjaMascot(mood = "HAPPY", dialogText = slide.mascotText)
                                Spacer(modifier = Modifier.height(14.dp))
                                InteractiveAlgoVisualizer(visualType = slide.visualType)
                            }
                            Duo3DButton(
                                text = "Got It, Next",
                                buttonColor = AscendGreen,
                                shadowColor = AscendGreenDark,
                                onClick = { slideIndex++ }
                            )
                        }
                    }

                    is AscendSlide.ComplexityCard -> {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                DuoNinjaMascot(mood = "FOCUS", dialogText = slide.mascotText)
                                Spacer(modifier = Modifier.height(20.dp))
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(20.dp))
                                        .background(theme.cardBackground)
                                        .border(2.dp, AscendGreen, RoundedCornerShape(20.dp))
                                        .padding(22.dp)
                                ) {
                                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                        Text(slide.label, color = AscendGreen, style = HelveticaSmall)
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Column {
                                                Text("Time", color = theme.textMuted, style = HelveticaSmall)
                                                Text(slide.timeComplexity, color = AscendGreen, style = HelveticaTitle)
                                            }
                                            Column {
                                                Text("Space", color = theme.textMuted, style = HelveticaSmall)
                                                Text(slide.spaceComplexity, color = ReelBlue, style = HelveticaTitle)
                                            }
                                        }
                                        HorizontalDivider(color = theme.border)
                                        Text(slide.explanation, color = theme.textPrimary, style = HelveticaBody)
                                    }
                                }
                            }
                            Duo3DButton(
                                text = "Continue",
                                buttonColor = AscendGreen,
                                shadowColor = AscendGreenDark,
                                onClick = { slideIndex++ }
                            )
                        }
                    }

                    is AscendSlide.QuickCheck -> {
                        var selectedIdx by remember(slideIndex) { mutableStateOf<Int?>(null) }
                        var checked by remember(slideIndex) { mutableStateOf(false) }

                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                DuoNinjaMascot(mood = "FOCUS", dialogText = slide.question)
                                Spacer(modifier = Modifier.height(14.dp))
                                slide.options.forEachIndexed { idx, opt ->
                                    val isSelected = selectedIdx == idx
                                    val statusCol = if (checked && isSelected) {
                                        if (opt.second) AscendGreen else ReflectRed
                                    } else null
                                    DuoOptionCard(
                                        indexNumber = idx + 1,
                                        title = opt.first,
                                        description = "",
                                        isSelected = isSelected,
                                        statusColor = statusCol,
                                        onClick = { if (!checked) selectedIdx = idx }
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                }
                            }

                            Column {
                                if (checked && selectedIdx != null) {
                                    val isCorrect = slide.options[selectedIdx!!].second
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(14.dp))
                                            .background(if (isCorrect) AscendGreen.copy(alpha = 0.15f) else ReflectRed.copy(alpha = 0.15f))
                                            .padding(12.dp)
                                    ) {
                                        Text(slide.explanation, color = theme.textPrimary, style = HelveticaBody)
                                    }
                                    Spacer(modifier = Modifier.height(10.dp))
                                }

                                if (!checked) {
                                    Duo3DButton(
                                        text = "Check",
                                        buttonColor = AscendGreen,
                                        shadowColor = AscendGreenDark,
                                        enabled = selectedIdx != null,
                                        onClick = { checked = true }
                                    )
                                } else {
                                    val isCorrect = slide.options[selectedIdx!!].second
                                    Duo3DButton(
                                        text = if (isCorrect) "Continue" else "Try Again",
                                        buttonColor = if (isCorrect) AscendGreen else ReflectRed,
                                        shadowColor = if (isCorrect) AscendGreenDark else ReflectRedDark,
                                        onClick = {
                                            if (isCorrect) slideIndex++ else {
                                                checked = false
                                                selectedIdx = null
                                            }
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun InteractiveAlgoVisualizer(visualType: VisualType) {
    val theme = getAppThemeColors()
    var step by remember(visualType) { mutableIntStateOf(0) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(theme.cardBackground)
            .border(2.dp, AscendGreen, RoundedCornerShape(20.dp))
            .padding(16.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            when (visualType) {
                VisualType.MONOTONIC_STACK -> {
                    val nums = listOf(2, 1, 5, 6, 2, 3)
                    val stackStates = listOf(
                        listOf(2) to "Push 2",
                        listOf(2, 1) to "1 < 2: Push 1",
                        listOf(5) to "5 > 1 & 5 > 2: Pop 1, Pop 2 -> Push 5",
                        listOf(5, 6) to "6 > 5: Push 6? Wait: 6 > 5 so Pop 5 -> [6]",
                        listOf(6, 2) to "2 < 6: Push 2",
                        listOf(6, 3) to "3 > 2: Pop 2 -> Push 3"
                    )
                    val idx = step.coerceIn(0, nums.lastIndex)
                    val (stack, action) = stackStates[idx]

                    Text("Array", color = theme.textMuted, style = HelveticaSmall)
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        nums.forEachIndexed { i, v ->
                            val isActive = i == idx
                            val bg by animateColorAsState(if (isActive) AscendGreen else theme.surfaceHighlight, label = "arr")
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(bg)
                                    .border(1.5.dp, if (isActive) AscendGreenDark else theme.border, RoundedCornerShape(10.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("$v", color = if (isActive) Color.White else theme.textPrimary, style = HelveticaSubtitle)
                            }
                        }
                    }

                    Text("Stack State", color = theme.textMuted, style = HelveticaSmall)
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        stack.forEach { v ->
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(ReelBlue.copy(alpha = 0.2f))
                                    .border(1.5.dp, ReelBlue, RoundedCornerShape(8.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("$v", color = ReelBlue, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                    Text(action, color = AscendGreen, style = HelveticaSmall)
                }

                VisualType.SLIDING_WINDOW -> {
                    val nums = listOf(1, 3, -1, -3, 5, 3, 6, 7)
                    val k = 3
                    val maxStep = nums.size - k
                    val l = step.coerceIn(0, maxStep)
                    val r = l + k - 1
                    val windowMax = nums.subList(l, r + 1).max()

                    Text("Window [L=$l .. R=$r]  •  Max = $windowMax", color = AscendGreen, style = HelveticaSmall)
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        nums.forEachIndexed { i, v ->
                            val inWindow = i in l..r
                            val bg by animateColorAsState(if (inWindow) AscendGreen.copy(alpha = 0.25f) else theme.surfaceHighlight, label = "win")
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(bg)
                                    .border(1.5.dp, if (inWindow) AscendGreen else theme.border, RoundedCornerShape(8.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("$v", color = theme.textPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                VisualType.TRIE_TREE -> {
                    val stages = listOf(
                        "Insert 'cat': root -> c",
                        "Insert 'cat': root -> c -> a",
                        "Insert 'cat': root -> c -> a -> t*",
                        "Insert 'car': reuse c -> a -> r*"
                    )
                    val s = step.coerceIn(0, stages.lastIndex)
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
                        listOf("root", "c", "a", if (s >= 3) "t* / r*" else "t*").forEachIndexed { i, node ->
                            val active = i <= s
                            Box(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(if (active) AscendGreen else theme.surfaceHighlight)
                                    .padding(horizontal = 12.dp, vertical = 8.dp)
                            ) {
                                Text(node, color = if (active) Color.White else theme.textMuted, style = HelveticaSmall)
                            }
                        }
                    }
                    Text(stages[s], color = AscendGreen, style = HelveticaSmall)
                }

                VisualType.TOPO_SORT -> {
                    val stages = listOf(
                        "In-degrees: A(0), B(1), C(1) -> Queue: [A]",
                        "Pop A -> Decrement B(0), C(0) -> Queue: [B, C]",
                        "Pop B, C -> Order: [A, B, C]"
                    )
                    val s = step.coerceIn(0, stages.lastIndex)
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        listOf("A", "B", "C").forEachIndexed { i, n ->
                            val done = (s == 1 && i == 0) || s == 2
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(if (done) AscendGreen else theme.surfaceHighlight)
                                    .border(1.5.dp, if (done) AscendGreenDark else theme.border, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(n, color = if (done) Color.White else theme.textPrimary, style = HelveticaSubtitle)
                            }
                        }
                    }
                    Text(stages[s], color = AscendGreen, style = HelveticaSmall)
                }
            }

            // Visualizer Controls (Step / Reset)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .background(AscendGreen.copy(alpha = 0.15f))
                        .clickable { step++ }
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.PlayArrow, contentDescription = "Step", tint = AscendGreen, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Step Forward", color = AscendGreen, style = HelveticaSmall)
                    }
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(theme.surfaceHighlight)
                        .clickable { step = 0 }
                        .padding(horizontal = 14.dp, vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Filled.Refresh, contentDescription = "Reset", tint = theme.textMuted, modifier = Modifier.size(18.dp))
                }
            }
        }
    }
}
