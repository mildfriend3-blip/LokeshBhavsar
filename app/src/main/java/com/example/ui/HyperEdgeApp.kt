package com.example.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
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
import androidx.compose.ui.geometry.Offset
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
import com.example.ui.theme.CreamBackground
import com.example.ui.theme.GreenSuccess
import com.example.ui.theme.OrangeAccent
import com.example.ui.theme.RedError

@Composable
fun HyperEdgeApp(
    viewModel: HyperEdgeViewModel = viewModel()
) {
    val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()
    val isOnline by viewModel.isOnline.collectAsStateWithLifecycle()
    val isHindi by viewModel.isHindi.collectAsStateWithLifecycle()
    val reports by viewModel.allReports.collectAsStateWithLifecycle()
    val latestReport by viewModel.latestReport.collectAsStateWithLifecycle()
    val isSyncing by viewModel.isSyncing.collectAsStateWithLifecycle()
    val syncLogs by viewModel.syncLogs.collectAsStateWithLifecycle()
    val currentToast by viewModel.currentToast.collectAsStateWithLifecycle()

    val queuedCount = reports.count { it.status in listOf("PENDING", "SEALED", "SYNCING") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE4DFCC)), // Outer neutral framing on wider screens
        contentAlignment = Alignment.Center
    ) {
        // Mobile-first frame container (360x800 viewport focus, centered on larger displays)
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .widthIn(max = 440.dp)
                .fillMaxWidth()
                .background(CreamBackground)
        ) {
            // CHANGE 2: Subtle paper grain / noise texture overlay on cream background (opacity 0.04)
            Canvas(modifier = Modifier.fillMaxSize()) {
                val step = 10.dp.toPx()
                var x = 2.dp.toPx()
                while (x < size.width) {
                    var y = 2.dp.toPx()
                    while (y < size.height) {
                        val jitterX = ((x.toInt() * 17 + y.toInt() * 31) % 5).toFloat()
                        val jitterY = ((y.toInt() * 23 + x.toInt() * 11) % 5).toFloat()
                        drawCircle(
                            color = Color(0x0A0F2B46), // 0.04 opacity organic stipple
                            radius = 0.75.dp.toPx(),
                            center = Offset(x + jitterX, y + jitterY)
                        )
                        y += step
                    }
                    x += step
                }
            }

            Scaffold(
                bottomBar = {
                    // FIX 1: Clean authentic bottom navigation (no debug strip!)
                    if (currentScreen != 1) {
                        HyperEdgeBottomNav(
                            currentScreen = currentScreen,
                            queuedCount = queuedCount,
                            onSelectTab = { tab ->
                                viewModel.setScreen(tab.targetScreen)
                            }
                        )
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
                            isHindi = isHindi,
                            onToggleOnline = { viewModel.toggleOnline() },
                            onToggleLanguage = { viewModel.toggleLanguage() },
                            onNext = { viewModel.setScreen(3) }
                        )
                        3 -> SolutionScreen(
                            isOnline = isOnline,
                            isHindi = isHindi,
                            onToggleOnline = { viewModel.toggleOnline() },
                            onToggleLanguage = { viewModel.toggleLanguage() },
                            onProceedToFileGrievance = { viewModel.setScreen(4) }
                        )
                        4 -> FileGrievanceScreen(
                            isOnline = isOnline,
                            isHindi = isHindi,
                            onToggleOnline = { viewModel.toggleOnline() },
                            onToggleLanguage = { viewModel.toggleLanguage() },
                            onSaveAndEncrypt = { report ->
                                viewModel.saveAndEncryptReport(report)
                            }
                        )
                        5 -> EdgeAiValidationScreen(
                            latestReport = latestReport,
                            isOnline = isOnline,
                            isHindi = isHindi,
                            onToggleOnline = { viewModel.toggleOnline() },
                            onToggleLanguage = { viewModel.toggleLanguage() },
                            onRetakePhoto = { viewModel.setScreen(4) },
                            onProceedToStorage = { viewModel.setScreen(6) }
                        )
                        6 -> ReportsSyncScreen(
                            reports = reports,
                            isOnline = isOnline,
                            isHindi = isHindi,
                            onToggleOnline = { viewModel.toggleOnline() },
                            onToggleLanguage = { viewModel.toggleLanguage() },
                            onForceSync = { viewModel.setScreen(7) },
                            onNewReport = { viewModel.setScreen(4) }
                        )
                        7 -> SyncQueueScreen(
                            reports = reports,
                            isOnline = isOnline,
                            isHindi = isHindi,
                            isSyncing = isSyncing,
                            syncLogs = syncLogs,
                            onToggleOnline = { viewModel.toggleOnline() },
                            onToggleLanguage = { viewModel.toggleLanguage() },
                            onSimulateSignalReturn = { viewModel.simulateSignalReturnAndSync() },
                            onProceedToConsole = { viewModel.setScreen(8) }
                        )
                        8 -> MunicipalConsoleScreen(
                            reports = reports,
                            isOnline = isOnline,
                            isHindi = isHindi,
                            onToggleOnline = { viewModel.toggleOnline() },
                            onToggleLanguage = { viewModel.toggleLanguage() }
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
