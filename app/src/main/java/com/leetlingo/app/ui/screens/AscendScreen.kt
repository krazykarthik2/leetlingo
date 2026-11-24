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
import androidx.compose.ui.text.font.FontWeight
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
    val topConcept = concepts.firstOrNull()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DuoPolar)
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("ASCEND", color = AscendGreen, fontWeight = FontWeight.Black, fontSize = 22.sp)
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(AscendGreenLight)
                        .border(2.dp, AscendGreen, RoundedCornerShape(12.dp))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text("NEW CONCEPT", color = AscendGreenDark, fontWeight = FontWeight.Black, fontSize = 11.sp)
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Ninja Mascot dialogue
            DuoNinjaMascot(
                mood = "HAPPY",
                dialogText = "Ready to ascend? Master this secret technique to unlock higher Katas!"
            )

            Spacer(modifier = Modifier.height(16.dp))

            topConcept?.let { concept ->
                // Clean White Card with Green Accents
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(AscendWhite)
                        .border(2.dp, DuoSwan, RoundedCornerShape(20.dp))
                        .padding(18.dp)
                ) {
                    Column {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(AscendGreenLight)
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(concept.topic.uppercase(), color = AscendGreenDark, fontSize = 11.sp, fontWeight = FontWeight.Black)
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                        Text(concept.name, color = DuoEel, fontSize = 22.sp, fontWeight = FontWeight.Black)
                        Text(concept.tagline, color = DuoWolf, fontSize = 13.sp, fontWeight = FontWeight.Medium)

                        Spacer(modifier = Modifier.height(14.dp))
                        Text("SECRET SCROLL IDEA:", color = AscendGreen, fontSize = 12.sp, fontWeight = FontWeight.Black)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(concept.coreIdea, color = DuoEel, fontSize = 13.sp, lineHeight = 18.sp, fontWeight = FontWeight.Medium)

                        Spacer(modifier = Modifier.height(14.dp))
                        Text("APPLY IN INTERVIEWS:", color = AscendGreen, fontSize = 12.sp, fontWeight = FontWeight.Black)
                        concept.whenToUse.forEach { bullet ->
                            Text("• $bullet", color = DuoWolf, fontSize = 12.sp, modifier = Modifier.padding(vertical = 1.dp))
                        }
                    }
                }
            }
        }

        // Single Green 3D Button (Ascend -> Green)
        Duo3DButton(
            text = "Absorb Pattern & Drill",
            buttonColor = AscendGreen,
            shadowColor = AscendGreenDark,
            onClick = { topConcept?.let { onAbsorbConcept(it) } }
        )
    }
}
