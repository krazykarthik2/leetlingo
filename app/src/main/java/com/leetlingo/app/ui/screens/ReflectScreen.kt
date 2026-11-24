package com.leetlingo.app.ui.screens

import androidx.compose.foundation.background
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
import com.leetlingo.app.ui.components.DuoChunkyButton
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
            .background(ReflectBackground)
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text("🪞 REFLECT", color = ReflectRed, fontWeight = FontWeight.Black, fontSize = 20.sp)
            Text("Weakness Radar & Diagnostic Analysis", color = Color(0xFF94A3B8), fontSize = 13.sp)

            Spacer(modifier = Modifier.height(20.dp))

            // Radar bars
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = ReflectCardBackground)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    profile.topics.forEach { topic ->
                        Column(modifier = Modifier.padding(vertical = 6.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = topic.name + if (topic.isBiggestBleed) " 🔴" else "",
                                    color = if (topic.isBiggestBleed) ReflectRed else Color.White,
                                    fontWeight = if (topic.isBiggestBleed) FontWeight.Bold else FontWeight.Normal,
                                    fontSize = 14.sp
                                )
                                Text(
                                    text = "${topic.masteryScore}% (${topic.won}/${topic.attempted})",
                                    color = Color(0xFF94A3B8),
                                    fontSize = 12.sp
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            LinearProgressIndicator(
                                progress = { topic.masteryScore / 100f },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(8.dp)
                                    .clip(RoundedCornerShape(4.dp)),
                                color = if (topic.isBiggestBleed) ReflectRed else Color(0xFF64748B),
                                trackColor = Color(0xFF1E293B),
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Diagnostic card
            biggestBleed?.let {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xFF2B0A0A))
                        .padding(16.dp)
                ) {
                    Column {
                        Text("CRITICAL GAP DETECTED", color = ReflectRed, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(it.name, color = Color.White, fontWeight = FontWeight.Black, fontSize = 18.sp)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(profile.summary, color = Color(0xFFE2E8F0), fontSize = 13.sp, lineHeight = 18.sp)
                    }
                }
            }
        }

        // Single primary Action
        DuoChunkyButton(
            text = "🎯 ATTACK BIGGEST GAP (${biggestBleed?.name ?: "DIAGNOSTIC"})",
            buttonColor = ReflectRed,
            onClick = {
                biggestBleed?.let { onAttackBleed(it.name) }
            }
        )
    }
}
