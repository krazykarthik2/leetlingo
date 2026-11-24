package com.leetlingo.app.ui.theme

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

// Universal Duolingo Canvas & Surface Neutral Tokens
val DuoSnow = Color(0xFFFFFFFF)       // Clean white card surface
val DuoPolar = Color(0xFFF7F7F7)      // Soft background
val DuoSwan = Color(0xFFE5E5E5)       // 2px border stroke
val DuoEel = Color(0xFF4B4B4B)        // Primary bold text
val DuoWolf = Color(0xFF777777)       // Secondary subtext
val DuoHare = Color(0xFFAFAFAF)       // Disabled state

// Color aliases for compatibility
val DuoFeatherGreen = AscendGreen
val DuoFeatherGreenDark = AscendGreenDark
val DuoMacawBlue = ReelBlue
val DuoMacawBlueDark = ReelBlueDark
val DuoCardinalRed = ReflectRed
val DuoCardinalRedDark = ReflectRedDark
val DuoBeeYellow = VaultYellow
val DuoBeeYellowDark = VaultYellowDark

val Shadow3D = Color(0x33000000)

// Ninja Mascot Accent (Midnight Shinobi)
val DuoNinjaSuit = Color(0xFF1F2937)
val DuoNinjaMask = Color(0xFF1CB0F6) // Matches current active mode
val DuoNinjaHeadband = Color(0xFFFF4B4B)
