package com.example.ui.screens

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.GreenSuccess
import com.example.ui.theme.NavyDark
import com.example.ui.theme.OrangeAccent
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onContinue: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Auto-advance after 3 seconds
    LaunchedEffect(Unit) {
        delay(3200)
        onContinue()
    }

    // Pulsing animation for offline mode active badge
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(800),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseAlpha"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(NavyDark)
            .clickable { onContinue() }
            .testTag("splash_screen")
    ) {
        // Subtle dotted grid background
        Canvas(modifier = Modifier.fillMaxSize()) {
            val step = 28.dp.toPx()
            val dotRadius = 1.2.dp.toPx()
            var x = step / 2
            while (x < size.width) {
                var y = step / 2
                while (y < size.height) {
                    drawCircle(
                        color = Color(0xFF193B5C),
                        radius = dotRadius,
                        center = Offset(x, y)
                    )
                    y += step
                }
                x += step
            }
        }

        // Top Left: Green Pill Badge "● OFFLINE MODE: ACTIVE"
        Box(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(top = 36.dp, start = 20.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(Color(0xFF0B2E1C))
                .padding(horizontal = 10.dp, vertical = 6.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(GreenSuccess)
                        .alpha(pulseAlpha)
                )
                Spacer(modifier = Modifier.size(6.dp))
                Text(
                    text = "OFFLINE MODE: ACTIVE",
                    color = GreenSuccess,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Black,
                    fontFamily = FontFamily.Monospace,
                    letterSpacing = 0.5.sp
                )
            }
        }

        // Top Right: Tap to skip indicator
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 36.dp, end = 20.dp)
                .clip(RoundedCornerShape(3.dp))
                .background(Color(0xFF14375A))
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Text(
                text = "TAP TO ENTER →",
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 10.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold
            )
        }

        // Center Brand Block
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.Start
        ) {
            // Emblems & Institutional crest
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 12.dp)
            ) {
                com.example.ui.components.JmcSealLogo(modifier = Modifier.size(32.dp))
                Spacer(modifier = Modifier.size(10.dp))
                Text(
                    text = "JAMMU MUNICIPAL CORPORATION",
                    color = Color.White.copy(alpha = 0.75f),
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 1.sp
                )
            }

            // Large White Bold "HYPEREDGE"
            Text(
                text = "HYPEREDGE",
                color = Color.White,
                fontSize = 42.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 1.sp,
                lineHeight = 44.sp
            )

            // Orange Wavy Underline
            Canvas(
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .height(16.dp)
                    .padding(vertical = 4.dp)
            ) {
                val path = Path()
                val waveLength = 20.dp.toPx()
                val amplitude = 3.dp.toPx()
                val midY = size.height / 2
                path.moveTo(0f, midY)
                var currentX = 0f
                while (currentX < size.width) {
                    path.quadraticTo(
                        currentX + waveLength / 4, midY - amplitude,
                        currentX + waveLength / 2, midY
                    )
                    path.quadraticTo(
                        currentX + 3 * waveLength / 4, midY + amplitude,
                        currentX + waveLength, midY
                    )
                    currentX += waveLength
                }
                drawPath(
                    path = path,
                    color = OrangeAccent,
                    style = Stroke(width = 3.dp.toPx())
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Orange Bold: "Civic Services That Don't Wait for Wi-Fi"
            Text(
                text = "Civic Services That Don't Wait for Wi-Fi",
                color = OrangeAccent,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 22.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            // White Subtitle: "Bridging Jammu's connectivity divide through edge computing"
            Text(
                text = "Bridging Jammu's connectivity divide through edge computing.",
                color = Color.White.copy(alpha = 0.85f),
                fontSize = 14.sp,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(18.dp))

            // Technical Specs pill array
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                listOf("SQLite Encrypted", "ChaCha20", "Mesh Protocol").forEach { tag ->
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(2.dp))
                            .background(Color(0xFF14324E))
                            .padding(horizontal = 6.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = tag,
                            color = Color.White.copy(alpha = 0.7f),
                            fontSize = 9.sp,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }
            }
        }

        // Bottom Footer details
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            // Bottom-Left
            Column {
                Text(
                    text = "Team HyperEdge",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Central University of Jammu",
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 10.sp
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Open Innovation | Smart City Jammu",
                    color = OrangeAccent,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            // Bottom-Right
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "No Signal. No Problem.",
                    color = OrangeAccent,
                    fontSize = 13.sp,
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "v0.4.2 · build 128",
                    color = Color.White.copy(alpha = 0.5f),
                    fontSize = 8.sp,
                    fontFamily = FontFamily.Monospace
                )
            }
        }
    }
}
