package com.leetlingo.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
    val theme = getAppThemeColors()
    val initialBleed = profile.topics.firstOrNull { it.isBiggestBleed } ?: profile.topics.firstOrNull()
    var selectedTopicName by remember { mutableStateOf(initialBleed?.name ?: "") }
    val activeTopic = profile.topics.firstOrNull { it.name == selectedTopicName } ?: initialBleed

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
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "${topic.won}/${topic.attempted} won  •  ",
                                        color = theme.textMuted,
                                        style = HelveticaSmall
                                    )
                                    Text(
                                        text = "${topic.masteryScore}%",
                                        color = if (topic.masteryScore < 50) ReflectRed else theme.textPrimary,
                                        style = HelveticaSmall
                                    )
                                }
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
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if (topic.isBiggestBleed) "Critical Gap: ${topic.name}" else "Diagnostic: ${topic.name}",
                                color = if (topic.isBiggestBleed) ReflectRed else theme.textPrimary,
                                style = HelveticaSmall
                            )
                            Text(
                                text = "${100 - topic.masteryScore}% Leak Rate",
                                color = if (topic.isBiggestBleed) ReflectRed else theme.textMuted,
                                style = HelveticaSmall
                            )
                        }
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
            onClick = { activeTopic?.let { onAttackBleed(it.name) } }
        )
    }
}
