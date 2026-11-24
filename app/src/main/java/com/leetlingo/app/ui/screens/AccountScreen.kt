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
import androidx.compose.ui.text.font.FontWeight
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
            .background(AccountBlack) // Pitch Black + Aesthetic Tone
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
                Text("ACCOUNT", color = AccountTextWhite, fontWeight = FontWeight.Black, fontSize = 22.sp)
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF1E1E1E))
                        .border(1.dp, AccountBorderGray, RoundedCornerShape(12.dp))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text("SHINOBI PROFILE", color = AccountTextMuted, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Ninja Mascot in Night Mode
            DuoNinjaMascot(
                mood = "FOCUS",
                dialogText = "Shadow Warrior Karthik. Keep your streak alive to stay ahead of the curve."
            )

            Spacer(modifier = Modifier.height(18.dp))

            // Profile Card (Aesthetic Pitch Black with crisp borders)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(AccountCardBlack)
                    .border(1.dp, AccountBorderGray, RoundedCornerShape(20.dp))
                    .padding(18.dp)
            ) {
                Column {
                    Text("KARTHIK", color = AccountTextWhite, fontSize = 22.sp, fontWeight = FontWeight.Black)
                    Text("karthik@domain.dev", color = AccountTextMuted, fontSize = 13.sp)
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        Text("🔥 7d Streak", color = VaultYellow, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text("🏛️ 47 Conquered", color = AscendGreen, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Lifetime Stats
            Text("LIFETIME SHINOBI STATS", color = AccountTextMuted, fontSize = 12.sp, fontWeight = FontWeight.Bold)
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
                        Text("Katas Solved (L1)", color = AccountTextWhite, fontSize = 14.sp)
                        Text("218", color = AccountTextMuted, fontWeight = FontWeight.Black)
                    }
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Pseudo Wins (L2)", color = AccountTextWhite, fontSize = 14.sp)
                        Text("47", color = AccountTextMuted, fontWeight = FontWeight.Black)
                    }
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("LeetCode Shipped", color = AccountTextWhite, fontSize = 14.sp)
                        Text("11", color = AccountTextMuted, fontWeight = FontWeight.Black)
                    }
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Patterns Absorbed", color = AccountTextWhite, fontSize = 14.sp)
                        Text("8", color = AccountTextMuted, fontWeight = FontWeight.Black)
                    }
                }
            }
        }

        // Chunky 3D Monochromatic Button
        Duo3DButton(
            text = "System Preferences",
            buttonColor = AccountTextWhite,
            shadowColor = Color(0xFFCCCCCC),
            textColor = AccountBlack,
            onClick = onOpenSettings
        )
    }
}
