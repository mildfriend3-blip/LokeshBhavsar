package com.example.ui.screens

import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Color system strictly defined: #0F2B46, #E8760C, #1F9D55, #BE3A2B, #5B6B7A, #FAF6EE, #F1EBDD
val CreamBg = Color(0xFFFAF6EE)
val MapCreamBg = Color(0xFFF1EBDD)
val NavyBrand = Color(0xFF0F2B46)
val OrangeSingle = Color(0xFFE8760C)
val RedAlert = Color(0xFFBE3A2B)
val GreenSynced = Color(0xFF1F9D55)
val SlateInactive = Color(0xFF5B6B7A)

@Composable
fun DashboardScreen(
    isOnline: Boolean = false,
    onToggleOnline: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(CreamBg)
    ) {
        // HEADER: Poora cream #FAF6EE background. Koi white patti nahi.
        // Left: "HyperEdge" (navy, bold).
        // Right: "OFFLINE" (red outline, 1px border, 4px radius, transparent bg)
        // and "2/8 Sync" (orange #E8760C bg, white text, 4px radius).
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(CreamBg)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "HyperEdge",
                fontSize = 22.sp,
                fontWeight = FontWeight.ExtraBold,
                color = NavyBrand,
                letterSpacing = (-0.5).sp
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // OFFLINE (red outline badge)
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .border(
                            width = 1.dp,
                            color = if (isOnline) GreenSynced else RedAlert,
                            shape = RoundedCornerShape(4.dp)
                        )
                        .clickable { onToggleOnline() }
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                        .testTag("signal_status_badge")
                ) {
                    Text(
                        text = if (isOnline) "ONLINE" else "OFFLINE",
                        color = if (isOnline) GreenSynced else RedAlert,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                }

                // 2/8 Sync (orange badge)
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(OrangeSingle)
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                        .testTag("sync_count_badge")
                ) {
                    Text(
                        text = "2/8 Sync",
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
        }

        // 1px navy border-bottom under header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(NavyBrand)
        )

        // SCROLLABLE CONTENT AREA
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(CreamBg)
                .verticalScroll(rememberScrollState())
                .padding(bottom = 90.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // MAP: Inline vector map with cream (#F1EBDD) background
            // Tawi river wavy line, 5-6 irregular roads, labels "Trikuta Nagar", "Tawi", "Bahu Plaza road"
            // 4 pins (2 orange #E8760C pending, 2 green #1F9D55 synced)
            // Height: ~45% of visible upper screen (260dp), 1px navy #0F2B46 border, 4px radius, 16px horizontal margin
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(260.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .border(1.dp, NavyBrand, RoundedCornerShape(4.dp))
                    .background(MapCreamBg)
                    .testTag("trikuta_map_view")
            ) {
                // Vector Canvas drawing Tawi River & 5-6 irregular roads
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val w = size.width
                    val h = size.height

                    // 1. Tawi river wavy blue-grey line
                    val river = Path().apply {
                        moveTo(0f, h * 0.20f)
                        cubicTo(
                            w * 0.28f, h * 0.10f,
                            w * 0.52f, h * 0.35f,
                            w, h * 0.16f
                        )
                    }
                    drawPath(
                        path = river,
                        color = Color(0xFF6B92A8),
                        style = Stroke(width = 6f)
                    )

                    // 2. Road 1: Bahu Plaza road (diagonal arterial road in south)
                    val bahuPlazaRoad = Path().apply {
                        moveTo(w * 0.05f, h * 0.85f)
                        lineTo(w * 0.44f, h * 0.64f)
                        lineTo(w * 0.95f, h * 0.74f)
                    }
                    drawPath(
                        path = bahuPlazaRoad,
                        color = Color(0xFFC7BBA4),
                        style = Stroke(width = 5f)
                    )

                    // 3. Road 2: Sector 3 Main Spine
                    val road2 = Path().apply {
                        moveTo(w * 0.44f, h * 0.64f)
                        lineTo(w * 0.38f, h * 0.30f)
                        lineTo(w * 0.60f, h * 0.23f)
                    }
                    drawPath(
                        path = road2,
                        color = Color(0xFFD4C8B2),
                        style = Stroke(width = 4f)
                    )

                    // 4. Road 3: Canal Link Road
                    val road3 = Path().apply {
                        moveTo(w * 0.12f, h * 0.40f)
                        lineTo(w * 0.38f, h * 0.30f)
                    }
                    drawPath(
                        path = road3,
                        color = Color(0xFFD4C8B2),
                        style = Stroke(width = 3.5f)
                    )

                    // 5. Road 4: East Ward Corridor
                    val road4 = Path().apply {
                        moveTo(w * 0.60f, h * 0.23f)
                        lineTo(w * 0.86f, h * 0.45f)
                        lineTo(w * 0.76f, h * 0.72f)
                    }
                    drawPath(
                        path = road4,
                        color = Color(0xFFD4C8B2),
                        style = Stroke(width = 3.5f)
                    )

                    // 6. Road 5: Market Enclave Loop
                    val road5 = Path().apply {
                        moveTo(w * 0.18f, h * 0.76f)
                        lineTo(w * 0.26f, h * 0.54f)
                        lineTo(w * 0.44f, h * 0.64f)
                    }
                    drawPath(
                        path = road5,
                        color = Color(0xFFC7BBA4),
                        style = Stroke(width = 3.5f)
                    )
                }

                // Map Text Labels: "Tawi", "Trikuta Nagar", "Bahu Plaza road"
                Text(
                    text = "Tawi",
                    color = Color(0xFF4A748C),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(start = 16.dp, top = 18.dp)
                )

                Text(
                    text = "Trikuta Nagar",
                    color = NavyBrand,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = (-0.2).sp,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(bottom = 24.dp, end = 24.dp)
                )

                Text(
                    text = "Bahu Plaza road",
                    color = Color(0xFF706351),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(end = 18.dp, bottom = 26.dp)
                )

                // 4 PINS: 2 Orange (#E8760C) pending, 2 Green (#1F9D55) synced
                // Pin 1: Bahu Plaza junction (Orange Pending)
                InlineMapPin(
                    color = OrangeSingle,
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(start = 126.dp, bottom = 72.dp)
                )

                // Pin 2: Canal curve (Orange Pending)
                InlineMapPin(
                    color = OrangeSingle,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(start = 70.dp, top = 88.dp)
                )

                // Pin 3: Trikuta Central (Green Synced)
                InlineMapPin(
                    color = GreenSynced,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(start = 50.dp, top = 8.dp)
                )

                // Pin 4: East Corridor (Green Synced)
                InlineMapPin(
                    color = GreenSynced,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(end = 52.dp, top = 72.dp)
                )

                // Tiny Pin Legend
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(8.dp)
                        .background(CreamBg)
                        .border(1.dp, NavyBrand.copy(alpha = 0.35f), RoundedCornerShape(2.dp))
                        .padding(horizontal = 6.dp, vertical = 3.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                            Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(OrangeSingle))
                            Text("Pending", fontSize = 9.sp, color = NavyBrand, fontWeight = FontWeight.Medium)
                        }
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                            Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(GreenSynced))
                            Text("Synced", fontSize = 9.sp, color = NavyBrand, fontWeight = FontWeight.Medium)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // MAP KE NEECHE:
            // "Ward 12 · Trikuta Nagar" heading
            // "4 Incidents Queued · 0 KB Synced" card
            // 2 report cards (initials avatar RK/SD, "Gadda near Bahu Plaza road", timestamp "24 Sep, 14:32:07", SEALED/PENDING stamp)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Ward 12 · Trikuta Nagar",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = NavyBrand,
                    letterSpacing = (-0.3).sp
                )

                // Summary Card: "4 Incidents Queued · 0 KB Synced" (1px navy border, 4px radius)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(elevation = 1.dp, shape = RoundedCornerShape(4.dp))
                        .clip(RoundedCornerShape(4.dp))
                        .border(1.dp, NavyBrand, RoundedCornerShape(4.dp))
                        .background(CreamBg)
                        .padding(horizontal = 14.dp, vertical = 12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "4",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace,
                                color = OrangeSingle
                            )
                            Text(
                                text = " Incidents Queued",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = NavyBrand
                            )
                        }

                        Text(
                            text = "·",
                            color = SlateInactive,
                            fontWeight = FontWeight.Bold
                        )

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "0 KB",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace,
                                color = NavyBrand
                            )
                            Text(
                                text = " Synced",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = NavyBrand
                            )
                        }
                    }
                }

                // Report Card 1: RK, "Gadda near Bahu Plaza road", timestamp "24 Sep, 14:32:07", SEALED stamp
                ReportCardItem(
                    initials = "RK",
                    avatarBg = NavyBrand,
                    avatarFg = CreamBg,
                    title = "Gadda near Bahu Plaza road",
                    timestamp = "24 Sep, 14:32:07",
                    stampText = "SEALED",
                    stampColor = GreenSynced
                )

                // Report Card 2: SD, "Kachra overflow Sec 4 market", timestamp "24 Sep, 14:18:42", PENDING stamp
                ReportCardItem(
                    initials = "SD",
                    avatarBg = OrangeSingle,
                    avatarFg = CreamBg,
                    title = "Kachra overflow Sec 4 market",
                    timestamp = "24 Sep, 14:18:42",
                    stampText = "PENDING",
                    stampColor = OrangeSingle
                )
            }
        }
    }
}

@Composable
fun InlineMapPin(
    color: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.size(16.dp),
        contentAlignment = Alignment.Center
    ) {
        // Outer halo
        Box(
            modifier = Modifier
                .size(16.dp)
                .clip(CircleShape)
                .background(color.copy(alpha = 0.25f))
        )
        // Solid pin center
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(color)
                .border(1.dp, CreamBg, CircleShape)
        )
    }
}

@Composable
fun ReportCardItem(
    initials: String,
    avatarBg: Color,
    avatarFg: Color,
    title: String,
    timestamp: String,
    stampText: String,
    stampColor: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .shadow(elevation = 1.dp, shape = RoundedCornerShape(4.dp))
            .clip(RoundedCornerShape(4.dp))
            .border(1.dp, NavyBrand, RoundedCornerShape(4.dp))
            .background(CreamBg)
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.weight(1f)
            ) {
                // Initials circle
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(avatarBg),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = initials,
                        color = avatarFg,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text(
                        text = title,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = NavyBrand,
                        maxLines = 1
                    )
                    Text(
                        text = timestamp,
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace,
                        color = SlateInactive
                    )
                }
            }

            // Stamp (SEALED in green or PENDING in orange)
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .border(1.dp, stampColor, RoundedCornerShape(4.dp))
                    .padding(horizontal = 8.dp, vertical = 3.dp)
            ) {
                Text(
                    text = stampText,
                    color = stampColor,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )
            }
        }
    }
}
