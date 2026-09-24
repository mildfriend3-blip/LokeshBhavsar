package com.example.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.HyperEdgeBottomNav
import com.example.ui.components.NavTab
import com.example.ui.screens.EdgeAiValidationScreen
import com.example.ui.screens.FileGrievanceScreen
import com.example.ui.screens.MunicipalConsoleScreen
import com.example.ui.screens.ProblemScreen
import com.example.ui.screens.ReportsSyncScreen
import com.example.ui.screens.SolutionScreen
import com.example.ui.screens.SplashScreen
import com.example.ui.screens.SyncQueueScreen
import com.example.ui.theme.CardBorderNavy
import com.example.ui.theme.CreamBackground
import com.example.ui.theme.GreenSuccess
import com.example.ui.theme.NavyDark
import com.example.ui.theme.NavyPrimary
import com.example.ui.theme.OrangeAccent
import com.example.ui.theme.RedError

@Composable
fun HyperEdgeApp(
    viewModel: HyperEdgeViewModel = viewModel()
) {
    val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()
    val isOnline by viewModel.isOnline.collectAsStateWithLifecycle()
    val reports by viewModel.allReports.collectAsStateWithLifecycle()
    val latestReport by viewModel.latestReport.collectAsStateWithLifecycle()
    val isSyncing by viewModel.isSyncing.collectAsStateWithLifecycle()
    val syncLogs by viewModel.syncLogs.collectAsStateWithLifecycle()
    val currentToast by viewModel.currentToast.collectAsStateWithLifecycle()

    val queuedCount = reports.count { it.status in listOf("PENDING", "SEALED", "SYNCING") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE5DFC9)), // Outer container for tablet / desktop framing
        contentAlignment = Alignment.Center
    ) {
        // Mobile-first frame container (360x800 viewport focus, centered on larger displays)
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .widthIn(max = 480.dp)
                .fillMaxWidth()
                .background(CreamBackground)
        ) {
            Scaffold(
                bottomBar = {
                    if (currentScreen != 1) {
                        Column {
                            // Quick Screen Jumper Strip for effortless testing of all 8 screens
                            ScreenJumperStrip(
                                currentScreen = currentScreen,
                                onSelectScreen = { viewModel.setScreen(it) }
                            )

                            HyperEdgeBottomNav(
                                currentScreen = currentScreen,
                                queuedCount = queuedCount,
                                onSelectTab = { tab ->
                                    viewModel.setScreen(tab.targetScreen)
                                }
                            )
                        }
                    }
                }
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    when (currentScreen) {
                        1 -> SplashScreen(
                            onContinue = { viewModel.setScreen(2) }
                        )
                        2 -> ProblemScreen(
                            isOnline = isOnline,
                            onToggleOnline = { viewModel.toggleOnline() },
                            onNext = { viewModel.setScreen(3) }
                        )
                        3 -> SolutionScreen(
                            isOnline = isOnline,
                            onToggleOnline = { viewModel.toggleOnline() },
                            onProceedToFileGrievance = { viewModel.setScreen(4) }
                        )
                        4 -> FileGrievanceScreen(
                            isOnline = isOnline,
                            onToggleOnline = { viewModel.toggleOnline() },
                            onSaveAndEncrypt = { report ->
                                viewModel.saveAndEncryptReport(report)
                            }
                        )
                        5 -> EdgeAiValidationScreen(
                            latestReport = latestReport,
                            isOnline = isOnline,
                            onToggleOnline = { viewModel.toggleOnline() },
                            onRetakePhoto = { viewModel.setScreen(4) },
                            onProceedToStorage = { viewModel.setScreen(6) }
                        )
                        6 -> ReportsSyncScreen(
                            reports = reports,
                            isOnline = isOnline,
                            onToggleOnline = { viewModel.toggleOnline() },
                            onForceSync = { viewModel.setScreen(7) },
                            onNewReport = { viewModel.setScreen(4) }
                        )
                        7 -> SyncQueueScreen(
                            reports = reports,
                            isOnline = isOnline,
                            isSyncing = isSyncing,
                            syncLogs = syncLogs,
                            onToggleOnline = { viewModel.toggleOnline() },
                            onSimulateSignalReturn = { viewModel.simulateSignalReturnAndSync() },
                            onProceedToConsole = { viewModel.setScreen(8) }
                        )
                        8 -> MunicipalConsoleScreen(
                            reports = reports,
                            isOnline = isOnline,
                            onToggleOnline = { viewModel.toggleOnline() }
                        )
                    }

                    // Toast Overlay
                    AnimatedVisibility(
                        visible = currentToast != null,
                        enter = slideInVertically(initialOffsetY = { -it }) + fadeIn(),
                        exit = slideOutVertically(targetOffsetY = { -it }) + fadeOut(),
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .padding(top = 8.dp, start = 16.dp, end = 16.dp)
                    ) {
                        currentToast?.let { toast ->
                            val (bgColor, icon, border) = when (toast.type) {
                                ToastType.SUCCESS -> Triple(Color(0xFF0F3B24), Icons.Default.CheckCircle, GreenSuccess)
                                ToastType.ERROR -> Triple(Color(0xFF4A1410), Icons.Default.Error, RedError)
                                ToastType.WARNING -> Triple(Color(0xFF422409), Icons.Default.Warning, OrangeAccent)
                            }

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(bgColor)
                                    .border(1.5.dp, border, RoundedCornerShape(4.dp))
                                    .clickable { viewModel.dismissToast() }
                                    .padding(horizontal = 12.dp, vertical = 9.dp)
                                    .testTag("app_toast")
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(
                                        modifier = Modifier.weight(1f),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = icon,
                                            contentDescription = null,
                                            tint = border,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = toast.message,
                                            color = Color.White,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            fontFamily = FontFamily.Monospace
                                        )
                                    }
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Dismiss",
                                        tint = Color.White.copy(alpha = 0.7f),
                                        modifier = Modifier.size(14.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ScreenJumperStrip(
    currentScreen: Int,
    onSelectScreen: (Int) -> Unit
) {
    val screenLabels = listOf(
        1 to "1 Splash",
        2 to "2 Problem",
        3 to "3 Solution",
        4 to "4 Report",
        5 to "5 AI Check",
        6 to "6 Ledger",
        7 to "7 Sync",
        8 to "8 Console"
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(NavyDark)
            .border(width = 0.5.dp, color = CardBorderNavy)
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 6.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "SCREENS:",
            color = Color.White.copy(alpha = 0.6f),
            fontSize = 8.sp,
            fontWeight = FontWeight.Black,
            fontFamily = FontFamily.Monospace,
            modifier = Modifier.padding(end = 2.dp)
        )

        screenLabels.forEach { (num, label) ->
            val isSelected = currentScreen == num
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(2.dp))
                    .background(if (isSelected) OrangeAccent else Color(0xFF132B42))
                    .clickable { onSelectScreen(num) }
                    .padding(horizontal = 6.dp, vertical = 3.dp)
                    .testTag("screen_jump_$num")
            ) {
                Text(
                    text = label,
                    color = if (isSelected) Color.White else Color.White.copy(alpha = 0.75f),
                    fontSize = 9.sp,
                    fontWeight = if (isSelected) FontWeight.Black else FontWeight.Medium,
                    fontFamily = FontFamily.Monospace
                )
            }
        }
    }
}
