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
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.ui.components.HyperEdgeFooter
import com.example.ui.components.HyperEdgeHeader
import com.example.ui.components.RuggedCard
import com.example.ui.theme.CreamBackground
import com.example.ui.theme.GreenSuccess
import com.example.ui.theme.NavyPrimary
import com.example.ui.theme.OrangeAccent
import com.example.ui.theme.RedError
import com.example.ui.theme.RedTint
import com.example.ui.theme.SlateSecondary

@Composable
fun ProblemScreen(
    isOnline: Boolean,
    onToggleOnline: () -> Unit,
    onNext: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(CreamBackground)
    ) {
        HyperEdgeHeader(
            screenNumber = 2,
            isOnline = isOnline,
            onToggleOnline = onToggleOnline
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(14.dp)
        ) {
            // Main Header Title
            Text(
                text = "The Problem: Signal Drops, Help Stops",
                color = NavyPrimary,
                fontSize = 19.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = (-0.2).sp
            )
            Text(
                text = "JAMMU TERRAIN CONNECTIVITY PROFILE · FIELD CORRIDOR AUDIT",
                color = SlateSecondary,
                fontSize = 10.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 2.dp, bottom = 12.dp)
            )

            // Tactical Jammu Region Map Outline & Dead Zone Diagram
            RuggedCard(
                backgroundColor = Color.White,
                modifier = Modifier.padding(bottom = 12.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "REGIONAL SIGNAL CONTOUR: JAMMU",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace,
                            color = NavyPrimary
                        )
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(2.dp))
                                .background(RedTint)
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "CRITICAL DEAD-ZONE",
                                color = RedError,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Map Schematic Canvas
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(130.dp)
                            .background(Color(0xFFF7F4EC))
                            .border(1.dp, Color(0xFFDCD6C8), RoundedCornerShape(2.dp))
                    ) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val w = size.width
                            val h = size.height
                            val dashedEffect = PathEffect.dashPathEffect(floatArrayOf(8f, 6f), 0f)

                            // City centre circle (dashed green)
                            drawCircle(
                                color = GreenSuccess,
                                radius = 32.dp.toPx(),
                                center = Offset(w * 0.32f, h * 0.52f),
                                style = androidx.compose.ui.graphics.drawscope.Stroke(
                                    width = 2.dp.toPx(),
                                    pathEffect = dashedEffect
                                )
                            )

                            // Red dashed vertical line marking dead-zone division
                            drawLine(
                                color = RedError,
                                start = Offset(w * 0.58f, 0f),
                                end = Offset(w * 0.58f, h),
                                strokeWidth = 2.dp.toPx(),
                                pathEffect = dashedEffect
                            )
                        }

                        // City Centre Annotation
                        Column(
                            modifier = Modifier
                                .align(Alignment.CenterStart)
                                .padding(start = 22.dp)
                        ) {
                            Text(
                                text = "City Centre",
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                color = NavyPrimary
                            )
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(2.dp))
                                    .background(GreenSuccess)
                                    .padding(horizontal = 4.dp, vertical = 1.dp)
                            ) {
                                Text(
                                    text = "Strong 4G/5G",
                                    color = Color.White,
                                    fontSize = 8.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        // Mountains / Border annotations
                        Column(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(top = 10.dp, end = 12.dp),
                            horizontalAlignment = Alignment.End
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = "▲", color = RedError, fontSize = 11.sp)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Hilly — No signal",
                                    color = RedError,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(modifier = Modifier.height(18.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = "▲", color = RedError, fontSize = 11.sp)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Border — No signal",
                                    color = RedError,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        // Dead zone label on line
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(bottom = 6.dp, end = 60.dp)
                        ) {
                            Text(
                                text = "DEAD ZONE ──►",
                                color = RedError,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Black,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }
                }
            }

            // 3 Connected Flow Boxes
            Text(
                text = "TRADITIONAL CLOUD-DEPENDENT FORM FAILURE:",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                color = NavyPrimary,
                modifier = Modifier.padding(vertical = 4.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Box 1: Opens civic app
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .border(1.dp, NavyPrimary, RoundedCornerShape(3.dp))
                        .background(Color.White)
                        .padding(vertical = 8.dp, horizontal = 4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Opens civic app",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = NavyPrimary
                    )
                }

                Text(
                    text = "→",
                    color = RedError,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Black,
                    modifier = Modifier.padding(horizontal = 4.dp)
                )

                // Box 2: No internet (red)
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .border(1.dp, RedError, RoundedCornerShape(3.dp))
                        .background(RedTint)
                        .padding(vertical = 8.dp, horizontal = 4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No internet",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = RedError
                    )
                }

                Text(
                    text = "→",
                    color = RedError,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Black,
                    modifier = Modifier.padding(horizontal = 4.dp)
                )

                // Box 3: Form fails to load (red)
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .border(1.dp, RedError, RoundedCornerShape(3.dp))
                        .background(RedError)
                        .padding(vertical = 8.dp, horizontal = 4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Form fails",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // 4 White Cards Below
            val problemCards = listOf(
                Pair("Two Jammus", "Smart City core enjoys fiber & 5G while peri-urban wards (Nagrota, Bahu, outskirts) face frequent signal drops."),
                Pair("Dead-zone trap", "Citizens spot open manholes or broken mains exactly where terrain blocks mobile data packets."),
                Pair("Silent failure", "Browser-based portals discard citizen inputs silently upon HTTP timeout with zero offline recovery."),
                Pair("Server overload", "Monsoon storms trigger concurrent requests that choke centralized municipal municipal web servers.")
            )

            problemCards.chunked(2).forEach { rowCards ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    rowCards.forEach { (title, desc) ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .border(1.dp, NavyPrimary.copy(alpha = 0.8f), RoundedCornerShape(4.dp))
                                .background(Color.White)
                                .padding(10.dp)
                        ) {
                            Column {
                                Text(
                                    text = title,
                                    color = RedError,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Black
                                )
                                Spacer(modifier = Modifier.height(3.dp))
                                Text(
                                    text = desc,
                                    color = SlateSecondary,
                                    fontSize = 10.sp,
                                    lineHeight = 14.sp
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Bottom Red Banner
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(4.dp))
                    .background(RedError)
                    .padding(10.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = "Warning",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "A civic system that needs internet fails the last mile.",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Action Button to Screen 3
            Button(
                onClick = onNext,
                colors = ButtonDefaults.buttonColors(containerColor = OrangeAccent),
                shape = RoundedCornerShape(4.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("proceed_to_solution_button")
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "EXPLORE OFFLINE SOLUTION (3/8)",
                        color = Color.White,
                        fontWeight = FontWeight.Black,
                        fontSize = 13.sp,
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
