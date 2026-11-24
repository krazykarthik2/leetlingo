package com.leetlingo.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.FolderSpecial
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Radar
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leetlingo.app.data.model.MemoryState
import com.leetlingo.app.data.model.VaultItem
import com.leetlingo.app.data.repository.ContentRepository
import com.leetlingo.app.ui.theme.*

enum class AppTab(val title: String, val icon: ImageVector, val activeColor: Color) {
    REEL("Reel", Icons.Filled.PlayCircle, ReelBlue),
    REFLECT("Reflect", Icons.Filled.Radar, ReflectRed),
    ASCEND("Ascend", Icons.Filled.TrendingUp, AscendGreen),
    VAULT("Vault", Icons.Filled.FolderSpecial, VaultYellow),
    ACCOUNT("Account", Icons.Filled.Person, AccountColor)
}

@Composable
fun LeetLingoMainScreen() {
    val context = LocalContext.current
    val repository = remember { ContentRepository(context) }
    val theme = getAppThemeColors()

    val reels = remember { repository.loadReels() }
    val concepts = remember { repository.loadConcepts() }
    val weaknessProfile = remember { repository.loadWeaknessProfile() }

    var currentTab by remember { mutableStateOf(AppTab.REEL) }
    val vaultItems = remember { mutableStateListOf<VaultItem>() }

    var streakCount by remember { mutableIntStateOf(7) }
    var streakActiveToday by remember { mutableStateOf(false) }

    val completeDailyAction = {
        if (!streakActiveToday) {
            streakActiveToday = true
            streakCount += 1
        }
    }

    Scaffold(
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(68.dp)
                    .background(theme.bottomBarBackground)
                    .border(1.dp, theme.border.copy(alpha = 0.5f))
                    .padding(horizontal = 8.dp, vertical = 6.dp),
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
                            .padding(horizontal = 14.dp, vertical = 4.dp)
                    ) {
                        Icon(
                            imageVector = tab.icon,
                            contentDescription = tab.title,
                            tint = if (isSelected) tab.activeColor else theme.textMuted,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = tab.title,
                            color = if (isSelected) tab.activeColor else theme.textMuted,
                            style = HelveticaSmall
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
                    streakCount = streakCount,
                    streakActiveToday = streakActiveToday,
                    onSaveToVault = { problem, pseudo ->
                        completeDailyAction()
                        vaultItems.add(
                            VaultItem(
                                problem = problem,
                                userPseudoCode = pseudo,
                                wonTimestampMillis = System.currentTimeMillis(),
                                memoryState = MemoryState.FRESH
                            )
                        )
                    },
                    onTooHardBail = {}
                )

                AppTab.REFLECT -> ReflectScreen(
                    profile = weaknessProfile,
                    onAttackBleed = { completeDailyAction() }
                )

                AppTab.ASCEND -> AscendScreen(
                    concepts = concepts,
                    onAbsorbConcept = {
                        completeDailyAction()
                        currentTab = AppTab.REEL
                    }
                )

                AppTab.VAULT -> VaultScreen(
                    vaultItems = vaultItems,
                    onLaunchLeetCode = {},
                    onReattemptL2 = { currentTab = AppTab.REEL }
                )

                AppTab.ACCOUNT -> AccountScreen(
                    streakCount = streakCount,
                    streakActiveToday = streakActiveToday,
                    onOpenSettings = {}
                )
            }
        }
    }
}
