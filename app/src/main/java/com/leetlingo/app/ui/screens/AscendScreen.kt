package com.leetlingo.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leetlingo.app.data.model.Concept
import com.leetlingo.app.ui.components.Duo3DButton
import com.leetlingo.app.ui.components.DuoNinjaMascot
import com.leetlingo.app.ui.theme.*

@Composable
fun AscendScreen(
    concepts: List<Concept>,
    onAbsorbConcept: (Concept) -> Unit
) {
    val theme = getAppThemeColors()
    var selectedIndex by remember { mutableIntStateOf(0) }
    val currentConcept = concepts.getOrNull(selectedIndex) ?: concepts.firstOrNull()

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
                    Text("${concepts.size} Patterns Available", color = AscendGreenDark, style = HelveticaSmall)
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Selector Chips to navigate all available concept masterclasses
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                concepts.forEachIndexed { idx, c ->
                    val isSelected = idx == selectedIndex
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isSelected) AscendGreen else theme.cardBackground)
                            .border(1.5.dp, if (isSelected) AscendGreenDark else theme.border, RoundedCornerShape(12.dp))
                            .clickable { selectedIndex = idx }
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = c.name,
                            color = if (isSelected) Color.White else theme.textSecondary,
                            style = HelveticaSmall
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            DuoNinjaMascot(
                mood = "HAPPY",
                dialogText = "Deep pattern comprehension unlocks unseen interview katas with zero panic."
            )

            Spacer(modifier = Modifier.height(14.dp))

            currentConcept?.let { concept ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(theme.cardBackground)
                        .border(2.dp, theme.border, RoundedCornerShape(20.dp))
                        .padding(18.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(AscendGreen.copy(alpha = 0.15f))
                                    .padding(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Text(concept.topic.replaceFirstChar { it.uppercase() }, color = AscendGreen, style = HelveticaSmall)
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Filled.Lightbulb,
                                    contentDescription = "Core Concept",
                                    tint = VaultYellow,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("High Yield", color = theme.textMuted, style = HelveticaSmall)
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                        Text(concept.name, color = theme.textPrimary, style = HelveticaTitle)
                        Text(concept.tagline, color = theme.textSecondary, style = HelveticaSubtitle)

                        Spacer(modifier = Modifier.height(14.dp))
                        Text("Core Invariant", color = AscendGreen, style = HelveticaSmall)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(concept.coreIdea, color = theme.textPrimary, style = HelveticaBody)

                        Spacer(modifier = Modifier.height(14.dp))
                        Text("When to Deploy in Interviews", color = AscendGreen, style = HelveticaSmall)
                        Spacer(modifier = Modifier.height(4.dp))
                        concept.whenToUse.forEach { bullet ->
                            Text("• $bullet", color = theme.textSecondary, style = HelveticaBody, modifier = Modifier.padding(vertical = 1.dp))
                        }

                        if (concept.prerequisites.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(12.dp))
                            Text("Prerequisites", color = theme.textMuted, style = HelveticaSmall)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(concept.prerequisites.joinToString("  •  "), color = theme.textSecondary, style = HelveticaSmall)
                        }
                    }
                }
            }
        }

        Duo3DButton(
            text = "Absorb Pattern & Drill",
            buttonColor = AscendGreen,
            shadowColor = AscendGreenDark,
            onClick = { currentConcept?.let { onAbsorbConcept(it) } }
        )
    }
}
