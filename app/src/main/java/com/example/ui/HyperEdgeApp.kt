package com.example.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.HyperEdgeBottomNav
import com.example.ui.components.MobileTab
import com.example.ui.screens.CreamBg
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.NavyBrand
import com.example.ui.screens.OfflineVaultScreen
import com.example.ui.screens.OrangeSingle
import com.example.ui.screens.ReportIssueScreen
import com.example.ui.screens.SlateInactive
import com.example.ui.theme.GreenSuccess

@Composable
fun HyperEdgeApp(
    viewModel: HyperEdgeViewModel = viewModel()
) {
    val isOnline by viewModel.isOnline.collectAsStateWithLifecycle()
    val reports by viewModel.allReports.collectAsStateWithLifecycle()
    val isSyncing by viewModel.isSyncing.collectAsStateWithLifecycle()
    val currentToast by viewModel.currentToast.collectAsStateWithLifecycle()

    var activeTab by remember { mutableStateOf(MobileTab.HOME) }
    val draftsCount = 4

    Scaffold(
        bottomBar = {
            HyperEdgeBottomNav(
                currentTab = activeTab,
                draftsCount = draftsCount,
                onSelectTab = { selectedTab ->
                    activeTab = selectedTab
                }
            )
        },
        containerColor = CreamBg
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(CreamBg)
        ) {
            when (activeTab) {
                MobileTab.HOME -> {
                    DashboardScreen(
                        isOnline = isOnline,
                        onToggleOnline = { viewModel.toggleOnline() }
                    )
                }
                MobileTab.REPORT -> {
                    ReportIssueScreen(
                        onBackClick = { activeTab = MobileTab.HOME },
                        onSaveToVault = { newReport ->
                            viewModel.saveAndEncryptReport(newReport)
                            activeTab = MobileTab.DRAFTS
                        }
                    )
                }
                MobileTab.DRAFTS -> {
                    OfflineVaultScreen(
                        reports = reports,
                        isOnline = isOnline,
                        isSyncing = isSyncing,
                        onSyncNow = { viewModel.simulateSignalReturnAndSync() }
                    )
                }
                MobileTab.PROFILE -> {
                    ProfilePlaceholder(
                        onBack = { activeTab = MobileTab.HOME }
                    )
                }
            }

            // Floating Toast
            AnimatedVisibility(
                visible = currentToast != null,
                enter = slideInVertically(initialOffsetY = { -it }) + fadeIn(),
                exit = slideOutVertically(targetOffsetY = { -it }) + fadeOut(),
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 12.dp, start = 16.dp, end = 16.dp)
            ) {
                currentToast?.let { toast ->
                    val (bgColor, icon) = when (toast.type) {
                        ToastType.SUCCESS -> NavyBrand to Icons.Default.CheckCircle
                        ToastType.ERROR -> NavyBrand to Icons.Default.Error
                        ToastType.WARNING -> NavyBrand to Icons.Default.Warning
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(elevation = 4.dp, shape = RoundedCornerShape(4.dp))
                            .clip(RoundedCornerShape(4.dp))
                            .background(bgColor)
                            .border(1.dp, NavyBrand, RoundedCornerShape(4.dp))
                            .padding(horizontal = 14.dp, vertical = 10.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = icon,
                                contentDescription = null,
                                tint = if (toast.type == ToastType.SUCCESS) GreenSuccess else OrangeSingle,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = toast.message,
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProfilePlaceholder(
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CreamBg)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Citizen Profile",
            fontSize = 20.sp,
            fontWeight = FontWeight.ExtraBold,
            color = NavyBrand
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(1.dp, RoundedCornerShape(4.dp))
                .clip(RoundedCornerShape(4.dp))
                .border(1.dp, NavyBrand, RoundedCornerShape(4.dp))
                .background(Color.White)
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(NavyBrand),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = CreamBg,
                        modifier = Modifier.size(28.dp)
                    )
                }

                Column {
                    Text(
                        text = "Field Officer #042",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = NavyBrand
                    )
                    Text(
                        text = "Ward 12 · Trikuta Nagar Sub-Division",
                        fontSize = 12.sp,
                        color = SlateInactive
                    )
                    Text(
                        text = "Device Node: JMC-CORRIDOR-042",
                        fontSize = 10.sp,
                        fontFamily = FontFamily.Monospace,
                        color = OrangeSingle
                    )
                }
            }
        }
    }
}
