package com.leetlingo.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leetlingo.app.data.model.Concept
import com.leetlingo.app.ui.components.DuoChunkyButton
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
            .background(AscendBackground)
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text("🚀 ASCEND", color = AscendGreen, fontWeight = FontWeight.Black, fontSize = 20.sp)
            Text("ML-Suggested Frontier Concept", color = Color(0xFF94A3B8), fontSize = 13.sp)

            Spacer(modifier = Modifier.height(24.dp))

            topConcept?.let { concept ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = AscendCardBackground),
                    border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(AscendGreen.copy(alpha = 0.5f)))
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(AscendGreen.copy(alpha = 0.2f))
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(concept.topic, color = AscendMintGlow, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                        Text(concept.name, color = AscendWhiteText, fontSize = 26.sp, fontWeight = FontWeight.Black)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(concept.tagline, color = Color(0xFFA7F3D0), fontSize = 14.sp, fontWeight = FontWeight.Medium)

                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Core Pattern:", color = Color(0xFF6EE7B7), fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(concept.coreIdea, color = Color(0xFFE2E8F0), fontSize = 14.sp, lineHeight = 20.sp)

                        Spacer(modifier = Modifier.height(16.dp))
                        Text("When to apply in interviews:", color = Color(0xFF6EE7B7), fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        concept.whenToUse.forEach { bullet ->
                            Text("• $bullet", color = Color(0xFFCBD5E1), fontSize = 13.sp, modifier = Modifier.padding(vertical = 2.dp))
                        }
                    }
                }
            }
        }

        // Single primary Action
        DuoChunkyButton(
            text = "⚡ ABSORB PATTERN & DRILL",
            buttonColor = AscendGreen,
            onClick = { topConcept?.let { onAbsorbConcept(it) } }
        )
    }
}
