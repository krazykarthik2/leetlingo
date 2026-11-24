package com.leetlingo.app.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leetlingo.app.ui.theme.*

/**
 * Authentic Duolingo 3D Button with Helvetica Typography & least letter spacing.
 */
@Composable
fun Duo3DButton(
    text: String,
    buttonColor: Color,
    shadowColor: Color,
    textColor: Color = Color.White,
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 16.dp,
    bottomDepth: Dp = 4.dp,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }
    val pressOffset by animateFloatAsState(targetValue = if (isPressed) 4f else 0f, label = "pressOffset")

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(54.dp + bottomDepth)
            .pointerInput(enabled) {
                if (!enabled) return@pointerInput
                while (true) {
                    awaitPointerEventScope {
                        awaitFirstDown(requireUnconsumed = false)
                        isPressed = true
                        waitForUpOrCancellation()
                        isPressed = false
                    }
                }
            }
            .clickable(
                enabled = enabled,
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                onClick()
            }
    ) {
        // Bottom Shadow Base
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp)
                .offset(y = bottomDepth)
                .clip(RoundedCornerShape(cornerRadius))
                .background(if (enabled) shadowColor else Color(0xFFCCCCCC))
        )

        // Top Button Face
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp)
                .offset(y = pressOffset.dp)
                .clip(RoundedCornerShape(cornerRadius))
                .background(if (enabled) buttonColor else Color(0xFFE2E8F0)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text.uppercase(),
                color = textColor,
                style = HelveticaButton
            )
        }
    }
}

/**
 * Option card adapting to Dark/Light theme dynamically.
 */
@Composable
fun DuoOptionCard(
    indexNumber: Int,
    title: String,
    description: String,
    isSelected: Boolean,
    statusColor: Color? = null,
    onClick: () -> Unit
) {
    val theme = getAppThemeColors()
    val borderColor = statusColor ?: if (isSelected) ReelBlue else theme.border
    val shadowColor = if (isSelected) (statusColor ?: ReelBlueDark) else theme.borderHighlight
    val bgColor = if (isSelected && statusColor == null) {
        ReelBlue.copy(alpha = 0.15f)
    } else theme.cardBackground

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(78.dp)
            .clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(74.dp)
                .offset(y = 4.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(shadowColor)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(74.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(bgColor)
                .border(2.dp, borderColor, RoundedCornerShape(16.dp))
                .padding(horizontal = 16.dp, vertical = 10.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .border(2.dp, borderColor, RoundedCornerShape(8.dp))
                        .background(if (isSelected) borderColor else theme.surfaceHighlight),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "$indexNumber",
                        color = if (isSelected) Color.White else theme.textSecondary,
                        style = HelveticaSmall
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column {
                    Text(
                        text = title,
                        color = theme.textPrimary,
                        style = HelveticaSubtitle
                    )
                    Text(
                        text = description,
                        color = theme.textSecondary,
                        style = HelveticaSmall,
                        maxLines = 1
                    )
                }
            }
        }
    }
}

/**
 * Animated Ninja Mascot ("Kuro-chan" - The Code Shinobi) with Speech Bubble.
 */
@Composable
fun DuoNinjaMascot(
    modifier: Modifier = Modifier,
    mood: String = "HAPPY",
    dialogText: String? = null
) {
    val theme = getAppThemeColors()

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Ninja Mascot Avatar
        Box(
            modifier = Modifier
                .size(72.dp)
                .clip(CircleShape)
                .background(DuoNinjaSuit)
                .border(3.dp, DuoNinjaMask, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .width(50.dp)
                        .height(13.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(DuoNinjaHeadband),
                    contentAlignment = Alignment.Center
                ) {
                    Text("</>", color = Color.White, fontSize = 8.sp, fontWeight = FontWeight.Black)
                }
                Spacer(modifier = Modifier.height(4.dp))
                Box(
                    modifier = Modifier
                        .width(44.dp)
                        .height(16.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFFFFDFBA)),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 7.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(if (mood == "HAPPY") "^" else "•", color = DuoNinjaSuit, fontWeight = FontWeight.Black, fontSize = 13.sp)
                        Text(if (mood == "HAPPY") "^" else "•", color = DuoNinjaSuit, fontWeight = FontWeight.Black, fontSize = 13.sp)
                    }
                }
                Spacer(modifier = Modifier.height(2.dp))
                Box(
                    modifier = Modifier
                        .width(34.dp)
                        .height(9.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(DuoNinjaSuit)
                )
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        dialogText?.let {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(18.dp))
                    .background(theme.cardBackground)
                    .border(2.dp, theme.border, RoundedCornerShape(18.dp))
                    .padding(14.dp)
            ) {
                Text(
                    text = it,
                    color = theme.textPrimary,
                    style = HelveticaBody
                )
            }
        }
    }
}
