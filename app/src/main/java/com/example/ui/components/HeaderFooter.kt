package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.SignalCellularAlt
import androidx.compose.material.icons.filled.SignalCellularConnectedNoInternet0Bar
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CardBorderNavy
import com.example.ui.theme.GreenSuccess
import com.example.ui.theme.NavyDark
import com.example.ui.theme.NavyPrimary
import com.example.ui.theme.OrangeAccent
import com.example.ui.theme.RedError
import com.example.ui.theme.SlateLight
import com.example.ui.theme.SlateSecondary

@Composable
fun JmcSealLogo(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(28.dp)
            .clip(CircleShape)
            .background(Color.White)
            .border(1.5.dp, OrangeAccent, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "JMC",
                color = NavyPrimary,
                fontSize = 9.sp,
                fontWeight = FontWeight.Black,
                fontFamily = FontFamily.Monospace,
                letterSpacing = (-0.5).sp
            )
            Text(
                text = "जम्मू",
                color = OrangeAccent,
                fontSize = 5.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 6.sp
            )
        }
    }
}

@Composable
fun HyperEdgeHeader(
    screenNumber: Int,
    isOnline: Boolean,
    isHindi: Boolean = false,
    onToggleOnline: () -> Unit,
    onToggleLanguage: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var activeTooltip by remember { mutableStateOf<String?>(null) }

    val infiniteTransition = rememberInfiniteTransition(label = "pulseRing")
    val ringScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.35f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "ringScale"
    )
    val ringAlpha by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 0.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "ringAlpha"
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(NavyPrimary)
    ) {
        // Main Navy Header Band
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Left: JMC Official Circular Seal + Title
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                JmcSealLogo(modifier = Modifier.padding(end = 8.dp))

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "HyperEdge",
                            color = Color.White,
                            fontWeight = FontWeight.Black,
                            fontSize = 16.sp,
                            letterSpacing = 0.5.sp
                        )
                        Text(
                            text = " | Smart City Jammu",
                            color = SlateLight,
                            fontWeight = FontWeight.Medium,
                            fontSize = 12.sp
                        )
                    }
                    Text(
                        text = if (isHindi) "जम्मू स्मार्ट सिटी · वार्ड १२ कॉरिडोर फील्ड नोड" else "JMC CORRIDOR FIELD NODE · JMC-CORRIDOR-042",
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 8.sp,
                        fontFamily = FontFamily.Monospace,
                        letterSpacing = 0.3.sp
                    )
                }
            }

            // Right: Language Switch + Pulsing Offline/Online Pill + Page Badge
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                // Language Toggle "EN | हिं"
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(3.dp))
                        .background(Color(0xFF163C5E))
                        .border(1.dp, Color(0xFF28547C), RoundedCornerShape(3.dp))
                        .clickable { onToggleLanguage() }
                        .padding(horizontal = 5.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = if (isHindi) "हिं | EN" else "EN | हिं",
                        color = Color.White,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                }

                // Signal Simulation Pill with Pulsing Ring & Slight Organic Rotation (-1.5°)
                Box(
                    modifier = Modifier
                        .rotate(if (!isOnline) -1.5f else 0f)
                        .clip(RoundedCornerShape(3.dp))
                        .background(if (isOnline) GreenSuccess else RedError)
                        .clickable { onToggleOnline() }
                        .padding(horizontal = 6.dp, vertical = 4.dp)
                        .testTag("signal_toggle_button")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            if (!isOnline) {
                                Box(
                                    modifier = Modifier
                                        .size(10.dp)
                                        .border(
                                            width = 1.dp,
                                            color = Color.White.copy(alpha = ringAlpha),
                                            shape = CircleShape
                                        )
                                )
                            }
                            Icon(
                                imageVector = if (isOnline) Icons.Default.SignalCellularAlt else Icons.Default.SignalCellularConnectedNoInternet0Bar,
                                contentDescription = if (isOnline) "Simulate Signal" else "Offline Dead-Zone",
                                tint = Color.White,
                                modifier = Modifier.size(11.dp)
                            )
                        }
                        Text(
                            text = if (isOnline) "ONLINE" else "OFFLINE",
                            color = Color.White,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Black,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }

                // Orange Page Badge "X / 8"
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(3.dp))
                        .background(OrangeAccent)
                        .padding(horizontal = 6.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "$screenNumber / 8",
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
        }

        // Sub-strip: Telemetry strip with clickable technical tooltips
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(NavyDark)
                .padding(horizontal = 12.dp, vertical = 3.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { activeTooltip = "sqlite" }
            ) {
                Box(
                    modifier = Modifier
                        .size(5.dp)
                        .clip(CircleShape)
                        .background(if (isOnline) GreenSuccess else OrangeAccent)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "SQLite Encrypted",
                    color = Color.White.copy(alpha = 0.85f),
                    fontSize = 9.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold
                )
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = "SQLite info",
                    tint = OrangeAccent,
                    modifier = Modifier
                        .size(9.dp)
                        .padding(start = 2.dp)
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { activeTooltip = "chacha20" }
            ) {
                Text(
                    text = "ChaCha20-Poly1305",
                    color = OrangeAccent,
                    fontSize = 9.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold
                )
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = "ChaCha20 info",
                    tint = OrangeAccent,
                    modifier = Modifier
                        .size(9.dp)
                        .padding(start = 2.dp)
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { activeTooltip = "mesh" }
            ) {
                Text(
                    text = "Mesh Sync",
                    color = GreenSuccess,
                    fontSize = 9.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }

    // Interactive Technical Tooltip Dialog
    if (activeTooltip != null) {
        val title = when (activeTooltip) {
            "sqlite" -> "SQLite On-Device Cryptographic Store"
            "chacha20" -> "ChaCha20-Poly1305 AEAD Encryption"
            else -> "Jammu Autonomous Mesh Protocol"
        }
        val description = when (activeTooltip) {
            "sqlite" -> "Local flash database persistence. Every citizen grievance record is immutably stored on the Android device prior to any network attempts, ensuring zero data loss in signal dead zones."
            "chacha20" -> "Authenticated Encryption with Associated Data (AEAD). Protects photographic evidence, GPS telemetry, and audio notes with Android Hardware Keyring backing."
            else -> "Zero-infrastructure opportunistic synchronizer. Automatically initiates peer-to-peer Wi-Fi Direct and BLE mesh handshakes when encountering municipal transit or JMC corridor routers."
        }

        AlertDialog(
            onDismissRequest = { activeTooltip = null },
            title = {
                Text(
                    text = title,
                    color = NavyPrimary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Black
                )
            },
            text = {
                Text(
                    text = description,
                    color = SlateSecondary,
                    fontSize = 12.sp,
                    lineHeight = 16.sp
                )
            },
            confirmButton = {
                TextButton(onClick = { activeTooltip = null }) {
                    Text("OK", color = OrangeAccent, fontWeight = FontWeight.Bold)
                }
            },
            containerColor = Color.White,
            shape = RoundedCornerShape(4.dp)
        )
    }
}

@Composable
fun HyperEdgeFooter(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(NavyPrimary)
            .padding(vertical = 4.dp, horizontal = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "HyperEdge · Team HyperEdge · Smart City Jammu",
            color = Color.White.copy(alpha = 0.75f),
            fontSize = 9.sp,
            fontWeight = FontWeight.Medium,
            letterSpacing = 0.4.sp
        )
    }
}

@Composable
fun RuggedCard(
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color.White,
    borderColor: Color = CardBorderNavy,
    borderWidth: Float = 1f,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .border(width = borderWidth.dp, color = borderColor, shape = RoundedCornerShape(4.dp))
            .clip(RoundedCornerShape(4.dp))
            .background(backgroundColor)
            .padding(12.dp)
    ) {
        content()
    }
}
