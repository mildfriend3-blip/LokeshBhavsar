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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.HyperEdgeBottomNav
import com.example.ui.components.MobileTab
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.OfflineVaultScreen
import com.example.ui.screens.ReportIssueScreen
import com.example.ui.theme.GreenSuccess
import com.example.ui.theme.OrangeAccent

@Composable
fun HyperEdgeApp(
    viewModel: HyperEdgeViewModel = viewModel()
) {
    val isOnline by viewModel.isOnline.collectAsStateWithLifecycle()
    val reports by viewModel.allReports.collectAsStateWithLifecycle()
    val isSyncing by viewModel.isSyncing.collectAsStateWithLifecycle()
    val currentToast by viewModel.currentToast.collectAsStateWithLifecycle()

    var activeTab by remember { mutableStateOf(MobileTab.DASHBOARD) }
    val queuedCount = reports.count { it.status in listOf("PENDING", "SEALED", "SYNCING") }

    Scaffold(
        bottomBar = {
            HyperEdgeBottomNav(
                currentTab = activeTab,
                queuedCount = queuedCount,
                onSelectTab = { selectedTab ->
                    activeTab = selectedTab
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (activeTab) {
                MobileTab.DASHBOARD -> {
                    DashboardScreen(
                        isOnline = isOnline,
                        onToggleOnline = { viewModel.toggleOnline() },
                        onReportIssueClick = { activeTab = MobileTab.REPORT }
                    )
                }
                MobileTab.REPORT -> {
                    ReportIssueScreen(
                        onBackClick = { activeTab = MobileTab.DASHBOARD },
                        onSaveToVault = { newReport ->
                            viewModel.saveAndEncryptReport(newReport)
                            activeTab = MobileTab.VAULT
                        }
                    )
                }
                MobileTab.VAULT -> {
                    OfflineVaultScreen(
                        reports = reports,
                        isOnline = isOnline,
                        isSyncing = isSyncing,
                        onSyncNow = { viewModel.simulateSignalReturnAndSync() }
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
                        ToastType.SUCCESS -> Color(0xFF0F172A) to Icons.Default.CheckCircle
                        ToastType.ERROR -> Color(0xFF0F172A) to Icons.Default.Error
                        ToastType.WARNING -> Color(0xFF0F172A) to Icons.Default.Warning
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(elevation = 6.dp, shape = RoundedCornerShape(10.dp))
                            .clip(RoundedCornerShape(10.dp))
                            .background(bgColor)
                            .border(1.dp, Color(0xFF334155), RoundedCornerShape(10.dp))
                            .padding(horizontal = 14.dp, vertical = 10.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = icon,
                                contentDescription = null,
                                tint = if (toast.type == ToastType.SUCCESS) GreenSuccess else OrangeAccent,
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
