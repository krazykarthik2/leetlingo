package com.leetlingo.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// =========================================================================
// STRICT USER-DEFINED COLOR ROLES:
// Reel -> Blue
// Reflect -> Red
// Ascend -> Green, White-ish
// Vault -> Amber Yellow / Gold
// Account -> Aesthetic Noise + Black
// =========================================================================

// 1. REEL MODE -> Blue (Duolingo 3D Macaw Blue)
val ReelBlue = Color(0xFF1CB0F6)
val ReelBlueDark = Color(0xFF1899D6) // 3D Bottom Shadow
val ReelBlueLight = Color(0xFFDDF4FE)

// 2. REFLECT MODE -> Red (Duolingo 3D Cardinal Red)
val ReflectRed = Color(0xFFFF4B4B)
val ReflectRedDark = Color(0xFFD32F2F) // 3D Bottom Shadow
val ReflectRedLight = Color(0xFFFFEBEB)

// 3. ASCEND MODE -> Green, White-ish (Duolingo 3D Feather Green & Crisp White)
val AscendGreen = Color(0xFF58CC02)
val AscendGreenDark = Color(0xFF46A302) // 3D Bottom Shadow
val AscendGreenLight = Color(0xFFE8FAD8)
val AscendWhite = Color(0xFFFFFFFF)

// 4. VAULT -> Yellow / Gold (Duolingo 3D Bee Yellow)
val VaultYellow = Color(0xFFFFC800)
val VaultYellowDark = Color(0xFFE5B200) // 3D Bottom Shadow
val VaultYellowLight = Color(0xFFFFF7D6)

// 5. ACCOUNT -> Aesthetic Noise + Pitch Black
val AccountBlack = Color(0xFF000000)
val AccountCardBlack = Color(0xFF111111)
val AccountBorderGray = Color(0xFF262626)
val AccountTextWhite = Color(0xFFFFFFFF)
val AccountTextMuted = Color(0xFF888888)
val AccountColor = Color(0xFF4B4B4B)

// Color aliases for compatibility
val DuoFeatherGreen = AscendGreen
val DuoFeatherGreenDark = AscendGreenDark
val DuoMacawBlue = ReelBlue
val DuoMacawBlueDark = ReelBlueDark
val DuoCardinalRed = ReflectRed
val DuoCardinalRedDark = ReflectRedDark
val DuoBeeYellow = VaultYellow
val DuoBeeYellowDark = VaultYellowDark

// Ninja Mascot Accent (Midnight Shinobi)
val DuoNinjaSuit = Color(0xFF1F2937)
val DuoNinjaMask = Color(0xFF1CB0F6)
val DuoNinjaHeadband = Color(0xFFFF4B4B)
val Shadow3D = Color(0x33000000)

/**
 * Dynamic Theme Palette that seamlessly toggles based on System Dark/Light Mode.
 * White for light theme, Deep dark/obsidian for dark theme.
 */
data class DuoThemeColors(
    val background: Color,
    val cardBackground: Color,
    val surfaceHighlight: Color,
    val border: Color,
    val borderHighlight: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val textMuted: Color,
    val topBarBackground: Color,
    val bottomBarBackground: Color
)

@Composable
fun getAppThemeColors(): DuoThemeColors {
    val dark = isSystemInDarkTheme()
    return if (dark) {
        DuoThemeColors(
            background = Color(0xFF0A0F1D),          // Deep Obsidian Midnight
            cardBackground = Color(0xFF131B2E),      // Dark Elevated Card
            surfaceHighlight = Color(0xFF1A243D),    // Code box / input surface
            border = Color(0xFF222F4C),              // Subtle Slate Border
            borderHighlight = Color(0xFF2D3E66),
            textPrimary = Color(0xFFF8FAFC),         // High contrast crisp white
            textSecondary = Color(0xFF94A3B8),       // Slate gray
            textMuted = Color(0xFF64748B),
            topBarBackground = Color(0xFF0A0F1D),
            bottomBarBackground = Color(0xFF070B14)
        )
    } else {
        DuoThemeColors(
            background = Color(0xFFF7F7F7),          // Duolingo Polar White
            cardBackground = Color(0xFFFFFFFF),      // Crisp White Surface
            surfaceHighlight = Color(0xFFF1F5F9),    // Soft Gray Code/Input
            border = Color(0xFFE2E8F0),              // Swan Border
            borderHighlight = Color(0xFFCBD5E1),
            textPrimary = Color(0xFF1E293B),         // Deep Slate/Eel
            textSecondary = Color(0xFF64748B),       // Wolf subtext
            textMuted = Color(0xFF94A3B8),
            topBarBackground = Color(0xFFFFFFFF),
            bottomBarBackground = Color(0xFFFFFFFF)
        )
    }
}
