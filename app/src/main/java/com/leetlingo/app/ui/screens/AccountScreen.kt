package com.leetlingo.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.leetlingo.app.ui.components.Duo3DButton
import com.leetlingo.app.ui.components.DuoNinjaMascot
import com.leetlingo.app.ui.theme.*

@Composable
fun AccountScreen(
    onOpenSettings: () -> Unit
) {
    var showSettings by remember { mutableStateOf(false) }

    if (showSettings) {
        SettingsScreen(
            onBack = { showSettings = false }
        )
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AccountBlack) // OLED Pitch Black + Aesthetic Noise
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
                Text("Account", color = AccountTextWhite, style = HelveticaTitle)
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF1E1E1E))
                        .border(1.dp, AccountBorderGray, RoundedCornerShape(12.dp))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text("Shinobi ID", color = AccountTextMuted, style = HelveticaSmall)
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            DuoNinjaMascot(
                mood = "FOCUS",
                dialogText = "Shadow Warrior. Protect your daily streak to dominate algorithmic interviews."
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Profile Card (Aesthetic Pitch Black)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(AccountCardBlack)
                    .border(1.dp, AccountBorderGray, RoundedCornerShape(20.dp))
                    .padding(16.dp)
            ) {
                Column {
                    Text("Karthik", color = AccountTextWhite, style = HelveticaTitle)
                    Text("karthik@domain.dev", color = AccountTextMuted, style = HelveticaBody)
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Filled.LocalFireDepartment,
                                contentDescription = "Streak",
                                tint = VaultYellow,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("7d Streak", color = VaultYellow, style = HelveticaSubtitle)
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Filled.Security,
                                contentDescription = "Vault",
                                tint = AscendGreen,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("47 Conquered", color = AscendGreen, style = HelveticaSubtitle)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // GitHub-Style Green Contribution Calendar
            Text("Activity Calendar", color = AccountTextMuted, style = HelveticaSmall)
            Spacer(modifier = Modifier.height(8.dp))
            GitHubContributionCalendar()
        }

        Duo3DButton(
            text = "System Preferences",
            buttonColor = AccountTextWhite,
            shadowColor = Color(0xFFCCCCCC),
            textColor = AccountBlack,
            onClick = {
                showSettings = true
                onOpenSettings()
            }
        )
    }
}

@Composable
fun GitHubContributionCalendar() {
    // 16 weeks x 7 days deterministic activity levels (0..4)
    val levels = remember {
        listOf(
            0, 1, 2, 0, 3, 1, 0,
            1, 2, 4, 2, 1, 0, 1,
            2, 3, 1, 4, 2, 2, 0,
            0, 1, 3, 2, 4, 3, 1,
            1, 0, 2, 3, 1, 2, 2,
            2, 4, 3, 1, 0, 3, 4,
            3, 2, 1, 4, 3, 2, 1,
            0, 2, 3, 4, 2, 1, 0,
            1, 3, 2, 1, 4, 3, 2,
            2, 1, 4, 3, 2, 0, 1,
            3, 4, 2, 1, 3, 4, 2,
            1, 2, 0, 3, 4, 2, 3,
            2, 3, 4, 2, 1, 3, 4,
            4, 3, 2, 1, 2, 4, 3,
            1, 2, 3, 4, 3, 2, 1,
            3, 4, 4, 3, 4, 4, 4
        )
    }

    val greenPalette = listOf(
        Color(0xFF161B22), // 0: empty
        Color(0xFF0E4429), // 1: low
        Color(0xFF006D32), // 2: medium
        Color(0xFF26A641), // 3: high
        AscendGreen        // 4: peak (#58CC02)
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(AccountCardBlack)
            .border(1.dp, AccountBorderGray, RoundedCornerShape(18.dp))
            .padding(14.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                for (week in 0 until 16) {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        for (day in 0 until 7) {
                            val level = levels[week * 7 + day]
                            Box(
                                modifier = Modifier
                                    .size(14.dp)
                                    .clip(RoundedCornerShape(3.dp))
                                    .background(greenPalette[level])
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Less", color = AccountTextMuted, style = HelveticaSmall)
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    greenPalette.forEach { color ->
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .clip(RoundedCornerShape(2.dp))
                                .background(color)
                        )
                    }
                }
                Text("More", color = AccountTextMuted, style = HelveticaSmall)
            }
        }
    }
}

@Composable
fun SettingsScreen(
    onBack: () -> Unit
) {
    var themeMode by remember { mutableStateOf("System") }
    var dailyReminder by remember { mutableStateOf(true) }
    var hapticsEnabled by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AccountBlack)
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            // Top Bar
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = AccountTextWhite,
                    modifier = Modifier
                        .size(26.dp)
                        .clickable { onBack() }
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text("System Preferences", color = AccountTextWhite, style = HelveticaTitle)
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Theme Preference
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(AccountCardBlack)
                    .border(1.dp, AccountBorderGray, RoundedCornerShape(18.dp))
                    .padding(16.dp)
            ) {
                Column {
                    Text("Appearance", color = AccountTextWhite, style = HelveticaSubtitle)
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf("System", "Dark", "Light").forEach { mode ->
                            val isSelected = themeMode == mode
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(if (isSelected) AscendGreen else Color(0xFF1E1E1E))
                                    .clickable { themeMode = mode }
                                    .padding(horizontal = 14.dp, vertical = 8.dp)
                            ) {
                                Text(
                                    text = mode,
                                    color = if (isSelected) AccountBlack else AccountTextMuted,
                                    style = HelveticaSmall
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Toggles Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(AccountCardBlack)
                    .border(1.dp, AccountBorderGray, RoundedCornerShape(18.dp))
                    .padding(16.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Daily Streak Reminder", color = AccountTextWhite, style = HelveticaBody)
                            Text("Nudge before midnight", color = AccountTextMuted, style = HelveticaSmall)
                        }
                        Switch(
                            checked = dailyReminder,
                            onCheckedChange = { dailyReminder = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = AccountBlack,
                                checkedTrackColor = AscendGreen
                            )
                        )
                    }

                    HorizontalDivider(color = AccountBorderGray)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Tactile Haptics", color = AccountTextWhite, style = HelveticaBody)
                            Text("3D button & swipe feedback", color = AccountTextMuted, style = HelveticaSmall)
                        }
                        Switch(
                            checked = hapticsEnabled,
                            onCheckedChange = { hapticsEnabled = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = AccountBlack,
                                checkedTrackColor = AscendGreen
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Subscription Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(AccountCardBlack)
                    .border(1.dp, AccountBorderGray, RoundedCornerShape(18.dp))
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Ad-Free Plan", color = AccountTextWhite, style = HelveticaBody)
                        Text("Remove interstitials", color = AccountTextMuted, style = HelveticaSmall)
                    }
                    Text("₹100 / mo", color = AscendGreen, style = HelveticaSubtitle)
                }
            }
        }

        Duo3DButton(
            text = "Done",
            buttonColor = AccountTextWhite,
            shadowColor = Color(0xFFCCCCCC),
            textColor = AccountBlack,
            onClick = onBack
        )
    }
}
