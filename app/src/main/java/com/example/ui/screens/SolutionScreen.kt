package com.example.ui.screens

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
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
import com.example.ui.theme.NavyDark
import com.example.ui.theme.NavyPrimary
import com.example.ui.theme.OrangeAccent
import com.example.ui.theme.OrangeTint
import com.example.ui.theme.SlateLight
import com.example.ui.theme.SlateSecondary

@Composable
fun SolutionScreen(
    isOnline: Boolean,
    isHindi: Boolean = false,
    onToggleOnline: () -> Unit,
    onToggleLanguage: () -> Unit = {},
    onProceedToFileGrievance: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(CreamBackground)
    ) {
        HyperEdgeHeader(
            screenNumber = 3,
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
            // Header Title
            Text(
                text = "Our Solution: Works Fully Offline",
                color = NavyPrimary,
                fontSize = 19.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = (-0.2).sp
            )
            Text(
                text = "EDGE-NATIVE ARCHITECTURE · ZERO CLOUD-BLOCK PIPELINE",
                color = SlateSecondary,
                fontSize = 10.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 2.dp, bottom = 12.dp)
            )

            // 4 Step Boxes Horizontally
            RuggedCard(
                backgroundColor = Color.White,
                modifier = Modifier.padding(bottom = 12.dp)
            ) {
                Column {
                    Text(
                        text = "HYPEREDGE INCIDENT CONVEYANCE LIFECYCLE:",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        color = NavyPrimary
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        // 1 CAPTURE
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .border(1.dp, NavyPrimary, RoundedCornerShape(3.dp))
                                .background(Color(0xFFF9F7F2))
                                .padding(vertical = 8.dp, horizontal = 4.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "1 CAPTURE",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Black,
                                    fontFamily = FontFamily.Monospace,
                                    color = NavyPrimary
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "Draft + photo",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = SlateSecondary
                                )
                            }
                        }

                        // 2 VALIDATE
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .border(1.dp, NavyPrimary, RoundedCornerShape(3.dp))
                                .background(Color(0xFFF9F7F2))
                                .padding(vertical = 8.dp, horizontal = 4.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "2 VALIDATE",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Black,
                                    fontFamily = FontFamily.Monospace,
                                    color = NavyPrimary
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "Phone checks",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = SlateSecondary
                                )
                            }
                        }

                        // 3 STORE
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .border(1.dp, NavyPrimary, RoundedCornerShape(3.dp))
                                .background(Color(0xFFF9F7F2))
                                .padding(vertical = 8.dp, horizontal = 4.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "3 STORE",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Black,
                                    fontFamily = FontFamily.Monospace,
                                    color = NavyPrimary
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "Encrypted",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = SlateSecondary
                                )
                            }
                        }

                        // 4 SYNC (orange border)
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .border(2.dp, OrangeAccent, RoundedCornerShape(3.dp))
                                .background(OrangeTint)
                                .padding(vertical = 8.dp, horizontal = 4.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "4 SYNC",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Black,
                                    fontFamily = FontFamily.Monospace,
                                    color = OrangeAccent
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "Auto-sends",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = NavyPrimary
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Thick Dual Status Bar: Steps 1-3 vs Step 4
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        // Green thick bar under boxes 1-3: "NO INTERNET NEEDED"
                        Box(
                            modifier = Modifier
                                .weight(3f)
                                .height(26.dp)
                                .clip(RoundedCornerShape(2.dp))
                                .background(GreenSuccess),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "✔ NO INTERNET NEEDED",
                                color = Color.White,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Black,
                                fontFamily = FontFamily.Monospace,
                                letterSpacing = 0.5.sp
                            )
                        }

                        // Orange thick bar under box 4: "WHEN SIGNAL RETURNS"
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(26.dp)
                                .clip(RoundedCornerShape(2.dp))
                                .background(OrangeAccent),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "ON SIGNAL",
                                color = Color.White,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Black,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }
                }
            }

            // 3 Technical Value Cards
            val solutionCards = listOf(
                Triple(
                    "Zero dead ends",
                    "Forms never hang or lose user input. All grievances commit atomically to local SQLite storage with immediate cryptographic confirmation.",
                    Icons.Default.Speed
                ),
                Triple(
                    "Zero cloud cost",
                    "On-device edge AI runs lightweight photometry and duplicate detection models on citizen hardware, saving municipal bandwidth and compute bills.",
                    Icons.Default.Memory
                ),
                Triple(
                    "Private by design",
                    "Sensitive civic imagery and GPS coordinates are encrypted locally using ChaCha20-Poly1305 before any network packet is dispatched.",
                    Icons.Default.Lock
                )
            )

            solutionCards.forEach { (title, desc, icon) ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 5.dp)
                        .border(1.dp, NavyPrimary, RoundedCornerShape(4.dp))
                        .background(Color.White)
                        .padding(12.dp)
                ) {
                    Row(verticalAlignment = Alignment.Top) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(RoundedCornerShape(3.dp))
                                .background(NavyPrimary),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = icon,
                                contentDescription = null,
                                tint = OrangeAccent,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = title,
                                color = NavyPrimary,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = desc,
                                color = SlateSecondary,
                                fontSize = 11.sp,
                                lineHeight = 16.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Tech specs banner
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(NavyDark)
                    .border(1.dp, OrangeAccent, RoundedCornerShape(4.dp))
                    .padding(10.dp)
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
                            text = "OFFLINE SQLite PERSISTENCE ENGINE",
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                        Text(
                            text = "All inputs persist safely during battery loss or network brownouts.",
                            color = SlateLight,
                            fontSize = 10.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Action Button to Screen 4
            Button(
                onClick = onProceedToFileGrievance,
                colors = ButtonDefaults.buttonColors(containerColor = OrangeAccent),
                shape = RoundedCornerShape(4.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("open_file_grievance_button")
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "OPEN GRIEVANCE FORM (4/8)",
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
