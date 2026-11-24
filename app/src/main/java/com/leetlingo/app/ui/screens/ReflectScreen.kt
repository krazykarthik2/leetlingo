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
import com.leetlingo.app.data.model.WeaknessProfile
import com.leetlingo.app.ui.components.Duo3DButton
import com.leetlingo.app.ui.components.DuoNinjaMascot
import com.leetlingo.app.ui.theme.*

@Composable
fun ReflectScreen(
    profile: WeaknessProfile,
    onAttackBleed: (topicName: String) -> Unit
) {
    val biggestBleed = profile.topics.firstOrNull { it.isBiggestBleed } ?: profile.topics.firstOrNull()

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
                Text("REFLECT", color = ReflectRed, fontWeight = FontWeight.Black, fontSize = 22.sp)
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(ReflectRedLight)
                        .border(2.dp, ReflectRed, RoundedCornerShape(12.dp))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text("DIAGNOSTIC", color = ReflectRed, fontWeight = FontWeight.Black, fontSize = 11.sp)
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Ninja Mascot dialogue
            DuoNinjaMascot(
                mood = "FOCUS",
                dialogText = "Here are your combat weaknesses. Sharpen your blade on your biggest gap!"
            )

            Spacer(modifier = Modifier.height(16.dp))

            // White Card with progress bars
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(DuoSnow)
                    .border(2.dp, DuoSwan, RoundedCornerShape(20.dp))
                    .padding(16.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    profile.topics.forEach { topic ->
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = topic.name + if (topic.isBiggestBleed) " ⚠️" else "",
                                    color = if (topic.isBiggestBleed) ReflectRed else DuoEel,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                                Text(
                                    text = "${topic.masteryScore}%",
                                    color = DuoWolf,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            LinearProgressIndicator(
                                progress = { topic.masteryScore / 100f },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(10.dp)
                                    .clip(RoundedCornerShape(5.dp)),
                                color = if (topic.isBiggestBleed) ReflectRed else DuoWolf,
                                trackColor = DuoPolar
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Critical Callout Box
            biggestBleed?.let {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(ReflectRedLight)
                        .border(2.dp, ReflectRed, RoundedCornerShape(16.dp))
                        .padding(14.dp)
                ) {
                    Column {
                        Text("CRITICAL GAP: ${it.name.uppercase()}", color = ReflectRed, fontWeight = FontWeight.Black, fontSize = 13.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(profile.summary, color = DuoEel, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                    }
                }
            }
        }

        // Single Red 3D Button (Reflect -> Red)
        Duo3DButton(
            text = "Attack Gap: ${biggestBleed?.name ?: "Drill"}",
            buttonColor = ReflectRed,
            shadowColor = ReflectRedDark,
            onClick = {
                biggestBleed?.let { onAttackBleed(it.name) }
            }
        )
    }
}
