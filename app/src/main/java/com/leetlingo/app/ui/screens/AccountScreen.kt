package com.leetlingo.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leetlingo.app.ui.components.DuoChunkyButton
import com.leetlingo.app.ui.theme.*

@Composable
fun AccountScreen(
    onOpenSettings: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AccountBlack)
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text("👤 ACCOUNT", color = AccountTextWhite, fontWeight = FontWeight.Black, fontSize = 20.sp)
            Text("Aesthetic OLED Monochrome + Analog Noise", color = AccountTextMuted, fontSize = 13.sp)

            Spacer(modifier = Modifier.height(20.dp))

            // Profile Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(AccountCardBlack)
                    .border(1.dp, AccountBorderGray, RoundedCornerShape(20.dp))
                    .padding(20.dp)
            ) {
                Column {
                    Text("KARTHIK", color = AccountTextWhite, fontSize = 22.sp, fontWeight = FontWeight.Black)
                    Text("karthik@domain.dev", color = AccountTextMuted, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        Text("🔥 12d Streak", color = Color(0xFFFFC800), fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text("🏛️ 47 Conquered", color = VaultAmber, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Lifetime metrics
            Text("LIFETIME SNAPSHOT", color = AccountTextMuted, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(AccountCardBlack)
                    .border(1.dp, AccountBorderGray, RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Cards Cracked (L1)", color = AccountTextWhite)
                        Text("218", color = AccountTextMuted, fontWeight = FontWeight.Bold)
                    }
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Pseudo Wins (L2)", color = AccountTextWhite)
                        Text("47", color = AccountTextMuted, fontWeight = FontWeight.Bold)
                    }
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("LeetCode Shipped", color = AccountTextWhite)
                        Text("11", color = AccountTextMuted, fontWeight = FontWeight.Bold)
                    }
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Patterns Absorbed", color = AccountTextWhite)
                        Text("8", color = AccountTextMuted, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Single primary action
        DuoChunkyButton(
            text = "⚙️ SYSTEM PREFERENCES",
            buttonColor = Color.White,
            textColor = Color.Black,
            onClick = onOpenSettings
        )
    }
}
