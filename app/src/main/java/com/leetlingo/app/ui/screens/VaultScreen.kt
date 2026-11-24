package com.leetlingo.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leetlingo.app.data.model.MemoryState
import com.leetlingo.app.data.model.VaultItem
import com.leetlingo.app.ui.components.DuoChunkyButton
import com.leetlingo.app.ui.theme.*

@Composable
fun VaultScreen(
    vaultItems: List<VaultItem>,
    onLaunchLeetCode: (VaultItem) -> Unit,
    onReattemptL2: (VaultItem) -> Unit
) {
    var selectedFilter by remember { mutableStateOf("ALL") }

    val filteredItems = when (selectedFilter) {
        "FADING" -> vaultItems.filter { it.memoryState == MemoryState.FADING }
        "COLD" -> vaultItems.filter { it.memoryState == MemoryState.COLD }
        "CONQUERED" -> vaultItems.filter { it.memoryState == MemoryState.CONQUERED }
        else -> vaultItems
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(VaultBackground)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("🏛️ VAULT", color = VaultAmber, fontWeight = FontWeight.Black, fontSize = 20.sp)
            Text("${vaultItems.size} Won", color = Color(0xFF94A3B8), fontSize = 14.sp)
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Filter chips
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("ALL", "FADING", "COLD", "CONQUERED").forEach { filter ->
                val isSelected = selectedFilter == filter
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(if (isSelected) VaultAmber else Color(0xFF1F1F1F))
                        .clickable { selectedFilter = filter }
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = filter,
                        color = if (isSelected) Color.Black else Color(0xFF94A3B8),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (filteredItems.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No problems in this filter yet. Crack reels to fill your Vault!", color = Color(0xFF64748B))
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(filteredItems) { item ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = VaultCardBackground)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(item.problem.title, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                val (statusText, statusCol) = when (item.memoryState) {
                                    MemoryState.FRESH -> "🟢 Fresh" to AscendGreen
                                    MemoryState.FADING -> "⚠️ Fading" to Color(0xFFFFC800)
                                    MemoryState.COLD -> "🔴 Cold" to ReflectRed
                                    MemoryState.CONQUERED -> "✅ Done" to ReelBlueGlow
                                }
                                Text(statusText, color = statusCol, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }

                            Spacer(modifier = Modifier.height(6.dp))
                            Text("Your pseudo logic:", color = Color(0xFF94A3B8), fontSize = 12.sp)
                            Text(item.userPseudoCode, color = Color(0xFFE2E8F0), fontSize = 13.sp, maxLines = 2)

                            Spacer(modifier = Modifier.height(12.dp))

                            DuoChunkyButton(
                                text = "🚀 CODE ON LEETCODE (No spoilers)",
                                buttonColor = VaultAmber,
                                textColor = Color.Black,
                                onClick = { onLaunchLeetCode(item) }
                            )
                        }
                    }
                }
            }
        }
    }
}
