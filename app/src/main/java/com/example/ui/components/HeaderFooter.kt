package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
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
import androidx.compose.ui.draw.scale
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

    // 2s expanding orange ring animation loop for OFFLINE badge
    val infiniteTransition = rememberInfiniteTransition(label = "pulseRing")
    val ringScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.38f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "ringScale"
    )
    val ringAlpha by infiniteTransition.animateFloat(
        initialValue = 0.9f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
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
                JmcSealLogo(modifier = Modifier.padding(end = 6.dp))

                Column(modifier = Modifier.padding(end = 4.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "HyperEdge",
                            color = Color.White,
                            fontWeight = FontWeight.Black,
                            fontSize = 15.sp,
                            letterSpacing = 0.3.sp,
                            maxLines = 1
                        )
                        Text(
                            text = " | Jammu",
                            color = SlateLight,
                            fontWeight = FontWeight.Medium,
                            fontSize = 11.sp,
                            maxLines = 1
                        )
                    }
                    Text(
                        text = if (isHindi) "वार्ड १२ · फील्ड नोड #०४२" else "WARD 12 FIELD NODE · #042",
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 8.sp,
                        fontFamily = FontFamily.Monospace,
                        letterSpacing = 0.2.sp,
                        maxLines = 1
                    )
                }
            }

            // Right: Language Switch + Pulsing Offline/Online Pill (-2° rotated) + Page Badge
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                // Language Toggle "EN | हिं" (EN active, हिं inactive)
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(3.dp))
                        .background(Color(0xFF163C5E))
                        .border(1.dp, Color(0xFF28547C), RoundedCornerShape(3.dp))
                        .clickable { onToggleLanguage() }
                        .padding(horizontal = 5.dp, vertical = 3.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "EN",
                            color = if (!isHindi) OrangeAccent else SlateLight,
                            fontSize = 9.sp,
                            fontWeight = if (!isHindi) FontWeight.Black else FontWeight.Normal,
                            fontFamily = FontFamily.Monospace
                        )
                        Text(
                            text = " | ",
                            color = Color.White.copy(alpha = 0.5f),
                            fontSize = 8.sp
                        )
                        Text(
                            text = "हिं",
                            color = if (isHindi) OrangeAccent else SlateLight,
                            fontSize = 9.sp,
                            fontWeight = if (isHindi) FontWeight.Black else FontWeight.Normal
                        )
                    }
                }

                // Signal Simulation Pill with Orange Ring Animation & -2 degrees rotation
                Box(contentAlignment = Alignment.Center) {
                    if (!isOnline) {
                        Box(
                            modifier = Modifier
                                .matchParentSize()
                                .rotate(-2f)
                                .scale(ringScale)
                                .border(
                                    width = 1.2.dp,
                                    color = OrangeAccent.copy(alpha = ringAlpha),
                                    shape = RoundedCornerShape(3.dp)
                                )
                        )
                    }

                    Box(
                        modifier = Modifier
                            .rotate(if (!isOnline) -2f else 0f) // CHANGE 4: -2 degrees rotation
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
                            Icon(
                                imageVector = if (isOnline) Icons.Default.SignalCellularAlt else Icons.Default.SignalCellularConnectedNoInternet0Bar,
                                contentDescription = if (isOnline) "Simulate Signal" else "Offline Dead-Zone",
                                tint = Color.White,
                                modifier = Modifier.size(11.dp)
                            )
                            Text(
                                text = if (isOnline) "ONLINE" else "OFFLINE",
                                color = Color.White,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Black,
                                fontFamily = FontFamily.Monospace
                            )
                        }
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
            "sqlite" -> "Encrypted on-device database"
            "chacha20" -> "Military-grade encryption"
            else -> "Autonomous Mesh Protocol"
        }
        val description = when (activeTooltip) {
            "sqlite" -> "Encrypted on-device database: Every civic complaint is immutably written to onboard flash storage before any remote network call is made. Zero data loss in signal dead zones."
            "chacha20" -> "Military-grade encryption: ChaCha20-Poly1305 AEAD authenticated cipher protects citizen photos, GPS coordinates, and voice notes with hardware keyring backing."
            else -> "Autonomous peer-to-peer mesh sync: Dispatches delta payloads opportunistically to nearby municipal transit nodes and field routers."
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
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(NavyPrimary)
            .padding(vertical = 4.dp, horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "HyperEdge · Team HyperEdge · Smart City Jammu",
            color = Color.White.copy(alpha = 0.85f),
            fontSize = 9.sp,
            fontWeight = FontWeight.Medium,
            letterSpacing = 0.4.sp
        )
        // CHANGE 5: "v0.4.2 · build 128" tiny grey text at the very bottom
        Text(
            text = "v0.4.2 · build 128",
            color = SlateLight.copy(alpha = 0.7f),
            fontSize = 8.sp,
            fontFamily = FontFamily.Monospace,
            letterSpacing = 0.5.sp
        )
    }
}

@Composable
fun RuggedCard(
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color.White,
    borderColor: Color = CardBorderNavy,
    borderWidth: Float = 1f,
    cornerRadius: Int = 4,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .border(width = borderWidth.dp, color = borderColor, shape = RoundedCornerShape(cornerRadius.dp))
            .clip(RoundedCornerShape(cornerRadius.dp))
            .background(backgroundColor)
            .padding(12.dp)
    ) {
        content()
    }
}
