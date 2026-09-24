package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
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
import com.example.ui.theme.GreenSuccess
import com.example.ui.theme.NavyPrimary
import com.example.ui.theme.OrangeAccent
import com.example.ui.theme.SlateLight
import com.example.ui.theme.SlateSecondary

data class CategoryChipItem(
    val id: String,
    val title: String,
    val defaultDesc: String,
    val preset: String,
    val photoUrl: String
)

val CATEGORY_CHIPS = listOf(
    CategoryChipItem(
        id = "sadak",
        title = "Sadak (Potholes)",
        defaultDesc = "Severe asphalt cavity with stagnant water pool near Canal Road bend.",
        preset = "pothole",
        photoUrl = "https://images.unsplash.com/photo-1515162816999-a0c47dc192f7?w=800&auto=format&fit=crop&q=80"
    ),
    CategoryChipItem(
        id = "kachra",
        title = "Kachra (Garbage)",
        defaultDesc = "Solid municipal waste container overflow blocking carriageway.",
        preset = "garbage",
        photoUrl = "https://images.unsplash.com/photo-1532996122724-e3c354a0b15b?w=800&auto=format&fit=crop&q=80"
    ),
    CategoryChipItem(
        id = "bijli",
        title = "Bijli (Streetlights)",
        defaultDesc = "Three consecutive overhead sodium fixtures dark creating pedestrian blind zone.",
        preset = "streetlight",
        photoUrl = "https://images.unsplash.com/photo-1509198397868-475647b2a1e5?w=800&auto=format&fit=crop&q=80"
    ),
    CategoryChipItem(
        id = "paani",
        title = "Paani (Water Leakage)",
        defaultDesc = "Underground potable main burst spraying continuous water over roadway.",
        preset = "water",
        photoUrl = "https://images.unsplash.com/photo-1584467735815-f778f274e296?w=800&auto=format&fit=crop&q=80"
    ),
    CategoryChipItem(
        id = "nala",
        title = "Nala (Drainage)",
        defaultDesc = "Stormwater conduit choked by silt and debris causing blackwater backup.",
        preset = "drainage",
        photoUrl = "https://images.unsplash.com/photo-1584467735815-f778f274e296?w=800&auto=format&fit=crop&q=80"
    )
)

@Composable
fun ReportIssueScreen(
    onSaveToVault: (GrievanceReport) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var selectedCategory by remember { mutableStateOf(CATEGORY_CHIPS[0]) }
    var descriptionText by remember { mutableStateOf(CATEGORY_CHIPS[0].defaultDesc) }
    var landmarkText by remember { mutableStateOf("Canal Road, Sec 3, Trikuta Nagar (Ward 12)") }
    var isCalibrating by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF9FAFB))
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Form Title
        Column {
            Text(
                text = "Report Civic Issue",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0F172A),
                letterSpacing = (-0.3).sp
            )
            Text(
                text = "Edge AI verifies photo on-device and signs ticket offline",
                fontSize = 12.sp,
                color = SlateSecondary
            )
        }

        // Section 1: Category Chips (Horizontal scrollable chips)
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(
                text = "ISSUE CATEGORY",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                color = Color(0xFF475569)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CATEGORY_CHIPS.forEach { chip ->
                    val isSelected = selectedCategory.id == chip.id
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .border(
                                width = 1.dp,
                                color = if (isSelected) OrangeAccent else Color(0xFFE2E8F0),
                                shape = RoundedCornerShape(20.dp)
                            )
                            .background(if (isSelected) OrangeAccent else Color.White)
                            .clickable {
                                selectedCategory = chip
                                descriptionText = chip.defaultDesc
                            }
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = chip.title,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) Color.White else Color(0xFF1E293B)
                        )
                    }
                }
            }
        }

        // Section 2: Simulated Camera Viewfinder Box (Edge AI Validation)
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "EDGE AI VALIDATION",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    color = Color(0xFF475569)
                )

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFDCFCE7))
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "TFLite Active",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF166534)
                    )
                }
            }

            // Camera Viewfinder Box with HUD Overlay
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(4f / 3f)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0xFF0F172A))
            ) {
                // Real photo
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(selectedCategory.photoUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = "Camera viewfinder preview",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Top GPS HUD Chip
                Box(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(10.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color.Black.copy(alpha = 0.65f))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.MyLocation,
                            contentDescription = null,
                            tint = GreenSuccess,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "32.7058° N, 74.8732° E · Ward 12",
                            fontSize = 10.sp,
                            fontFamily = FontFamily.Monospace,
                            color = Color.White,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                // AI Bounding Box Reticle
                Box(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(170.dp, 120.dp)
                        .border(2.dp, OrangeAccent, RoundedCornerShape(8.dp))
                        .padding(6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .clip(RoundedCornerShape(4.dp))
                            .background(OrangeAccent)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "${selectedCategory.title.substringBefore(" ").uppercase()} DETECTED · 94%",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }

                // Bottom telemetry readout
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .fillMaxWidth()
                        .background(Color.Black.copy(alpha = 0.72f))
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Clarity: 93.8% PASS (Sharp)",
                            fontSize = 10.sp,
                            color = GreenSuccess,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                        Text(
                            text = "SHA-256: 8F2A-91C8",
                            fontSize = 10.sp,
                            color = Color.White.copy(alpha = 0.8f),
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }
            }
        }

        // Section 3: Description & Location Inputs
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(
                text = "ISSUE DESCRIPTION",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                color = Color(0xFF475569)
            )

            OutlinedTextField(
                value = descriptionText,
                onValueChange = { descriptionText = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("description_input"),
                placeholder = { Text("Describe civic hazard...") },
                shape = RoundedCornerShape(10.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedBorderColor = OrangeAccent,
                    unfocusedBorderColor = Color(0xFFCBD5E1)
                ),
                maxLines = 3
            )

            OutlinedTextField(
                value = landmarkText,
                onValueChange = { landmarkText = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("location_input"),
                label = { Text("Ward Location & Landmark") },
                shape = RoundedCornerShape(10.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedBorderColor = OrangeAccent,
                    unfocusedBorderColor = Color(0xFFCBD5E1)
                ),
                singleLine = true
            )
        }

        // Section 4: Prominent Orange CTA Button
        Button(
            onClick = {
                val newId = "HYE-${(100..999).random()}"
                val report = GrievanceReport(
                    id = newId,
                    category = selectedCategory.title,
                    description = descriptionText,
                    location = landmarkText,
                    coordinates = "32.7058° N, 74.8732° E",
                    status = "PENDING",
                    formattedTime = "Just now",
                    shaHash = "SHA-256: 8F2A-${(1000..9999).random().toString(16).uppercase()}",
                    aiConfidence = 94,
                    imagePreset = selectedCategory.preset
                )
                onSaveToVault(report)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .shadow(elevation = 2.dp, shape = RoundedCornerShape(12.dp))
                .testTag("save_vault_button"),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = OrangeAccent,
                contentColor = Color.White
            )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Text(
                    text = "Save to Offline Vault",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.2.sp
                )
            }
        }

        Text(
            text = "Stored directly on flash with ChaCha20 encryption. No internet connection needed.",
            fontSize = 11.sp,
            color = SlateSecondary,
            modifier = Modifier.padding(horizontal = 4.dp)
        )

        Spacer(modifier = Modifier.height(60.dp))
    }
}
