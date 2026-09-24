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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.OrangeAccent
import com.example.ui.theme.SlateLight
import com.example.ui.theme.SlateSecondary

enum class MobileTab {
    HOME,
    REPORT,
    VAULT
}

@Composable
fun HyperEdgeBottomNav(
    currentTab: MobileTab,
    queuedCount: Int,
    onSelectTab: (MobileTab) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
            .shadow(elevation = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Tab 1: Home
            val isHome = currentTab == MobileTab.HOME
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .clickable { onSelectTab(MobileTab.HOME) }
                    .padding(horizontal = 16.dp, vertical = 4.dp)
                    .testTag("nav_tab_home")
            ) {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "Home",
                    tint = if (isHome) OrangeAccent else SlateSecondary,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Home",
                    fontSize = 11.sp,
                    fontWeight = if (isHome) FontWeight.Bold else FontWeight.Medium,
                    color = if (isHome) OrangeAccent else SlateSecondary
                )
            }

            // Tab 2: Center Floating Action Button (FAB) for Report Issue
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .offset(y = (-14).dp)
                    .clickable { onSelectTab(MobileTab.REPORT) }
                    .testTag("nav_tab_report")
            ) {
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .shadow(elevation = 6.dp, shape = CircleShape)
                        .clip(CircleShape)
                        .background(OrangeAccent),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Report Issue",
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Report",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F172A)
                )
            }

            // Tab 3: My Vault
            val isVault = currentTab == MobileTab.VAULT
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .clickable { onSelectTab(MobileTab.VAULT) }
                    .padding(horizontal = 16.dp, vertical = 4.dp)
                    .testTag("nav_tab_vault")
            ) {
                Box {
                    Icon(
                        imageVector = Icons.Default.Archive,
                        contentDescription = "My Vault",
                        tint = if (isVault) OrangeAccent else SlateSecondary,
                        modifier = Modifier.size(22.dp)
                    )
                    if (queuedCount > 0) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .offset(x = 6.dp, y = (-4).dp)
                                .size(16.dp)
                                .clip(CircleShape)
                                .background(OrangeAccent),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = queuedCount.toString(),
                                color = Color.White,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "My Vault",
                    fontSize = 11.sp,
                    fontWeight = if (isVault) FontWeight.Bold else FontWeight.Medium,
                    color = if (isVault) OrangeAccent else SlateSecondary
                )
            }
        }
    }
}
