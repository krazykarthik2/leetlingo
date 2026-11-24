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
    val biggestBleed = profile.topics.firstOrNull { it.isBiggestBleed } ?: profile.topics.firstOrNull()

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
                Text("REFLECT", color = ReflectRed, style = HelveticaTitle)
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(ReflectRedLight)
                        .border(2.dp, ReflectRed, RoundedCornerShape(12.dp))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text("DIAGNOSTIC", color = ReflectRed, style = HelveticaSmall)
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            DuoNinjaMascot(
                mood = "FOCUS",
                dialogText = "Weakness diagnostic complete. Focus on your biggest gap to patch the leak!"
            )

            Spacer(modifier = Modifier.height(16.dp))

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
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = topic.name + if (topic.isBiggestBleed) " ⚠️" else "",
                                    color = if (topic.isBiggestBleed) ReflectRed else theme.textPrimary,
                                    style = HelveticaSubtitle
                                )
                                Text(
                                    text = "${topic.masteryScore}%",
                                    color = theme.textMuted,
                                    style = HelveticaSmall
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            LinearProgressIndicator(
                                progress = { topic.masteryScore / 100f },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(10.dp)
                                    .clip(RoundedCornerShape(5.dp)),
                                color = if (topic.isBiggestBleed) ReflectRed else theme.textMuted,
                                trackColor = theme.surfaceHighlight
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            biggestBleed?.let {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(ReflectRed.copy(alpha = 0.15f))
                        .border(2.dp, ReflectRed, RoundedCornerShape(16.dp))
                        .padding(14.dp)
                ) {
                    Column {
                        Text("CRITICAL GAP: ${it.name.uppercase()}", color = ReflectRed, style = HelveticaSmall)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(profile.summary, color = theme.textPrimary, style = HelveticaBody)
                    }
                }
            }
        }

        Duo3DButton(
            text = "Attack Gap: ${biggestBleed?.name ?: "Drill"}",
            buttonColor = ReflectRed,
            shadowColor = ReflectRedDark,
            onClick = { biggestBleed?.let { onAttackBleed(it.name) } }
        )
    }
}
