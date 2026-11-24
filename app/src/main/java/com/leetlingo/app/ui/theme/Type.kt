package com.leetlingo.app.ui.theme

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Helvetica font family with least/tight letter spacing for aesthetic, premium Swiss typography
val HelveticaFontFamily = FontFamily.SansSerif

val HelveticaTitle = TextStyle(
    fontFamily = HelveticaFontFamily,
    fontWeight = FontWeight.Black,
    fontSize = 24.sp,
    letterSpacing = (-0.8).sp // Least / tight letter spacing
)

val HelveticaSubtitle = TextStyle(
    fontFamily = HelveticaFontFamily,
    fontWeight = FontWeight.Bold,
    fontSize = 17.sp,
    letterSpacing = (-0.4).sp
)

val HelveticaBody = TextStyle(
    fontFamily = HelveticaFontFamily,
    fontWeight = FontWeight.Medium,
    fontSize = 15.sp,
    lineHeight = 22.sp,
    letterSpacing = (-0.3).sp
)

val HelveticaSmall = TextStyle(
    fontFamily = HelveticaFontFamily,
    fontWeight = FontWeight.Bold,
    fontSize = 12.sp,
    letterSpacing = (-0.2).sp
)

val HelveticaButton = TextStyle(
    fontFamily = HelveticaFontFamily,
    fontWeight = FontWeight.Black,
    fontSize = 16.sp,
    letterSpacing = (-0.3).sp
)
