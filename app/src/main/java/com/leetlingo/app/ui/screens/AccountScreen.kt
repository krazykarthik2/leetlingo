package com.leetlingo.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leetlingo.app.ui.components.Duo3DButton
import com.leetlingo.app.ui.components.DuoNinjaMascot
import com.leetlingo.app.ui.theme.*

@Composable
fun AccountScreen(
    onOpenSettings: () -> Unit
) {
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

            Spacer(modifier = Modifier.height(18.dp))

            // Profile Card (Aesthetic Pitch Black)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(AccountCardBlack)
                    .border(1.dp, AccountBorderGray, RoundedCornerShape(20.dp))
                    .padding(18.dp)
            ) {
                Column {
                    Text("Karthik", color = AccountTextWhite, style = HelveticaTitle)
                    Text("karthik@domain.dev", color = AccountTextMuted, style = HelveticaBody)
                    Spacer(modifier = Modifier.height(12.dp))
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

            Text("Lifetime Shinobi Stats", color = AccountTextMuted, style = HelveticaSmall)
            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(AccountCardBlack)
                    .border(1.dp, AccountBorderGray, RoundedCornerShape(18.dp))
                    .padding(16.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Katas Solved (L1)", color = AccountTextWhite, style = HelveticaBody)
                        Text("218", color = AccountTextMuted, style = HelveticaSubtitle)
                    }
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Pseudo Wins (L2)", color = AccountTextWhite, style = HelveticaBody)
                        Text("47", color = AccountTextMuted, style = HelveticaSubtitle)
                    }
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("LeetCode Shipped", color = AccountTextWhite, style = HelveticaBody)
                        Text("11", color = AccountTextMuted, style = HelveticaSubtitle)
                    }
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Patterns Absorbed", color = AccountTextWhite, style = HelveticaBody)
                        Text("8", color = AccountTextMuted, style = HelveticaSubtitle)
                    }
                }
            }
        }

        Duo3DButton(
            text = "System Preferences",
            buttonColor = AccountTextWhite,
            shadowColor = Color(0xFFCCCCCC),
            textColor = AccountBlack,
            onClick = onOpenSettings
        )
    }
}
