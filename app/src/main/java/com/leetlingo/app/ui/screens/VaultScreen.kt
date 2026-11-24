package com.leetlingo.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import com.leetlingo.app.ui.components.Duo3DButton
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
            .background(DuoPolar)
            .padding(16.dp)
    ) {
        // Top Header
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("VAULT", color = VaultYellowDark, fontWeight = FontWeight.Black, fontSize = 22.sp)
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(VaultYellowLight)
                    .border(2.dp, VaultYellow, RoundedCornerShape(12.dp))
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text("${vaultItems.size} CONQUERED", color = VaultYellowDark, fontWeight = FontWeight.Black, fontSize = 11.sp)
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Duolingo Filter Pills
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("ALL", "FADING", "COLD", "CONQUERED").forEach { filter ->
                val isSelected = selectedFilter == filter
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(14.dp))
                        .background(if (isSelected) VaultYellow else DuoSnow)
                        .border(2.dp, if (isSelected) VaultYellowDark else DuoSwan, RoundedCornerShape(14.dp))
                        .clickable { selectedFilter = filter }
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = filter,
                        color = if (isSelected) DuoSnow else DuoWolf,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        if (filteredItems.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Your vault is currently empty.\nCrack Katas in Reel mode to save them!", color = DuoWolf, fontWeight = FontWeight.Bold)
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(filteredItems) { item ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(18.dp))
                            .background(DuoSnow)
                            .border(2.dp, DuoSwan, RoundedCornerShape(18.dp))
                            .padding(16.dp)
                    ) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(item.problem.title, color = DuoEel, fontWeight = FontWeight.Black, fontSize = 16.sp)
                                val (statusText, statusCol) = when (item.memoryState) {
                                    MemoryState.FRESH -> "Fresh" to AscendGreen
                                    MemoryState.FADING -> "Fading" to VaultYellowDark
                                    MemoryState.COLD -> "Cold" to ReflectRed
                                    MemoryState.CONQUERED -> "Done" to ReelBlue
                                }
                                Text(statusText, color = statusCol, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }

                            Spacer(modifier = Modifier.height(6.dp))
                            Text("Your verified pseudo:", color = DuoWolf, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            Text(item.userPseudoCode, color = DuoEel, fontSize = 13.sp, maxLines = 2, fontWeight = FontWeight.Medium)

                            Spacer(modifier = Modifier.height(12.dp))

                            Duo3DButton(
                                text = "Code on LeetCode",
                                buttonColor = VaultYellow,
                                shadowColor = VaultYellowDark,
                                textColor = DuoSnow,
                                onClick = { onLaunchLeetCode(item) }
                            )
                        }
                    }
                }
            }
        }
    }
}
