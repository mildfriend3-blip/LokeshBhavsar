package com.example.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
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

@Composable
fun EdgeAiValidationScreen(
    latestReport: GrievanceReport?,
    isOnline: Boolean,
    isHindi: Boolean = false,
    onToggleOnline: () -> Unit,
    onToggleLanguage: () -> Unit = {},
    onRetakePhoto: () -> Unit,
    onProceedToStorage: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val report = latestReport ?: GrievanceReport(
        id = "HYE-0042",
        category = "Sanitation & Garbage Overflow",
        description = "Sanitary waste corridor accumulation near Trikuta Nagar Sector 4.",
        location = "Ward 12 · Trikuta Nagar, Sector 4 / Zone 2",
        status = "SEALED",
        formattedTime = "14:12:08 IST",
        shaHash = "SHA-256: 8F2A-91C8-3D4E-7B21",
        aiConfidence = 94,
        photometryStatus = "CLEAR PHOTOMETRY · NO BLUR",
        payloadKb = 47,
        imagePreset = "sanitation"
    )

    val photoUrl = when (report.imagePreset) {
        "sanitation" -> "https://images.unsplash.com/photo-1605600659873-d808a13e4d2a?w=400&q=80"
        "water" -> "https://images.unsplash.com/photo-1584467735815-f778f274e296?w=400&q=80"
        "pothole" -> "https://images.unsplash.com/photo-1515162816999-a0c47dc192f7?w=400&q=80"
        "light" -> "https://images.unsplash.com/photo-1509114397022-ed747cca3f65?w=400&q=80"
        else -> "https://images.unsplash.com/photo-1542601906990-b4d3fb778b09?w=400&q=80"
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(CreamBackground)
    ) {
        HyperEdgeHeader(
            screenNumber = 5,
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
            // Screen Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = if (isHindi) "एज एआई सत्यापन" else "Edge AI Validation",
                        color = NavyPrimary,
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Black
                    )
                    Text(
                        text = "INCIDENT PIPELINE · ON-DEVICE VISION AUDIT",
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
                        .padding(horizontal = 7.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = "AI VERIFIED (${report.aiConfidence}%)",
                        color = GreenSuccess,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Large Photo Preview with Real Photo & Edge AI Detection HUD
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(210.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .border(1.dp, CardBorderNavy, RoundedCornerShape(4.dp))
                    .background(NavyDark)
            ) {
                // Real Image
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(photoUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = "Validated evidence",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Overlay Grid Canvas
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val w = size.width
                    val h = size.height
                    val dash = PathEffect.dashPathEffect(floatArrayOf(6f, 6f), 0f)

                    // 3x3 rule of thirds grid
                    drawLine(Color(0x55FFFFFF), Offset(w * 0.33f, 0f), Offset(w * 0.33f, h), 1f, pathEffect = dash)
                    drawLine(Color(0x55FFFFFF), Offset(w * 0.66f, 0f), Offset(w * 0.66f, h), 1f, pathEffect = dash)
                    drawLine(Color(0x55FFFFFF), Offset(0f, h * 0.33f), Offset(w, h * 0.33f), 1f, pathEffect = dash)
                    drawLine(Color(0x55FFFFFF), Offset(0f, h * 0.66f), Offset(w, h * 0.66f), 1f, pathEffect = dash)

                    // AI Bounding Box in Orange
                    drawRect(
                        color = OrangeAccent,
                        topLeft = Offset(w * 0.15f, h * 0.15f),
                        size = androidx.compose.ui.geometry.Size(w * 0.70f, h * 0.70f),
                        style = androidx.compose.ui.graphics.drawscope.Stroke(width = 2.dp.toPx())
                    )
                }

                // AI Detection Label on Top-Left of Box
                Box(
                    modifier = Modifier
                        .padding(top = 18.dp, start = 22.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(OrangeAccent)
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "DETECTED: ${report.category.uppercase()}",
                        color = Color.White,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.Monospace
                    )
                }

                // Rubber-stamp seal in corner
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(bottom = 30.dp, end = 12.dp)
                        .rotate(-5f)
                        .border(2.dp, RedError, RoundedCornerShape(3.dp))
                        .background(Color.White.copy(alpha = 0.9f))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "SEALED",
                            color = RedError,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black,
                            fontFamily = FontFamily.Monospace
                        )
                        Text(
                            text = "JMC WARD 12",
                            color = RedError,
                            fontSize = 6.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }

                // Bottom HUD strip
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .background(NavyPrimary.copy(alpha = 0.95f))
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "PHOTOMETRIC ISO: 160 · f/1.8 · NO BLUR",
                            color = SlateLight,
                            fontSize = 8.sp,
                            fontFamily = FontFamily.Monospace
                        )
                        Text(
                            text = "SEAL ID: ${report.id}",
                            color = OrangeAccent,
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 4 Telemetry Spec Cards
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                // Metric 1: Clear Photometry
                RuggedCard(backgroundColor = Color.White) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = GreenSuccess,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = "CLEAR PHOTOMETRY · NO BLUR",
                                    color = NavyPrimary,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Black,
                                    fontFamily = FontFamily.Monospace
                                )
                                Text(
                                    text = "Laplacian variance > 280 · Adequate ambient lux",
                                    color = SlateSecondary,
                                    fontSize = 10.sp
                                )
                            }
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(2.dp))
                                .background(GreenTint)
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "PASS",
                                color = GreenSuccess,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }
                }

                // Metric 2: SHA-256 Hash
                RuggedCard(backgroundColor = Color.White) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Fingerprint,
                            contentDescription = null,
                            tint = NavyPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "CRYPTOGRAPHIC LEDGER DIGEST",
                                color = SlateSecondary,
                                fontSize = 9.sp,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = report.shaHash,
                                color = NavyPrimary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }
                }

                // Metric 3: Duplicate Check
                RuggedCard(backgroundColor = Color.White) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Memory,
                                contentDescription = null,
                                tint = GreenSuccess,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = "0 DUPLICATES DETECTED",
                                    color = GreenSuccess,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Black,
                                    fontFamily = FontFamily.Monospace
                                )
                                Text(
                                    text = "Ward 12 local mesh buffer checked within 50m radius",
                                    color = SlateSecondary,
                                    fontSize = 10.sp
                                )
                            }
                        }
                        Text(
                            text = "UNIQUE",
                            color = GreenSuccess,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }

                // Metric 4: Category Auto-suggested
                RuggedCard(backgroundColor = OrangeTint, borderColor = OrangeAccent) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Key,
                                contentDescription = null,
                                tint = OrangeAccent,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = "CATEGORY AUTO-CONFIRMED",
                                    color = SlateSecondary,
                                    fontSize = 9.sp,
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "${report.category} (confidence 91%)",
                                    color = NavyPrimary,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Black
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Action Buttons: Retake vs Proceed
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onRetakePhoto,
                    shape = RoundedCornerShape(4.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = NavyPrimary),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("retake_photo_button")
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = null,
                            tint = NavyPrimary,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "RETAKE PHOTO",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }

                Button(
                    onClick = onProceedToStorage,
                    colors = ButtonDefaults.buttonColors(containerColor = OrangeAccent),
                    shape = RoundedCornerShape(4.dp),
                    modifier = Modifier
                        .weight(1.3f)
                        .testTag("proceed_to_storage_button")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "PROCEED TO STORAGE (6/8)",
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black,
                            fontFamily = FontFamily.Monospace
                        )
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }
        }

        HyperEdgeFooter()
    }
}
