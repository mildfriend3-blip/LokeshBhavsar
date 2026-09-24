package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Lan
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CardBorderNavy
import com.example.ui.theme.NavyPrimary
import com.example.ui.theme.OrangeAccent
import com.example.ui.theme.SlateLight
import com.example.ui.theme.SlateSecondary

enum class NavTab(val label: String, val icon: ImageVector, val targetScreen: Int) {
    HOME("Home", Icons.Default.Dashboard, 6),
    REPORT("Report", Icons.Default.AddCircle, 4),
    DRAFTS("Drafts", Icons.Default.Sync, 7),
    PROFILE("Profile", Icons.Default.Lan, 8)
}

@Composable
fun HyperEdgeBottomNav(
    currentScreen: Int,
    queuedCount: Int,
    onSelectTab: (NavTab) -> Unit,
    modifier: Modifier = Modifier
) {
    val activeTab = when (currentScreen) {
        4, 5 -> NavTab.REPORT
        6 -> NavTab.HOME
        7 -> NavTab.DRAFTS
        8 -> NavTab.PROFILE
        else -> NavTab.HOME
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(NavyPrimary)
    ) {
        // Thin navy divider
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(CardBorderNavy)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            NavTab.entries.forEach { tab ->
                val isSelected = activeTab == tab
                val iconColor = if (isSelected) OrangeAccent else SlateLight
                val textColor = if (isSelected) OrangeAccent else SlateSecondary

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .clickable { onSelectTab(tab) }
                        .padding(horizontal = 14.dp, vertical = 2.dp)
                        .testTag("nav_tab_${tab.name.lowercase()}")
                ) {
                    Box {
                        Icon(
                            imageVector = tab.icon,
                            contentDescription = tab.label,
                            tint = iconColor,
                            modifier = Modifier.size(22.dp)
                        )
                        if (tab == NavTab.DRAFTS && queuedCount > 0) {
                            Box(
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .size(14.dp)
                                    .clip(CircleShape)
                                    .background(OrangeAccent),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = queuedCount.toString(),
                                    color = Color.White,
                                    fontSize = 8.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = tab.label,
                        color = textColor,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }
        }
    }
}
