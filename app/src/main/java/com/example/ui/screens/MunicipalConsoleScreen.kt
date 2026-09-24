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
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.WaterDamage
import androidx.compose.material.icons.filled.WaterDrop
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
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
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
import com.example.ui.theme.SlateLight
import com.example.ui.theme.SlateSecondary

data class JammuIncidentRecord(
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
    isHindi: Boolean = false,
    onToggleOnline: () -> Unit,
    onToggleLanguage: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableStateOf("Dashboard") }
    val consoleTabs = listOf("Dashboard", "Reports", "Map", "SLA", "Settings")

    // Authentic Jammu Municipal Corporation records across real Jammu wards
    val incidentRows = remember(reports) {
        val list = mutableListOf<JammuIncidentRecord>()
        reports.take(3).forEach { r ->
            val color = when (r.status) {
                "SYNCED" -> GreenSuccess
                "SYNCING" -> OrangeAccent
                else -> RedError
            }
            list.add(
                JammuIncidentRecord(
                    id = r.id,
                    category = r.category.take(17) + if (r.category.length > 17) "…" else "",
                    location = r.location.take(16) + "…",
                    status = r.status,
                    time = r.formattedTime,
                    pinColor = color
                )
            )
        }
        list.addAll(
            listOf(
                JammuIncidentRecord("HYE-0038", "Water Leakage", "Channi Himmat Sec 1", "RESOLVED", "08:24:10 IST", GreenSuccess),
                JammuIncidentRecord("HYE-0037", "Sanitation Dump", "Talab Tillo Mandi", "DISPATCHED", "06:10:04 IST", OrangeAccent),
                JammuIncidentRecord("HYE-0036", "Streetlight Out", "Bakshi Ngr Enclave", "RESOLVED", "Yesterday", GreenSuccess)
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
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = if (isHindi) "नगर निगम कमांड कंसोल" else "Municipal Command Console",
                        color = NavyPrimary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black
                    )
                    Text(
                        text = "JMC WARD 12 (TRIKUTA NAGAR) · JAMMU SMART CITY",
                        color = SlateSecondary,
                        fontSize = 9.sp,
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

            // CHANGE 4: Handwriting annotation pointing to "48 Total Reports"
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(start = 4.dp, bottom = 4.dp)
            ) {
                Text(
                    text = "↳ started here (baseline field triage)",
                    color = OrangeAccent,
                    fontSize = 11.sp,
                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = FontFamily.Cursive
                )
            }

            // FIX 4: 4 Metric Cards (2x2 Grid) with ACTUALLY BIG 48sp Monospace Numbers
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Metric 1: 48
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
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace,
                                    letterSpacing = 0.5.sp
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
                            Text(
                                text = "48",
                                color = NavyPrimary,
                                fontSize = 48.sp, // FIX 4: Big 48sp+
                                fontWeight = FontWeight.Black,
                                fontFamily = FontFamily.Monospace,
                                lineHeight = 50.sp
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

                    // Metric 2: 09
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
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace,
                                    letterSpacing = 0.5.sp
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
                            Text(
                                text = "09",
                                color = OrangeAccent,
                                fontSize = 48.sp, // FIX 4: Big 48sp+
                                fontWeight = FontWeight.Black,
                                fontFamily = FontFamily.Monospace,
                                lineHeight = 50.sp
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
                    // Metric 3: 39
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
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace,
                                    letterSpacing = 0.5.sp
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
                            Text(
                                text = "39",
                                color = GreenSuccess,
                                fontSize = 48.sp, // FIX 4: Big 48sp+
                                fontWeight = FontWeight.Black,
                                fontFamily = FontFamily.Monospace,
                                lineHeight = 50.sp
                            )
                            Text(
                                text = "Verified with geostamp",
                                color = SlateSecondary,
                                fontSize = 9.sp,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }

                    // Metric 4: 18 min
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
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace,
                                    letterSpacing = 0.5.sp
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
                            Row(verticalAlignment = Alignment.Bottom) {
                                Text(
                                    text = "18",
                                    color = NavyPrimary,
                                    fontSize = 48.sp, // FIX 4: Big 48sp+
                                    fontWeight = FontWeight.Black,
                                    fontFamily = FontFamily.Monospace,
                                    lineHeight = 50.sp
                                )
                                Text(
                                    text = "min",
                                    color = NavyPrimary,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace,
                                    modifier = Modifier.padding(bottom = 6.dp, start = 2.dp)
                                )
                            }
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

            // FIX 3: REALISTIC GIS MAP OF JAMMU WARD 12 & TAWI RIVER
            Text(
                text = "CORRIDOR GIS · TAWI BASIN & WARD 12",
                color = NavyPrimary,
                fontSize = 11.sp,
                fontWeight = FontWeight.Black,
                fontFamily = FontFamily.Monospace,
                letterSpacing = 0.5.sp,
                modifier = Modifier.padding(bottom = 6.dp)
            )

            // Realistic Map Canvas with Winding Roads, Tawi River, Building Clusters, and Icon Pins
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(210.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .border(1.dp, CardBorderNavy, RoundedCornerShape(4.dp))
                    .background(Color(0xFFEDE9DC))
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val w = size.width
                    val h = size.height

                    // 1. Irregular Building Cluster Polygons (Trikuta Sector 4 residential parcels)
                    val blockColor = Color(0xFFDED8C7)
                    val blockStroke = Color(0xFFC8BFAB)

                    fun drawBlock(points: List<Offset>) {
                        val p = Path()
                        p.moveTo(points[0].x, points[0].y)
                        for (i in 1 until points.size) p.lineTo(points[i].x, points[i].y)
                        p.close()
                        drawPath(p, blockColor)
                        drawPath(p, blockStroke, style = Stroke(1.dp.toPx()))
                    }

                    // Sector Block A (Winding parcel)
                    drawBlock(listOf(
                        Offset(w * 0.08f, h * 0.15f),
                        Offset(w * 0.28f, h * 0.12f),
                        Offset(w * 0.32f, h * 0.35f),
                        Offset(w * 0.14f, h * 0.40f),
                        Offset(w * 0.06f, h * 0.26f)
                    ))

                    // Sector Block B (Trikuta Market complex)
                    drawBlock(listOf(
                        Offset(w * 0.38f, h * 0.28f),
                        Offset(w * 0.62f, h * 0.22f),
                        Offset(w * 0.66f, h * 0.45f),
                        Offset(w * 0.42f, h * 0.48f)
                    ))

                    // Sector Block C (Canal residential enclave)
                    drawBlock(listOf(
                        Offset(w * 0.15f, h * 0.55f),
                        Offset(w * 0.40f, h * 0.58f),
                        Offset(w * 0.36f, h * 0.85f),
                        Offset(w * 0.10f, h * 0.80f)
                    ))

                    // Sector Block D (Gandhi Nagar Border sector)
                    drawBlock(listOf(
                        Offset(w * 0.52f, h * 0.65f),
                        Offset(w * 0.78f, h * 0.58f),
                        Offset(w * 0.84f, h * 0.88f),
                        Offset(w * 0.58f, h * 0.90f)
                    ))

                    // 2. TAWI RIVER (Winding natural blue waterway from Northeast to Southwest)
                    val riverPath = Path()
                    riverPath.moveTo(w * 0.68f, 0f)
                    riverPath.cubicTo(
                        w * 0.75f, h * 0.25f,
                        w * 0.85f, h * 0.45f,
                        w * 0.96f, h * 0.70f
                    )
                    riverPath.lineTo(w, h * 0.75f)
                    riverPath.lineTo(w, 0f)
                    riverPath.close()
                    drawPath(riverPath, Color(0xFF98C5E2))

                    // River main channel line
                    val riverCenter = Path()
                    riverCenter.moveTo(w * 0.72f, 0f)
                    riverCenter.cubicTo(
                        w * 0.78f, h * 0.25f,
                        w * 0.88f, h * 0.48f,
                        w * 0.98f, h * 0.75f
                    )
                    drawPath(riverCenter, Color(0xFF67A7CE), style = Stroke(width = 8.dp.toPx()))

                    // 3. Winding Street Lines (Curved roads, not straight geometric lines)
                    // Canal Road (winding across west to central)
                    val canalRoad = Path()
                    canalRoad.moveTo(0f, h * 0.48f)
                    canalRoad.cubicTo(
                        w * 0.20f, h * 0.45f,
                        w * 0.32f, h * 0.52f,
                        w * 0.50f, h * 0.50f
                    )
                    canalRoad.cubicTo(
                        w * 0.65f, h * 0.48f,
                        w * 0.72f, h * 0.35f,
                        w * 0.78f, h * 0.15f
                    )
                    drawPath(canalRoad, Color.White, style = Stroke(width = 6.dp.toPx()))
                    drawPath(canalRoad, Color(0xFF425567), style = Stroke(width = 1.dp.toPx()))

                    // Trikuta Nagar Main Road (Arched curve)
                    val trikutaMain = Path()
                    trikutaMain.moveTo(w * 0.34f, 0f)
                    trikutaMain.cubicTo(
                        w * 0.36f, h * 0.25f,
                        w * 0.45f, h * 0.60f,
                        w * 0.48f, h
                    )
                    drawPath(trikutaMain, Color.White, style = Stroke(width = 5.dp.toPx()))
                    drawPath(trikutaMain, Color(0xFF425567), style = Stroke(width = 1.dp.toPx()))

                    // Bahu Fort Link Road (Winding over river towards Fort)
                    val bahuRoad = Path()
                    bahuRoad.moveTo(w * 0.50f, h * 0.50f)
                    bahuRoad.cubicTo(
                        w * 0.68f, h * 0.54f,
                        w * 0.78f, h * 0.62f,
                        w, h * 0.60f
                    )
                    drawPath(bahuRoad, Color.White, style = Stroke(width = 4.dp.toPx()))
                    drawPath(bahuRoad, Color(0xFF425567), style = Stroke(width = 1.dp.toPx()))
                }

                // Street Label Annotations (CHANGE 6: Canal Rd, Trikuta Nagar Rd, Sector 4, Tawi)
                Text(
                    text = "Canal Rd",
                    color = NavyPrimary,
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    modifier = Modifier
                        .padding(top = 88.dp, start = 30.dp)
                        .background(Color.White.copy(alpha = 0.85f))
                        .padding(horizontal = 2.dp)
                )

                Text(
                    text = "Trikuta Nagar Rd",
                    color = NavyPrimary,
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    modifier = Modifier
                        .padding(top = 40.dp, start = 140.dp)
                        .background(Color.White.copy(alpha = 0.85f))
                        .padding(horizontal = 2.dp)
                )

                Text(
                    text = "Sector 4",
                    color = SlateSecondary,
                    fontSize = 7.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    modifier = Modifier
                        .padding(top = 118.dp, start = 60.dp)
                        .background(Color.White.copy(alpha = 0.7f))
                        .padding(horizontal = 2.dp)
                )

                Text(
                    text = "Tawi ──►",
                    color = Color(0xFF1E5275),
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Black,
                    fontFamily = FontFamily.Monospace,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(top = 18.dp, end = 20.dp)
                )

                Text(
                    text = "BAHU FORT LINK",
                    color = NavyPrimary,
                    fontSize = 7.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    modifier = Modifier
                        .padding(top = 135.dp, start = 220.dp)
                        .background(Color.White.copy(alpha = 0.85f))
                        .padding(horizontal = 2.dp)
                )

                // FIX 3: Pins with REAL ICONS INSIDE (Trash, Wrench, Water Drop, Lightbulb)
                // Pin 1: Red Pothole on Canal Road
                MapPinWithIcon(
                    icon = Icons.Default.Warning,
                    bgColor = RedError,
                    label = "HYE-0041",
                    modifier = Modifier.padding(top = 80.dp, start = 85.dp)
                )

                // Pin 2: Orange Garbage near Sector 4
                MapPinWithIcon(
                    icon = Icons.Default.DeleteSweep,
                    bgColor = OrangeAccent,
                    label = "HYE-0042",
                    modifier = Modifier.padding(top = 50.dp, start = 190.dp)
                )

                // Pin 3: Blue Water Leak near Canal link
                MapPinWithIcon(
                    icon = Icons.Default.WaterDrop,
                    bgColor = Color(0xFF1976D2),
                    label = "HYE-0038",
                    modifier = Modifier.padding(top = 125.dp, start = 135.dp)
                )

                // Pin 4: Green Resolved Streetlight near Trikuta South
                MapPinWithIcon(
                    icon = Icons.Default.Lightbulb,
                    bgColor = GreenSuccess,
                    label = "HYE-0039",
                    modifier = Modifier.padding(top = 150.dp, start = 240.dp)
                )

                // FIX 3: "YOU ARE HERE" marker with blue dot, pulse ring, and label
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(top = 95.dp, start = 145.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(NavyDark.copy(alpha = 0.95f))
                        .padding(horizontal = 5.dp, vertical = 2.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Box(
                            modifier = Modifier
                                .size(12.dp)
                                .border(1.5.dp, Color(0xFF4FC3F7), CircleShape)
                        )
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF29B6F6))
                        )
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "YOU ARE HERE (SEC 4)",
                        color = Color.White,
                        fontSize = 7.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.Monospace
                    )
                }

                // Legend Strip at bottom
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(6.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(Color.White.copy(alpha = 0.92f))
                        .border(1.dp, CardBorderNavy, RoundedCornerShape(2.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.size(5.dp).clip(CircleShape).background(RedError))
                            Spacer(modifier = Modifier.width(2.dp))
                            Text("New", fontSize = 8.sp, color = NavyPrimary, fontWeight = FontWeight.Bold)
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.size(5.dp).clip(CircleShape).background(OrangeAccent))
                            Spacer(modifier = Modifier.width(2.dp))
                            Text("Active", fontSize = 8.sp, color = NavyPrimary, fontWeight = FontWeight.Bold)
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.size(5.dp).clip(CircleShape).background(GreenSuccess))
                            Spacer(modifier = Modifier.width(2.dp))
                            Text("Solved", fontSize = 8.sp, color = NavyPrimary, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Incident Data Table with Real Jammu Locations
            Text(
                text = "CORRIDOR DISPATCH TABLE (REAL-TIME BUFFER):",
                color = NavyPrimary,
                fontSize = 10.sp,
                fontWeight = FontWeight.Black,
                fontFamily = FontFamily.Monospace,
                letterSpacing = 0.5.sp,
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
                    Text(text = "CATEGORY", color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace, modifier = Modifier.weight(1.3f))
                    Text(text = "JAMMU LOCATION", color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace, modifier = Modifier.weight(1.4f))
                    Text(text = "STATUS", color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace, modifier = Modifier.weight(0.9f))
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
                        Text(text = row.category, color = NavyPrimary, fontSize = 9.sp, fontWeight = FontWeight.Medium, modifier = Modifier.weight(1.3f))
                        Text(text = row.location, color = SlateSecondary, fontSize = 8.sp, modifier = Modifier.weight(1.4f))
                        Row(modifier = Modifier.weight(0.9f), verticalAlignment = Alignment.CenterVertically) {
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

            // Institutional Civic-Tech attribution
            RuggedCard(backgroundColor = Color.White) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Jammu Smart City Municipal Mission",
                            color = NavyPrimary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Engineered by Team HyperEdge · Central University of Jammu. Designed for high-reliability municipal triage across border corridors and signal dead zones.",
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

@Composable
fun MapPinWithIcon(
    icon: ImageVector,
    bgColor: Color,
    label: String,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .size(20.dp)
                .clip(CircleShape)
                .background(bgColor)
                .border(1.5.dp, Color.White, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(11.dp)
            )
        }
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(2.dp))
                .background(NavyDark.copy(alpha = 0.85f))
                .padding(horizontal = 3.dp, vertical = 1.dp)
        ) {
            Text(
                text = label,
                color = Color.White,
                fontSize = 6.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace
            )
        }
    }
}
