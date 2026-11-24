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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leetlingo.app.ui.theme.*

/**
 * Authentic Duolingo 3D Button.
 * Has a dark shadow base (`bottomDepth`) that presses down when tapped!
 */
@Composable
fun Duo3DButton(
    text: String,
    buttonColor: Color,
    shadowColor: Color,
    textColor: Color = DuoSnow,
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
            .height(56.dp + bottomDepth)
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
                .height(56.dp)
                .offset(y = bottomDepth)
                .clip(RoundedCornerShape(cornerRadius))
                .background(if (enabled) shadowColor else DuoSwan)
        )

        // Top Button Face (Pushes down by pressOffset)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .offset(y = pressOffset.dp)
                .clip(RoundedCornerShape(cornerRadius))
                .background(if (enabled) buttonColor else DuoHare),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text.uppercase(),
                color = textColor,
                fontSize = 16.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 1.sp
            )
        }
    }
}

/**
 * Duolingo-style Rounded Selectable Option Card.
 * White card with 2px thick borders and 3D base depth.
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
    val borderColor = statusColor ?: if (isSelected) DuoMacawBlue else DuoSwan
    val shadowColor = if (isSelected) (statusColor ?: DuoMacawBlueDark) else Color(0xFFD6D6D6)
    val bgColor = if (isSelected && statusColor == null) Color(0xFFEBF7FD) else DuoSnow

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(78.dp)
            .clickable { onClick() }
    ) {
        // Bottom shadow
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(74.dp)
                .offset(y = 4.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(shadowColor)
        )

        // Top card surface
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
                // Key badge (e.g. 1, 2, 3, 4)
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .border(2.dp, borderColor, RoundedCornerShape(8.dp))
                        .background(if (isSelected) borderColor else DuoPolar),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "$indexNumber",
                        color = if (isSelected) DuoSnow else DuoWolf,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column {
                    Text(
                        text = title,
                        color = DuoEel,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = description,
                        color = DuoWolf,
                        fontSize = 12.sp,
                        maxLines = 1
                    )
                }
            }
        }
    }
}

/**
 * Animated Ninja Mascot ("Kuro-chan" - The Code Shinobi)
 * Replaces Duo the Owl with a friendly, iconic Duolingo-styled Ninja!
 */
@Composable
fun DuoNinjaMascot(
    modifier: Modifier = Modifier,
    mood: String = "HAPPY", // HAPPY, FOCUS, CHEER
    dialogText: String? = null
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Ninja Mascot Avatar
        Box(
            modifier = Modifier
                .size(76.dp)
                .clip(CircleShape)
                .background(DuoNinjaSuit)
                .border(3.dp, DuoNinjaMask, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                // Red Headband with Ninja Symbol
                Box(
                    modifier = Modifier
                        .width(54.dp)
                        .height(14.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(DuoNinjaHeadband),
                    contentAlignment = Alignment.Center
                ) {
                    Text("</>", color = DuoSnow, fontSize = 8.sp, fontWeight = FontWeight.Black)
                }
                Spacer(modifier = Modifier.height(4.dp))
                // Eyes Mask opening
                Box(
                    modifier = Modifier
                        .width(48.dp)
                        .height(18.dp)
                        .clip(RoundedCornerShape(9.dp))
                        .background(Color(0xFFFFDFBA)), // Peach skin
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Anime expressive eyes
                        Text(if (mood == "HAPPY") "^" else "•", color = DuoNinjaSuit, fontWeight = FontWeight.Black, fontSize = 14.sp)
                        Text(if (mood == "HAPPY") "^" else "•", color = DuoNinjaSuit, fontWeight = FontWeight.Black, fontSize = 14.sp)
                    }
                }
                Spacer(modifier = Modifier.height(2.dp))
                // Mask cloth covering mouth
                Box(
                    modifier = Modifier
                        .width(36.dp)
                        .height(10.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(DuoNinjaSuit)
                )
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        // Duolingo Speech Bubble
        dialogText?.let {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(18.dp))
                    .background(DuoSnow)
                    .border(2.dp, DuoSwan, RoundedCornerShape(18.dp))
                    .padding(14.dp)
            ) {
                Text(
                    text = it,
                    color = DuoEel,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 20.sp
                )
            }
        }
    }
}
