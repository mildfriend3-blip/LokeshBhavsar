package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
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
import com.example.ui.screens.CreamBg
import com.example.ui.screens.NavyBrand
import com.example.ui.screens.OrangeSingle
import com.example.ui.screens.SlateInactive

enum class MobileTab {
    HOME,
    REPORT,
    DRAFTS,
    PROFILE
}

@Composable
fun HyperEdgeBottomNav(
    currentTab: MobileTab,
    draftsCount: Int = 4,
    onSelectTab: (MobileTab) -> Unit,
    modifier: Modifier = Modifier
) {
    // Full width, koi inset box nahi, cream bg, 1px navy border-top
    Box(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(CreamBg)
        ) {
            // 1px navy border-top
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(NavyBrand)
            )

            // 64px content height
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Tab 1: Home
                val isHome = currentTab == MobileTab.HOME
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .clickable { onSelectTab(MobileTab.HOME) }
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                        .testTag("nav_tab_home")
                ) {
                    Icon(
                        imageVector = Icons.Default.Home,
                        contentDescription = "Home",
                        tint = if (isHome) OrangeSingle else SlateInactive,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Home",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = if (isHome) OrangeSingle else SlateInactive
                    )
                }

                // Space allocated for center protruding FAB
                Spacer(modifier = Modifier.size(56.dp))

                // Tab 3: Drafts (with Badge 4)
                val isDrafts = currentTab == MobileTab.DRAFTS
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .clickable { onSelectTab(MobileTab.DRAFTS) }
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                        .testTag("nav_tab_drafts")
                ) {
                    Box {
                        Icon(
                            imageVector = Icons.Default.Description,
                            contentDescription = "Drafts",
                            tint = if (isDrafts) OrangeSingle else SlateInactive,
                            modifier = Modifier.size(22.dp)
                        )
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .offset(x = 6.dp, y = (-4).dp)
                                .size(16.dp)
                                .clip(CircleShape)
                                .background(OrangeSingle),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = draftsCount.toString(),
                                color = Color.White,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Drafts",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = if (isDrafts) OrangeSingle else SlateInactive
                    )
                }

                // Tab 4: Profile
                val isProfile = currentTab == MobileTab.PROFILE
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .clickable { onSelectTab(MobileTab.PROFILE) }
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                        .testTag("nav_tab_profile")
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Profile",
                        tint = if (isProfile) OrangeSingle else SlateInactive,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Profile",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = if (isProfile) OrangeSingle else SlateInactive
                    )
                }
            }
        }

        // Center FAB: 56px orange #E8760C circle FAB, only 16px protruding above nav, with 4px cream ring
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = (-16).dp)
                .clickable { onSelectTab(MobileTab.REPORT) }
                .testTag("nav_tab_report")
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .border(width = 4.dp, color = CreamBg, shape = CircleShape)
                    .background(OrangeSingle)
                    .shadow(elevation = 2.dp, shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Report",
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
            }
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "Report",
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                color = if (currentTab == MobileTab.REPORT) OrangeSingle else SlateInactive
            )
        }
    }
}
