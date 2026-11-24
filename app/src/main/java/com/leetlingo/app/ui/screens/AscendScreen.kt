package com.leetlingo.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
    val topConcept = concepts.firstOrNull()

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
                    Text("New Concept", color = AscendGreenDark, style = HelveticaSmall)
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            DuoNinjaMascot(
                mood = "HAPPY",
                dialogText = "Ready to ascend? Learn this advanced technique to conquer unseen katas!"
            )

            Spacer(modifier = Modifier.height(16.dp))

            topConcept?.let { concept ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(theme.cardBackground)
                        .border(2.dp, theme.border, RoundedCornerShape(20.dp))
                        .padding(18.dp)
                ) {
                    Column {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(AscendGreen.copy(alpha = 0.15f))
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(concept.topic.replaceFirstChar { it.uppercase() }, color = AscendGreen, style = HelveticaSmall)
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                        Text(concept.name, color = theme.textPrimary, style = HelveticaTitle)
                        Text(concept.tagline, color = theme.textSecondary, style = HelveticaSubtitle)

                        Spacer(modifier = Modifier.height(14.dp))
                        Text("Secret Pattern", color = AscendGreen, style = HelveticaSmall)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(concept.coreIdea, color = theme.textPrimary, style = HelveticaBody)

                        Spacer(modifier = Modifier.height(14.dp))
                        Text("Apply in Interviews", color = AscendGreen, style = HelveticaSmall)
                        concept.whenToUse.forEach { bullet ->
                            Text("• $bullet", color = theme.textSecondary, style = HelveticaBody, modifier = Modifier.padding(vertical = 1.dp))
                        }
                    }
                }
            }
        }

        Duo3DButton(
            text = "Absorb Pattern & Drill",
            buttonColor = AscendGreen,
            shadowColor = AscendGreenDark,
            onClick = { topConcept?.let { onAbsorbConcept(it) } }
        )
    }
}
