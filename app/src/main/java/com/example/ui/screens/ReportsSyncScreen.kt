package com.example.ui.screens

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.WaterDamage
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.GrievanceReport
import com.example.ui.components.HyperEdgeFooter
import com.example.ui.components.HyperEdgeHeader
import com.example.ui.theme.CardBorderNavy
import com.example.ui.theme.CreamBackground
import com.example.ui.theme.GreenSuccess
import com.example.ui.theme.GreenTint
import com.example.ui.theme.NavyDark
import com.example.ui.theme.NavyPrimary
import com.example.ui.theme.OrangeAccent
import com.example.ui.theme.OrangeTint
import com.example.ui.theme.RedError
import com.example.ui.theme.RedTint
import com.example.ui.theme.SlateLight
import com.example.ui.theme.SlateSecondary

@Composable
fun ReportsSyncScreen(
    reports: List<GrievanceReport>,
    isOnline: Boolean,
    isHindi: Boolean = false,
    onToggleOnline: () -> Unit,
    onToggleLanguage: () -> Unit = {},
    onForceSync: () -> Unit,
    onNewReport: () -> Unit,
    modifier: Modifier = Modifier
) {
    var tooltipTerm by remember { mutableStateOf<String?>(null) }
    val queuedCount = reports.count { it.status in listOf("PENDING", "SEALED", "SYNCING") }
    val syncedCount = reports.count { it.status == "SYNCED" }
    val syncedKb = syncedCount * 47

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(CreamBackground)
    ) {
        HyperEdgeHeader(
            screenNumber = 6,
            isOnline = isOnline,
            isHindi = isHindi,
            onToggleOnline = onToggleOnline,
            onToggleLanguage = onToggleLanguage
        )

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 14.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(10.dp))

                // Screen Header (Bilingual)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = if (isHindi) "मेरी शिकायतें एवं ऑफ़लाइन सिंक" else "My Reports & Offline Sync",
                            color = NavyPrimary,
                            fontSize = 19.sp,
                            fontWeight = FontWeight.Black
                        )
                        Text(
                            text = "जम्मू नगर निगम · LOCAL CRYPTOGRAPHIC LEDGER",
                            color = SlateSecondary,
                            fontSize = 9.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Dual Status Pills (with -1.5° rotation on dead-zone badge)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Pill 1: Red DEAD-ZONE ACTIVE / Green SIGNAL RESTORED
                    Box(
                        modifier = Modifier
                            .rotate(if (!isOnline) -1.5f else 0f)
                            .clip(RoundedCornerShape(3.dp))
                            .background(if (isOnline) GreenTint else RedTint)
                            .border(1.dp, if (isOnline) GreenSuccess else RedError, RoundedCornerShape(3.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .clip(CircleShape)
                                    .background(if (isOnline) GreenSuccess else RedError)
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                text = if (isOnline) "SIGNAL LINK RESTORED" else "DEAD-ZONE ACTIVE",
                                color = if (isOnline) GreenSuccess else RedError,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Black,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }

                    // Pill 2: Green HARDWARE KEY SEALED
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(3.dp))
                            .background(GreenTint)
                            .border(1.dp, GreenSuccess, RoundedCornerShape(3.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Shield,
                                contentDescription = null,
                                tint = GreenSuccess,
                                modifier = Modifier.size(11.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "HARDWARE KEY SEALED",
                                color = GreenSuccess,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Black,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Big Card: Local Cryptographic Ledger (SQLite Encrypted) with RED RUBBER STAMP
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(4.dp))
                        .border(1.5.dp, CardBorderNavy, RoundedCornerShape(4.dp)) // FIX 5: 1.5pt border
                        .background(Color.White)
                ) {
                    Column {
                        Box(modifier = Modifier.padding(12.dp)) {
                            Column {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = "Local Cryptographic Ledger",
                                            color = NavyPrimary,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Black
                                        )
                                        Icon(
                                            imageVector = Icons.Default.Info,
                                            contentDescription = "Info",
                                            tint = OrangeAccent,
                                            modifier = Modifier
                                                .size(14.dp)
                                                .clickable { tooltipTerm = "sqlite" }
                                                .padding(start = 4.dp)
                                        )
                                    }
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(2.dp))
                                            .background(NavyPrimary)
                                            .padding(horizontal = 5.dp, vertical = 2.dp)
                                    ) {
                                        Text(
                                            text = "SQLite Encrypted",
                                            color = Color.White,
                                            fontSize = 8.sp,
                                            fontFamily = FontFamily.Monospace,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(6.dp))

                                Text(
                                    text = "Strict municipal persistence. Incident records are immutably written to onboard flash storage with zero cloud-latency blocking.",
                                    color = SlateSecondary,
                                    fontSize = 11.sp,
                                    lineHeight = 16.sp
                                )
                            }

                            // FIX 5: Red "SEALED" rubber-stamp rotated over the ledger card
                            Box(
                                modifier = Modifier
                                    .align(Alignment.CenterEnd)
                                    .padding(end = 6.dp)
                                    .rotate(-6f)
                                    .border(2.dp, RedError.copy(alpha = 0.85f), RoundedCornerShape(3.dp))
                                    .background(Color(0x15BE3A2B))
                                    .padding(horizontal = 8.dp, vertical = 3.dp)
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = "★ SEALED ★",
                                        color = RedError,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Black,
                                        fontFamily = FontFamily.Monospace,
                                        letterSpacing = 1.sp
                                    )
                                    Text(
                                        text = "JMC CORRIDOR CERTIFIED",
                                        color = RedError,
                                        fontSize = 6.sp,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = FontFamily.Monospace
                                    )
                                }
                            }
                        }

                        // Bottom Strip: ChaCha20-Poly1305 · Hardware Keyring Sealed
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(NavyDark)
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.clickable { tooltipTerm = "chacha20" }
                                ) {
                                    Text(
                                        text = "ChaCha20-Poly1305 · Hardware Keyring",
                                        color = Color.White.copy(alpha = 0.9f),
                                        fontSize = 9.sp,
                                        fontFamily = FontFamily.Monospace,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Icon(
                                        imageVector = Icons.Default.Info,
                                        contentDescription = "Info",
                                        tint = OrangeAccent,
                                        modifier = Modifier.size(10.dp).padding(start = 2.dp)
                                    )
                                }
                                Text(
                                    text = "ON-DEVICE",
                                    color = OrangeAccent,
                                    fontSize = 9.sp,
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.Black
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Counter Panel: "X Incidents Queued · Y KB Synced"
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(4.dp))
                        .border(1.dp, OrangeAccent, RoundedCornerShape(4.dp))
                        .background(OrangeTint)
                        .padding(horizontal = 12.dp, vertical = 10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "QUEUED / SYNC STATUS",
                                color = SlateSecondary,
                                fontSize = 9.sp,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.5.sp
                            )
                            Text(
                                text = "$queuedCount Incidents Queued · $syncedKb KB Synced",
                                color = OrangeAccent,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Black,
                                fontFamily = FontFamily.Monospace
                            )
                        }

                        Button(
                            onClick = onNewReport,
                            colors = ButtonDefaults.buttonColors(containerColor = NavyPrimary),
                            shape = RoundedCornerShape(3.dp),
                            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                            modifier = Modifier.testTag("add_new_report_btn")
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(imageVector = Icons.Default.Add, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(3.dp))
                                Text(text = "NEW REPORT", color = Color.White, fontSize = 9.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "FIELD INCIDENTS (${reports.size})",
                        color = NavyPrimary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.Monospace,
                        letterSpacing = 0.5.sp
                    )
                    Text(
                        text = "* Swipe left to purge local record",
                        color = SlateSecondary,
                        fontSize = 9.sp,
                        fontStyle = FontStyle.Italic
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))
            }

            // List of Reports with Color Hierarchy & Citizen Avatars
            items(reports, key = { it.id }) { report ->
                HierarchicalReportCard(report = report)
                Spacer(modifier = Modifier.height(8.dp))
            }

            item {
                Spacer(modifier = Modifier.height(10.dp))

                // Big Orange Button at Bottom: "FORCE SYNC WHEN SIGNAL RETURNS"
                Button(
                    onClick = onForceSync,
                    colors = ButtonDefaults.buttonColors(containerColor = OrangeAccent),
                    shape = RoundedCornerShape(4.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("force_sync_button")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Sync,
                            contentDescription = "Sync",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = "FORCE SYNC WHEN SIGNAL RETURNS (7/8)",
                            color = Color.White,
                            fontWeight = FontWeight.Black,
                            fontSize = 12.sp,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))
            }
        }

        // Info Tooltip Dialog
        if (tooltipTerm != null) {
            AlertDialog(
                onDismissRequest = { tooltipTerm = null },
                title = {
                    Text(
                        text = if (tooltipTerm == "sqlite") "SQLite Local Persistence" else "ChaCha20-Poly1305 Security",
                        color = NavyPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Black
                    )
                },
                text = {
                    Text(
                        text = if (tooltipTerm == "sqlite")
                            "Every civic complaint is written directly to the mobile device's on-disk SQLite database before any remote network call is made. Data survives phone restarts and zero-signal zones."
                        else
                            "Lightweight high-speed encryption engine optimized for mobile processors without hardware AES accelerators. Secures citizen photos and GPS coords.",
                        color = SlateSecondary,
                        fontSize = 12.sp
                    )
                },
                confirmButton = {
                    TextButton(onClick = { tooltipTerm = null }) {
                        Text("CLOSE", color = OrangeAccent, fontWeight = FontWeight.Bold)
                    }
                },
                containerColor = Color.White,
                shape = RoundedCornerShape(4.dp)
            )
        }

        HyperEdgeFooter()
    }
}

@Composable
fun HierarchicalReportCard(report: GrievanceReport) {
    // Determine Citizen Initials based on report ID
    val citizenInitials = when (report.id.takeLast(2).toIntOrNull() ?: 0 % 5) {
        0 -> "RS"
        1 -> "AK"
        2 -> "MP"
        3 -> "VS"
        4 -> "TD"
        else -> "PK"
    }

    // FIX 7: Color Hierarchy
    // Critical: red left-border + light red bg (#BE3A2B at 8%)
    // Pending: amber left-border + light amber bg (#E8760C at 8%)
    // Resolved: green left-border + light green bg (#1F9D55 at 8%)
    val isCritical = report.category.contains("Water") || report.category.contains("Pothole")
    val (leftBorderColor, cardBgColor, badgeBorder, badgeTextColor, badgeBg) = when (report.status) {
        "SYNCED" -> Tuple5(GreenSuccess, Color(0x141F9D55), GreenSuccess, GreenSuccess, GreenTint)
        "SYNCING" -> Tuple5(OrangeAccent, Color(0x14E8760C), OrangeAccent, OrangeAccent, OrangeTint)
        else -> {
            if (isCritical) {
                Tuple5(RedError, Color(0x14BE3A2B), RedError, RedError, RedTint)
            } else {
                Tuple5(OrangeAccent, Color(0x14E8760C), CardBorderNavy, NavyPrimary, Color(0xFFEBEFF4))
            }
        }
    }

    val categoryIcon = when (report.imagePreset) {
        "sanitation" -> Icons.Default.DeleteSweep
        "water" -> Icons.Default.WaterDrop
        "pothole" -> Icons.Default.Warning
        "light" -> Icons.Default.Lightbulb
        else -> Icons.Default.WaterDamage
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, CardBorderNavy, RoundedCornerShape(4.dp))
            .clip(RoundedCornerShape(4.dp))
            .background(cardBgColor)
    ) {
        // Left thick accent border (4dp) for visual hierarchy
        Box(
            modifier = Modifier
                .width(4.dp)
                .matchParentSize()
                .background(leftBorderColor)
        )

        Column(modifier = Modifier.padding(start = 14.dp, top = 10.dp, end = 10.dp, bottom = 10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
                        // Category Icon Badge
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(RoundedCornerShape(3.dp))
                                .background(NavyPrimary),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = categoryIcon,
                                contentDescription = null,
                                tint = OrangeAccent,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                // Citizen Avatar Pill
                                Box(
                                    modifier = Modifier
                                        .size(18.dp)
                                        .clip(CircleShape)
                                        .background(NavyPrimary.copy(alpha = 0.85f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = citizenInitials,
                                        color = Color.White,
                                        fontSize = 8.sp,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = FontFamily.Monospace
                                    )
                                }
                                Spacer(modifier = Modifier.width(5.dp))
                                Text(
                                    text = report.id,
                                    color = NavyPrimary,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Black,
                                    fontFamily = FontFamily.Monospace
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "· ${report.formattedTime}",
                                    color = SlateSecondary,
                                    fontSize = 10.sp,
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                            Text(
                                text = report.category,
                                color = NavyPrimary,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    // Status Badge
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(3.dp))
                            .background(badgeBg)
                            .border(1.dp, badgeBorder, RoundedCornerShape(3.dp))
                            .padding(horizontal = 6.dp, vertical = 3.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            if (report.status == "SYNCED") {
                                Icon(imageVector = Icons.Default.Check, contentDescription = null, tint = GreenSuccess, modifier = Modifier.size(10.dp))
                                Spacer(modifier = Modifier.width(3.dp))
                            }
                            Text(
                                text = report.status,
                                color = badgeTextColor,
                                fontSize = 8.sp,
                                fontWeight = FontWeight.Black,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = report.description,
                    color = SlateSecondary,
                    fontSize = 11.sp,
                    lineHeight = 15.sp,
                    maxLines = 2
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = report.location,
                        color = SlateSecondary,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "${report.payloadKb} KB · ChaCha20",
                        color = NavyPrimary,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
        }
    }

data class Tuple5<A, B, C, D, E>(val a: A, val b: B, val c: C, val d: D, val e: E)
