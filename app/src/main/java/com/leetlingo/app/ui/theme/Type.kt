package com.leetlingo.app.ui.theme

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.leetlingo.app.R

// Exact Helvetica Font Family loaded directly from the provided TTFs:
// - Helvetica.ttf (Regular)
// - Helvetica-Bold_0.ttf (Bold)
// - Helvetica-BoldOblique.ttf (Bold Italic)
// - Helvetica-Oblique.ttf (Italic)
val HelveticaFontFamily = FontFamily(
    Font(R.font.helvetica_regular, FontWeight.Normal, FontStyle.Normal),
    Font(R.font.helvetica_bold, FontWeight.Bold, FontStyle.Normal),
    Font(R.font.helvetica_bold, FontWeight.Black, FontStyle.Normal),
    Font(R.font.helvetica_oblique, FontWeight.Normal, FontStyle.Italic),
    Font(R.font.helvetica_bold_oblique, FontWeight.Bold, FontStyle.Italic)
)

val HelveticaTitle = TextStyle(
    fontFamily = HelveticaFontFamily,
    fontWeight = FontWeight.Bold,
    fontSize = 24.sp,
    letterSpacing = (-1.0).sp // Least / tight Swiss letter spacing
)

val HelveticaSubtitle = TextStyle(
    fontFamily = HelveticaFontFamily,
    fontWeight = FontWeight.Bold,
    fontSize = 17.sp,
    letterSpacing = (-0.6).sp
)

val HelveticaBody = TextStyle(
    fontFamily = HelveticaFontFamily,
    fontWeight = FontWeight.Normal,
    fontSize = 15.sp,
    lineHeight = 22.sp,
    letterSpacing = (-0.4).sp
)

val HelveticaSmall = TextStyle(
    fontFamily = HelveticaFontFamily,
    fontWeight = FontWeight.Bold,
    fontSize = 12.sp,
    letterSpacing = (-0.3).sp
)

val HelveticaButton = TextStyle(
    fontFamily = HelveticaFontFamily,
    fontWeight = FontWeight.Bold,
    fontSize = 16.sp,
    letterSpacing = (-0.4).sp
)
