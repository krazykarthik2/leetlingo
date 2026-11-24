package com.leetlingo.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leetlingo.app.data.model.MemoryState
import com.leetlingo.app.data.model.VaultItem
import com.leetlingo.app.data.repository.ContentRepository
import com.leetlingo.app.ui.theme.*

enum class AppTab(val title: String, val icon: String, val activeColor: Color) {
    REEL("Reel", "🎬", ReelBlue),
    REFLECT("Reflect", "🪞", ReflectRed),
    ASCEND("Ascend", "🚀", AscendGreen),
    VAULT("Vault", "🏛️", VaultAmber),
    ACCOUNT("Account", "👤", AccountTextWhite)
}

@Composable
fun LeetLingoMainScreen() {
    val context = LocalContext.current
    val repository = remember { ContentRepository(context) }

    val reels = remember { repository.loadReels() }
    val concepts = remember { repository.loadConcepts() }
    val weaknessProfile = remember { repository.loadWeaknessProfile() }

    var currentTab by remember { mutableStateOf(AppTab.REEL) }
    val vaultItems = remember { mutableStateListOf<VaultItem>() }

    Scaffold(
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(68.dp)
                    .background(Color(0xFF030712))
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                AppTab.values().forEach { tab ->
                    val isSelected = currentTab == tab
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { currentTab = tab }
                            .padding(horizontal = 12.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = tab.icon,
                            fontSize = 20.sp
                        )
                        Text(
                            text = tab.title,
                            color = if (isSelected) tab.activeColor else Color(0xFF64748B),
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (currentTab) {
                AppTab.REEL -> ReelFeedScreen(
                    problems = reels,
                    onSaveToVault = { problem, pseudo ->
                        vaultItems.add(
                            VaultItem(
                                problem = problem,
                                userPseudoCode = pseudo,
                                wonTimestampMillis = System.currentTimeMillis(),
                                memoryState = MemoryState.FRESH
                            )
                        )
                    },
                    onTooHardBail = {
                        // Ingest into Reflect queue
                    }
                )

                AppTab.REFLECT -> ReflectScreen(
                    profile = weaknessProfile,
                    onAttackBleed = {
                        currentTab = AppTab.REEL
                    }
                )

                AppTab.ASCEND -> AscendScreen(
                    concepts = concepts,
                    onAbsorbConcept = {
                        currentTab = AppTab.REEL
                    }
                )

                AppTab.VAULT -> VaultScreen(
                    vaultItems = vaultItems,
                    onLaunchLeetCode = {
                        // Open external LeetCode intent
                    },
                    onReattemptL2 = {
                        currentTab = AppTab.REEL
                    }
                )

                AppTab.ACCOUNT -> AccountScreen(
                    onOpenSettings = {}
                )
            }
        }
    }
}
