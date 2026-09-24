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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
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
import com.example.ui.theme.OrangeTint
import com.example.ui.theme.RedError
import com.example.ui.theme.RedTint
import com.example.ui.theme.SlateLight
import com.example.ui.theme.SlateSecondary

data class IncidentRow(
    val id: String,
    val category: String,
    val location: String,
    val status: String,
    val time: String,
    val pinColor: Color
)

@Composable
fun MunicipalConsoleScreen(
    reports: List<GrievanceReport>,
    isOnline: Boolean,
    onToggleOnline: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableStateOf("Dashboard") }
    val consoleTabs = listOf("Dashboard", "Reports", "Map", "SLA", "Settings")

    // Combined live reports + historical Jammu Smart City field records
    val incidentRows = remember(reports) {
        val list = mutableListOf<IncidentRow>()
        // Live items first
        reports.take(3).forEach { r ->
            val color = when (r.status) {
                "SYNCED" -> GreenSuccess
                "SYNCING" -> OrangeAccent
                else -> RedError
            }
            list.add(
                IncidentRow(
                    id = r.id,
                    category = r.category.take(18) + if (r.category.length > 18) "…" else "",
                    location = "Ward 12 Trikuta Ngr",
                    status = r.status,
                    time = r.formattedTime,
                    pinColor = color
                )
            )
        }
        // Additional historical baseline
        list.addAll(
            listOf(
                IncidentRow("HYE-0038", "Water Leakage", "Sector 4 Main Link", "RESOLVED", "09:40 IST", GreenSuccess),
                IncidentRow("HYE-0037", "Pothole / Road", "Canal Rd Culvert", "DISPATCHED", "08:15 IST", OrangeAccent),
                IncidentRow("HYE-0036", "Drainage Clog", "Trikuta Lane 2", "RESOLVED", "Yesterday", GreenSuccess)
            )
        )
        list
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(CreamBackground)
    ) {
        HyperEdgeHeader(
            screenNumber = 8,
            isOnline = isOnline,
            onToggleOnline = onToggleOnline
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
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Municipal Command Console",
                        color = NavyPrimary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black
                    )
                    Text(
                        text = "JMC Ward 12 (Trikuta Nagar) · Corridor Server",
                        color = SlateSecondary,
                        fontSize = 10.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(3.dp))
                        .background(GreenTint)
                        .border(1.dp, GreenSuccess, RoundedCornerShape(3.dp))
                        .padding(horizontal = 6.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = "MESH LINK ACTIVE · SYNCED",
                        color = GreenSuccess,
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // 4 Metric Cards (2x2 Grid)
            // Card 1: TOTAL REPORTS: 48 (+12 offline) [JMC Buffer]
            // Card 2: PENDING SYNC / TRIAGE: 09 (Critical 4) [Action Req]
            // Card 3: RESOLVED FIELD WORK: 39 (Seal Rate 81.2%)
            // Card 4: AVG FIELD DISPATCH: 18 min (Algorithmic) [AI Auto]
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Metric 1
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .border(1.dp, CardBorderNavy, RoundedCornerShape(4.dp))
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color.White)
                            .padding(10.dp)
                    ) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "TOTAL REPORTS",
                                    color = SlateSecondary,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace
                                )
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(2.dp))
                                        .background(Color(0xFFEFF2F6))
                                        .padding(horizontal = 4.dp, vertical = 1.dp)
                                ) {
                                    Text(text = "JMC Buffer", color = NavyPrimary, fontSize = 8.sp, fontFamily = FontFamily.Monospace)
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "48",
                                color = NavyPrimary,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Black
                            )
                            Text(
                                text = "+12 offline buffer sealed",
                                color = GreenSuccess,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }

                    // Metric 2
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .border(1.dp, CardBorderNavy, RoundedCornerShape(4.dp))
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color.White)
                            .padding(10.dp)
                    ) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "PENDING TRIAGE",
                                    color = SlateSecondary,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace
                                )
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(2.dp))
                                        .background(OrangeTint)
                                        .padding(horizontal = 4.dp, vertical = 1.dp)
                                ) {
                                    Text(text = "Action Req", color = OrangeAccent, fontSize = 8.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "09",
                                color = OrangeAccent,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Black
                            )
                            Text(
                                text = "Critical 4 · Pothole/Water",
                                color = RedError,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Metric 3
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .border(1.dp, CardBorderNavy, RoundedCornerShape(4.dp))
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color.White)
                            .padding(10.dp)
                    ) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "RESOLVED FIELD",
                                    color = SlateSecondary,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace
                                )
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(2.dp))
                                        .background(GreenTint)
                                        .padding(horizontal = 4.dp, vertical = 1.dp)
                                ) {
                                    Text(text = "81.2%", color = GreenSuccess, fontSize = 8.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "39",
                                color = GreenSuccess,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Black
                            )
                            Text(
                                text = "Verified with geostamp",
                                color = SlateSecondary,
                                fontSize = 9.sp,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }

                    // Metric 4
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .border(1.dp, CardBorderNavy, RoundedCornerShape(4.dp))
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color.White)
                            .padding(10.dp)
                    ) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "AVG DISPATCH",
                                    color = SlateSecondary,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace
                                )
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(2.dp))
                                        .background(NavyDark)
                                        .padding(horizontal = 4.dp, vertical = 1.dp)
                                ) {
                                    Text(text = "AI Auto", color = Color.White, fontSize = 8.sp, fontFamily = FontFamily.Monospace)
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "18 min",
                                color = NavyPrimary,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Black
                            )
                            Text(
                                text = "Algorithmic routing",
                                color = SlateSecondary,
                                fontSize = 9.sp,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Section: Corridor GIS · Ward 12 — Sector 4 / Trikuta Canal
            Text(
                text = "CORRIDOR GIS · WARD 12 — SECTOR 4 / TRIKUTA CANAL",
                color = NavyPrimary,
                fontSize = 11.sp,
                fontWeight = FontWeight.Black,
                fontFamily = FontFamily.Monospace,
                modifier = Modifier.padding(bottom = 6.dp)
            )

            // Tactical SVG / Canvas Map with colored pins
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .border(1.dp, CardBorderNavy, RoundedCornerShape(4.dp))
                    .background(Color(0xFFEFECE2))
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val w = size.width
                    val h = size.height

                    // Grid lines
                    val dash = PathEffect.dashPathEffect(floatArrayOf(4f, 4f), 0f)
                    drawLine(Color(0xFFD6D0C2), Offset(0f, h * 0.33f), Offset(w, h * 0.33f), 1f, pathEffect = dash)
                    drawLine(Color(0xFFD6D0C2), Offset(0f, h * 0.66f), Offset(w, h * 0.66f), 1f, pathEffect = dash)
                    drawLine(Color(0xFFD6D0C2), Offset(w * 0.33f, 0f), Offset(w * 0.33f, h), 1f, pathEffect = dash)
                    drawLine(Color(0xFFD6D0C2), Offset(w * 0.66f, 0f), Offset(w * 0.66f, h), 1f, pathEffect = dash)

                    // Canal Road Vector (Curved blue line)
                    val path = androidx.compose.ui.graphics.Path()
                    path.moveTo(0f, h * 0.8f)
                    path.quadraticTo(w * 0.4f, h * 0.6f, w * 0.7f, h * 0.2f)
                    path.lineTo(w, h * 0.1f)
                    drawPath(
                        path = path,
                        color = Color(0xFF6DA4C9),
                        style = androidx.compose.ui.graphics.drawscope.Stroke(width = 4.dp.toPx())
                    )

                    // Sector block polygons
                    drawRect(
                        color = Color(0x330F2B46),
                        topLeft = Offset(w * 0.12f, h * 0.18f),
                        size = androidx.compose.ui.geometry.Size(w * 0.22f, h * 0.32f)
                    )
                    drawRect(
                        color = Color(0x330F2B46),
                        topLeft = Offset(w * 0.42f, h * 0.22f),
                        size = androidx.compose.ui.geometry.Size(w * 0.24f, h * 0.38f)
                    )

                    // Incident Pins (Red=New, Orange=In-Progress, Green=Resolved)
                    fun drawPin(center: Offset, color: Color, label: String) {
                        drawCircle(color = color, radius = 7.dp.toPx(), center = center)
                        drawCircle(color = Color.White, radius = 3.dp.toPx(), center = center)
                    }

                    drawPin(Offset(w * 0.24f, h * 0.42f), RedError, "HYE-0042")
                    drawPin(Offset(w * 0.54f, h * 0.32f), OrangeAccent, "HYE-0041")
                    drawPin(Offset(w * 0.72f, h * 0.58f), GreenSuccess, "HYE-0040")
                    drawPin(Offset(w * 0.82f, h * 0.26f), GreenSuccess, "HYE-0039")
                    drawPin(Offset(w * 0.38f, h * 0.74f), RedError, "HYE-0043")
                }

                // Map Legend Overlays
                Box(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(8.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(NavyDark.copy(alpha = 0.9f))
                        .padding(horizontal = 6.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = "CANAL CORRIDOR SECTOR 4",
                        color = Color.White,
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                }

                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(8.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(Color.White.copy(alpha = 0.9f))
                        .border(1.dp, CardBorderNavy, RoundedCornerShape(2.dp))
                        .padding(horizontal = 6.dp, vertical = 3.dp)
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(RedError))
                            Spacer(modifier = Modifier.width(2.dp))
                            Text("New", fontSize = 8.sp, color = NavyPrimary, fontWeight = FontWeight.Bold)
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(OrangeAccent))
                            Spacer(modifier = Modifier.width(2.dp))
                            Text("Active", fontSize = 8.sp, color = NavyPrimary, fontWeight = FontWeight.Bold)
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(GreenSuccess))
                            Spacer(modifier = Modifier.width(2.dp))
                            Text("Solved", fontSize = 8.sp, color = NavyPrimary, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Incident Data Table
            Text(
                text = "CORRIDOR DISPATCH TABLE (REAL-TIME BUFFER):",
                color = NavyPrimary,
                fontSize = 10.sp,
                fontWeight = FontWeight.Black,
                fontFamily = FontFamily.Monospace,
                modifier = Modifier.padding(bottom = 4.dp)
            )

            // Table Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                    .background(NavyPrimary)
                    .padding(horizontal = 8.dp, vertical = 6.dp)
            ) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    Text(text = "ID", color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace, modifier = Modifier.weight(0.9f))
                    Text(text = "CATEGORY", color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace, modifier = Modifier.weight(1.4f))
                    Text(text = "LOCATION", color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace, modifier = Modifier.weight(1.3f))
                    Text(text = "STATUS", color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace, modifier = Modifier.weight(1f))
                }
            }

            // Table Rows
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, CardBorderNavy, RoundedCornerShape(bottomStart = 4.dp, bottomEnd = 4.dp))
                    .background(Color.White)
            ) {
                incidentRows.forEachIndexed { index, row ->
                    val isEven = index % 2 == 0
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(if (isEven) Color.White else Color(0xFFFBF8F2))
                            .padding(horizontal = 8.dp, vertical = 7.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = row.id, color = NavyPrimary, fontSize = 9.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace, modifier = Modifier.weight(0.9f))
                        Text(text = row.category, color = NavyPrimary, fontSize = 9.sp, fontWeight = FontWeight.Medium, modifier = Modifier.weight(1.4f))
                        Text(text = row.location, color = SlateSecondary, fontSize = 8.sp, modifier = Modifier.weight(1.3f))
                        Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(row.pinColor))
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = row.status,
                                color = row.pinColor,
                                fontSize = 8.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }
                    if (index < incidentRows.size - 1) {
                        Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color(0xFFEFECE2)))
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Sub-nav tabs: Dashboard, Reports, Map, SLA, Settings
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color(0xFFEDE8DC))
                    .padding(3.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                consoleTabs.forEach { tab ->
                    val isSelected = selectedTab == tab
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(3.dp))
                            .background(if (isSelected) NavyPrimary else Color.Transparent)
                            .clickable { selectedTab = tab }
                            .padding(horizontal = 8.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = tab,
                            color = if (isSelected) Color.White else NavyPrimary,
                            fontSize = 10.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Institutional footer card
            RuggedCard(backgroundColor = Color.White) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "🏛", fontSize = 16.sp)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Jammu Smart City Civic Edge Node",
                            color = NavyPrimary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Developed by Team HyperEdge · Central University of Jammu · Open Innovation Challenge 2026. Zero cloud blocking for last-mile citizens.",
                        color = SlateSecondary,
                        fontSize = 10.sp,
                        lineHeight = 14.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))
        }

        HyperEdgeFooter()
    }
}
