package com.example.ui.screens

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.CellTower
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.SignalCellularAlt
import androidx.compose.material.icons.filled.SignalCellularConnectedNoInternet0Bar
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.GrievanceReport
import com.example.ui.components.HyperEdgeFooter
import com.example.ui.components.HyperEdgeHeader
import com.example.ui.components.RuggedCard
import com.example.ui.theme.CardBorderNavy
import com.example.ui.theme.CreamBackground
import com.example.ui.theme.GreenSuccess
import com.example.ui.theme.GreenTint
import com.example.ui.theme.NavyDark
import com.example.ui.theme.NavyPrimary
import com.example.ui.theme.OrangeAccent
import com.example.ui.theme.RedError
import com.example.ui.theme.RedTint
import com.example.ui.theme.SlateLight
import com.example.ui.theme.SlateSecondary

@Composable
fun SyncQueueScreen(
    reports: List<GrievanceReport>,
    isOnline: Boolean,
    isHindi: Boolean = false,
    isSyncing: Boolean,
    syncLogs: List<String>,
    onToggleOnline: () -> Unit,
    onToggleLanguage: () -> Unit = {},
    onSimulateSignalReturn: () -> Unit,
    onProceedToConsole: () -> Unit,
    modifier: Modifier = Modifier
) {
    val totalCount = reports.size
    val syncedCount = reports.count { it.status == "SYNCED" }
    val progress = if (totalCount > 0) syncedCount.toFloat() / totalCount else 1f

    val infiniteTransition = rememberInfiniteTransition(label = "spin")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(CreamBackground)
    ) {
        HyperEdgeHeader(
            screenNumber = 7,
            isOnline = isOnline,
            isHindi = isHindi,
            onToggleOnline = onToggleOnline,
            onToggleLanguage = onToggleLanguage
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(14.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = if (isHindi) "सिंक कतार / Sync Queue" else "Sync Queue / सिंक कतार",
                        color = NavyPrimary,
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Black
                    )
                    Text(
                        text = "PEER-TO-PEER MESH HANDSHAKE · JMC UPLINK",
                        color = SlateSecondary,
                        fontSize = 9.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold
                    )
                }

                if (isSyncing) {
                    Icon(
                        imageVector = Icons.Default.Sync,
                        contentDescription = "Syncing",
                        tint = OrangeAccent,
                        modifier = Modifier
                            .size(24.dp)
                            .rotate(rotation)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // FIX 6: "Last synced 4 min ago" with refresh icon
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { onSimulateSignalReturn() }
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Refresh",
                        tint = SlateSecondary,
                        modifier = Modifier.size(13.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Last synced 4 min ago (Node JMC-CORRIDOR-042)",
                        color = SlateSecondary,
                        fontSize = 10.sp,
                        fontFamily = FontFamily.Monospace
                    )
                }

                Text(
                    text = "DELTA SYNC ACTIVE",
                    color = GreenSuccess,
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Status Card
            val cardBg = if (isOnline) GreenTint else RedTint
            val cardBorder = if (isOnline) GreenSuccess else RedError
            val statusColor = if (isOnline) GreenSuccess else RedError
            val statusText = if (isOnline) "MESH LINK ACTIVE · SYNCED" else "DEAD-ZONE ACTIVE · QUEUED"

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(4.dp))
                    .border(1.5.dp, cardBorder, RoundedCornerShape(4.dp))
                    .background(cardBg)
                    .padding(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .clip(CircleShape)
                                .background(statusColor)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = statusText,
                                color = statusColor,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Black,
                                fontFamily = FontFamily.Monospace
                            )
                            Text(
                                text = if (isOnline) "Field node authenticated with JMC optical backbone" else "Records encrypted & buffered on device flash partition",
                                color = SlateSecondary,
                                fontSize = 10.sp
                            )
                        }
                    }

                    Icon(
                        imageVector = if (isOnline) Icons.Default.SignalCellularAlt else Icons.Default.SignalCellularConnectedNoInternet0Bar,
                        contentDescription = null,
                        tint = statusColor,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Progress Bar Card: "X of Y reports synced"
            RuggedCard(backgroundColor = Color.White) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "BATCH PROGRESSION:",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace,
                            color = NavyPrimary,
                            letterSpacing = 0.5.sp
                        )
                        Text(
                            text = "$syncedCount of $totalCount reports synced",
                            color = if (syncedCount == totalCount) GreenSuccess else OrangeAccent,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Black,
                            fontFamily = FontFamily.Monospace
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = if (progress >= 1f) GreenSuccess else OrangeAccent,
                        trackColor = Color(0xFFE9E5DC),
                    )

                    // CHANGE 5: Small italic grey text below progress bar
                    Text(
                        text = "swipe left to view individual packets",
                        color = SlateSecondary,
                        fontSize = 9.sp,
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                        modifier = Modifier.padding(top = 3.dp)
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "EST. CORRIDOR LATENCY: ${if (isOnline) "48 ms" else "OFFLINE BUFFER"}",
                            color = SlateSecondary,
                            fontSize = 8.sp,
                            fontFamily = FontFamily.Monospace
                        )
                        Text(
                            text = "EXPONENTIAL BACKOFF ACTIVE",
                            color = SlateSecondary,
                            fontSize = 8.sp,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Live Log Feed (Monospace Terminal)
            Text(
                text = "REAL-TIME TELEMETRY LOG (CHACHA20 ENVELOPE):",
                color = NavyPrimary,
                fontSize = 10.sp,
                fontWeight = FontWeight.Black,
                fontFamily = FontFamily.Monospace,
                letterSpacing = 0.5.sp,
                modifier = Modifier.padding(bottom = 4.dp)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(NavyDark)
                    .border(1.dp, CardBorderNavy, RoundedCornerShape(4.dp))
                    .padding(10.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    syncLogs.forEach { logLine ->
                        val logColor = when {
                            logLine.contains("pushed") || logLine.contains("verified") || logLine.contains("COMPLETE") || logLine.contains("ACK") -> GreenSuccess
                            logLine.contains("assembled") || logLine.contains("initiated") -> OrangeAccent
                            logLine.contains("OFFLINE") || logLine.contains("WAIT") -> RedError
                            else -> Color.White.copy(alpha = 0.85f)
                        }
                        Text(
                            text = logLine,
                            color = logColor,
                            fontSize = 9.sp,
                            fontFamily = FontFamily.Monospace,
                            lineHeight = 13.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Primary Action: "Simulate Signal Return" button
            Button(
                onClick = onSimulateSignalReturn,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isOnline && syncedCount == totalCount) GreenSuccess else OrangeAccent
                ),
                shape = RoundedCornerShape(4.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(46.dp)
                    .testTag("simulate_signal_return_button")
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = if (syncedCount == totalCount) Icons.Default.CheckCircle else Icons.Default.CellTower,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = if (syncedCount == totalCount) "ALL REPORTS SYNCHRONIZED ✔" else "SIMULATE SIGNAL RETURN & SYNC",
                        color = Color.White,
                        fontWeight = FontWeight.Black,
                        fontSize = 12.sp,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Secondary Action: Proceed to Command Console
            Button(
                onClick = onProceedToConsole,
                colors = ButtonDefaults.buttonColors(containerColor = NavyPrimary),
                shape = RoundedCornerShape(4.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp)
                    .testTag("proceed_to_console_button")
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "MUNICIPAL COMMAND CONSOLE (8/8)",
                        color = Color.White,
                        fontWeight = FontWeight.Black,
                        fontSize = 12.sp,
                        fontFamily = FontFamily.Monospace
                    )
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }

        HyperEdgeFooter()
    }
}
