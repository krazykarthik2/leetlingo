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
    val theme = getAppThemeColors()
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
            .background(theme.background)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Vault", color = VaultYellowDark, style = HelveticaTitle)
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(VaultYellowLight)
                    .border(2.dp, VaultYellow, RoundedCornerShape(12.dp))
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text("${vaultItems.size} Conquered", color = VaultYellowDark, style = HelveticaSmall)
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("All", "Fading", "Cold", "Conquered").forEach { filter ->
                val isSelected = selectedFilter.equals(filter, ignoreCase = true)
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(14.dp))
                        .background(if (isSelected) VaultYellow else theme.cardBackground)
                        .border(2.dp, if (isSelected) VaultYellowDark else theme.border, RoundedCornerShape(14.dp))
                        .clickable { selectedFilter = filter.uppercase() }
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = filter,
                        color = if (isSelected) Color.White else theme.textSecondary,
                        style = HelveticaSmall
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        if (filteredItems.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    "Your vault is currently empty.\nMaster Katas in Reel mode to save them!",
                    color = theme.textMuted,
                    style = HelveticaSubtitle
                )
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(filteredItems) { item ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(18.dp))
                            .background(theme.cardBackground)
                            .border(2.dp, theme.border, RoundedCornerShape(18.dp))
                            .padding(16.dp)
                    ) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(item.problem.title, color = theme.textPrimary, style = HelveticaSubtitle)
                                val (statusText, statusCol) = when (item.memoryState) {
                                    MemoryState.FRESH -> "FRESH" to AscendGreen
                                    MemoryState.FADING -> "FADING" to VaultYellowDark
                                    MemoryState.COLD -> "COLD" to ReflectRed
                                    MemoryState.CONQUERED -> "CONQUERED" to ReelBlue
                                }
                                Text(statusText, color = statusCol, style = HelveticaSmall)
                            }

                            Spacer(modifier = Modifier.height(6.dp))
                            Text("Your verified pseudo:", color = theme.textMuted, style = HelveticaSmall)
                            Text(item.userPseudoCode, color = theme.textSecondary, style = HelveticaBody, maxLines = 2)

                            Spacer(modifier = Modifier.height(12.dp))

                            Duo3DButton(
                                text = "Code on LeetCode",
                                buttonColor = VaultYellow,
                                shadowColor = VaultYellowDark,
                                textColor = Color.White,
                                onClick = { onLaunchLeetCode(item) }
                            )
                        }
                    }
                }
            }
        }
    }
}
