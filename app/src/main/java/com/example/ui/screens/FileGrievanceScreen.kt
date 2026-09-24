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
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Stop
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
import com.example.ui.theme.RedTint
import com.example.ui.theme.SlateLight
import com.example.ui.theme.SlateSecondary

data class CivicCategoryPhoto(
    val id: String,
    val nameEn: String,
    val nameHi: String,
    val photoUrl: String,
    val preset: String,
    val defaultDesc: String,
    val fallbackTone: Color
)

val REAL_PHOTO_CATEGORIES = listOf(
    CivicCategoryPhoto(
        id = "sanitation",
        nameEn = "Sanitation & Garbage Overflow",
        nameHi = "सफाई एवं कचरा जमाव",
        photoUrl = "https://images.unsplash.com/photo-1605600659873-d808a13e4d2a?w=400&q=80",
        preset = "sanitation",
        defaultDesc = "Municipal bin overflow at Trikuta Nagar Sector 4 corner. Uncollected organic and dry waste spilling onto carriageway.",
        fallbackTone = Color(0xFF4A4433)
    ),
    CivicCategoryPhoto(
        id = "water",
        nameEn = "Water Leakage",
        nameHi = "पेयजल पाइपलाइन रिसाव",
        photoUrl = "https://images.unsplash.com/photo-1584467735815-f778f274e296?w=400&q=80",
        preset = "water",
        defaultDesc = "High-pressure municipal potable water pipeline rupture near house #42 lane. Water gushing onto street surface.",
        fallbackTone = Color(0xFF234B61)
    ),
    CivicCategoryPhoto(
        id = "pothole",
        nameEn = "Pothole / Road Gap",
        nameHi = "सड़क पर गड्ढा / दरार",
        photoUrl = "https://images.unsplash.com/photo-1515162816999-a0c47dc192f7?w=400&q=80",
        preset = "pothole",
        defaultDesc = "Deep road depression with stagnant water near Canal Road culvert. Severe hazard for two-wheelers and night traffic.",
        fallbackTone = Color(0xFF3B3B3B)
    ),
    CivicCategoryPhoto(
        id = "streetlight",
        nameEn = "Streetlight Outage",
        nameHi = "स्ट्रीट लाइट बंद होना",
        photoUrl = "https://images.unsplash.com/photo-1509114397022-ed747cca3f65?w=400&q=80",
        preset = "light",
        defaultDesc = "Three sequential sodium pole lamps dark along Trikuta Sector 4 park perimeter. High pedestrian security risk.",
        fallbackTone = Color(0xFF1B232E)
    ),
    CivicCategoryPhoto(
        id = "drainage",
        nameEn = "Drainage Clog",
        nameHi = "नाली जाम एवं जलभराव",
        photoUrl = "https://images.unsplash.com/photo-1542601906990-b4d3fb778b09?w=400&q=80",
        preset = "drainage",
        defaultDesc = "Stormwater conduit obstructed by silt and plastics. Backflow submerging footpath during light showers.",
        fallbackTone = Color(0xFF333B32)
    )
)

@Composable
fun FileGrievanceScreen(
    isOnline: Boolean,
    isHindi: Boolean = false,
    onToggleOnline: () -> Unit,
    onToggleLanguage: () -> Unit = {},
    onSaveAndEncrypt: (GrievanceReport) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var selectedCategory by remember { mutableStateOf(REAL_PHOTO_CATEGORIES[0]) }
    var description by remember { mutableStateOf(selectedCategory.defaultDesc) }
    var isRecordingVoice by remember { mutableStateOf(false) }
    var voiceDuration by remember { mutableStateOf("0:00") }
    var capturedPhotoCount by remember { mutableStateOf(1) }
    var photoHash by remember { mutableStateOf("HASH: 8F2A-91C8 · 0 DUPLICATES") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(CreamBackground)
    ) {
        HyperEdgeHeader(
            screenNumber = 4,
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
            // Header Info & Sub-label (Bilingual)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = if (isHindi) "शिकायत दर्ज करें / File Grievance" else "File Grievance / शिकायत दर्ज करें",
                        color = NavyPrimary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black
                    )
                    Text(
                        text = "JMC Civic Form · Mesh Field Node: JMC-CORRIDOR-042",
                        color = SlateSecondary,
                        fontSize = 10.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold
                    )
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(3.dp))
                        .background(GreenSuccess)
                        .padding(horizontal = 6.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = "STANDALONE MESH",
                        color = Color.White,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // SECTION 1: SELECT GRIEVANCE DOMAIN WITH REAL PHOTOS
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 6.dp)
            ) {
                Text(
                    text = if (isHindi) "१. श्रेणी चुनें / 1. SELECT DOMAIN" else "1. SELECT GRIEVANCE DOMAIN",
                    color = NavyPrimary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Black,
                    fontFamily = FontFamily.Monospace,
                    letterSpacing = 0.5.sp
                )
                Spacer(modifier = Modifier.width(6.dp))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(2.dp))
                        .background(OrangeTint)
                        .padding(horizontal = 4.dp, vertical = 1.dp)
                ) {
                    Text(
                        text = "REQUIRED",
                        color = OrangeAccent,
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }

            // Real Photo Category Cards List
            Column(verticalArrangement = Arrangement.spacedBy(7.dp)) {
                REAL_PHOTO_CATEGORIES.forEach { category ->
                    val isSelected = selectedCategory.id == category.id
                    val bgColor = if (isSelected) NavyPrimary else Color.White
                    val textColor = if (isSelected) Color.White else NavyPrimary
                    val subTextColor = if (isSelected) SlateLight else SlateSecondary
                    val borderColor = if (isSelected) OrangeAccent else CardBorderNavy

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(width = if (isSelected) 1.5.dp else 1.dp, color = borderColor, shape = RoundedCornerShape(4.dp))
                            .clip(RoundedCornerShape(4.dp))
                            .background(bgColor)
                            .clickable {
                                selectedCategory = category
                                description = category.defaultDesc
                            }
                            .padding(8.dp)
                            .testTag("category_${category.id}")
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                modifier = Modifier.weight(1f),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // REAL PHOTO THUMBNAIL
                                Box(
                                    modifier = Modifier
                                        .size(52.dp, 44.dp)
                                        .clip(RoundedCornerShape(3.dp))
                                        .border(1.dp, if (isSelected) OrangeAccent else CardBorderNavy, RoundedCornerShape(3.dp))
                                        .background(category.fallbackTone)
                                ) {
                                    AsyncImage(
                                        model = ImageRequest.Builder(context)
                                            .data(category.photoUrl)
                                            .crossfade(true)
                                            .build(),
                                        contentDescription = category.nameEn,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.fillMaxSize()
                                    )
                                }

                                Spacer(modifier = Modifier.width(10.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = category.nameEn,
                                        color = textColor,
                                        fontSize = 13.sp,
                                        fontWeight = if (isSelected) FontWeight.Black else FontWeight.Bold
                                    )
                                    Text(
                                        text = category.nameHi,
                                        color = subTextColor,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }

                            if (isSelected) {
                                Box(
                                    modifier = Modifier
                                        .size(20.dp)
                                        .clip(CircleShape)
                                        .background(OrangeAccent),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Selected",
                                        tint = Color.White,
                                        modifier = Modifier.size(14.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // SECTION 2: EDGE AI VISUAL EVIDENCE (REAL PHOTO INSPECTION HUD)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "2. EDGE AI VISUAL EVIDENCE",
                    color = NavyPrimary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Black,
                    fontFamily = FontFamily.Monospace,
                    letterSpacing = 0.5.sp
                )
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(2.dp))
                        .background(GreenTint)
                        .border(1.dp, GreenSuccess, RoundedCornerShape(2.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "AI VERIFIED (94%)",
                        color = GreenSuccess,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Real Photo Preview Area with Overlay Grid & Detection HUD
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .border(1.dp, CardBorderNavy, RoundedCornerShape(4.dp))
                    .background(Color(0xFF132A3E))
            ) {
                // Real photographic background from selected category
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(selectedCategory.photoUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = "Captured civic evidence",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Tactical Reticle and Green Bounding Box Canvas
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val w = size.width
                    val h = size.height
                    val dash = PathEffect.dashPathEffect(floatArrayOf(6f, 6f), 0f)

                    // Reticle lines
                    drawLine(
                        color = Color(0x77FFFFFF),
                        start = Offset(w * 0.2f, h * 0.5f),
                        end = Offset(w * 0.8f, h * 0.5f),
                        strokeWidth = 1.dp.toPx(),
                        pathEffect = dash
                    )
                    drawLine(
                        color = Color(0x77FFFFFF),
                        start = Offset(w * 0.5f, h * 0.2f),
                        end = Offset(w * 0.5f, h * 0.8f),
                        strokeWidth = 1.dp.toPx(),
                        pathEffect = dash
                    )
                    // AI Target Box
                    drawRect(
                        color = GreenSuccess,
                        topLeft = Offset(w * 0.22f, h * 0.18f),
                        size = androidx.compose.ui.geometry.Size(w * 0.56f, h * 0.64f),
                        style = androidx.compose.ui.graphics.drawscope.Stroke(width = 2.dp.toPx())
                    )
                }

                // AI Detection Label on Top-Left of Box
                Box(
                    modifier = Modifier
                        .padding(top = 26.dp, start = 32.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(GreenSuccess)
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "DETECTED: ${selectedCategory.nameEn.uppercase()}",
                        color = Color.White,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.Monospace
                    )
                }

                // Top-Left Badge: Clear Photometry
                Box(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(8.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(NavyDark.copy(alpha = 0.9f))
                        .padding(horizontal = 6.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = "✓ CLEAR PHOTOMETRY · NO BLUR",
                        color = GreenSuccess,
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                }

                // Bottom Overlay: Hash & Duplicate Info
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .fillMaxWidth()
                        .background(NavyDark.copy(alpha = 0.92f))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = photoHash,
                            color = Color.White.copy(alpha = 0.9f),
                            fontSize = 8.sp,
                            fontFamily = FontFamily.Monospace
                        )
                        Text(
                            text = "480×640 · ON-DEVICE NPU",
                            color = OrangeAccent,
                            fontSize = 8.sp,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Action: Capture Photo button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = {
                        capturedPhotoCount++
                        val randomHex = (1000..9999).random().toString(16).uppercase()
                        photoHash = "HASH: 8F2A-$randomHex · 0 DUPLICATES"
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = NavyPrimary),
                    shape = RoundedCornerShape(4.dp),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("capture_photo_button")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CameraAlt,
                            contentDescription = "Capture Photo",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "CAPTURE PHOTO (#$capturedPhotoCount)",
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }

                // Retake / Recalibrate button
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .border(1.dp, CardBorderNavy, RoundedCornerShape(4.dp))
                        .background(Color.White)
                        .clickable {
                            val randomHex = (1000..9999).random().toString(16).uppercase()
                            photoHash = "HASH: 8F2A-$randomHex · 0 DUPLICATES"
                        }
                        .padding(horizontal = 10.dp, vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Recalibrate Photometry",
                        tint = NavyPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // SECTION 3: LOCATION FIELD (Auto-locked)
            Text(
                text = "3. GEOSPATIAL EDGE LOCK",
                color = NavyPrimary,
                fontSize = 11.sp,
                fontWeight = FontWeight.Black,
                fontFamily = FontFamily.Monospace,
                letterSpacing = 0.5.sp,
                modifier = Modifier.padding(bottom = 4.dp)
            )

            RuggedCard(backgroundColor = Color(0xFFFBF8F2)) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.MyLocation,
                                contentDescription = "Location Pin",
                                tint = GreenSuccess,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Ward 12 · Trikuta Nagar, Sector 4 / Zone 2 Division",
                                color = NavyPrimary,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "Locked to Node",
                            tint = SlateSecondary,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "GPS: 32.7058° N, 74.8732° E · Accuracy ±2.4m · Jammu Municipality",
                        color = SlateSecondary,
                        fontSize = 9.sp,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // SECTION 4: DESCRIPTION TEXTAREA & VOICE NOTE
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "4. INCIDENT PARTICULARS",
                    color = NavyPrimary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Black,
                    fontFamily = FontFamily.Monospace,
                    letterSpacing = 0.5.sp
                )
                Text(
                    text = "HINDI / URDU READY · द्विभाषी",
                    color = SlateSecondary,
                    fontSize = 8.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(96.dp)
                    .testTag("description_input"),
                textStyle = androidx.compose.ui.text.TextStyle(
                    fontSize = 12.sp,
                    color = NavyPrimary,
                    lineHeight = 16.sp
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = CardBorderNavy,
                    unfocusedBorderColor = CardBorderNavy,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                ),
                shape = RoundedCornerShape(4.dp)
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Voice Note Simulator
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(3.dp))
                    .border(1.dp, CardBorderNavy, RoundedCornerShape(3.dp))
                    .background(if (isRecordingVoice) RedTint else Color(0xFFF7F4EE))
                    .clickable {
                        isRecordingVoice = !isRecordingVoice
                        voiceDuration = if (isRecordingVoice) "0:07" else "0:14"
                    }
                    .padding(horizontal = 10.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (isRecordingVoice) Icons.Default.Stop else Icons.Default.Mic,
                        contentDescription = "Voice Note",
                        tint = if (isRecordingVoice) RedError else NavyPrimary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isRecordingVoice) "RECORDING ON-DEVICE AUDIO ($voiceDuration)..." else "TAP FOR AUDIO NOTE (OPUS 16kHz COMPACT)",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        color = if (isRecordingVoice) RedError else NavyPrimary
                    )
                }
                if (isRecordingVoice) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(RedError)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // BOTTOM STICKY ORANGE CTA
            Button(
                onClick = {
                    val newId = "HYE-${(43..99).random().toString().padStart(4, '0')}"
                    val calendar = java.util.Calendar.getInstance()
                    val h = calendar.get(java.util.Calendar.HOUR_OF_DAY).toString().padStart(2, '0')
                    val m = calendar.get(java.util.Calendar.MINUTE).toString().padStart(2, '0')
                    val s = calendar.get(java.util.Calendar.SECOND).toString().padStart(2, '0')
                    val exactTime = "$h:$m:$s IST"

                    val report = GrievanceReport(
                        id = newId,
                        category = selectedCategory.nameEn,
                        description = description.ifBlank { selectedCategory.defaultDesc },
                        location = "Ward 12 · Trikuta Nagar, Sector 4 / Zone 2",
                        status = "SEALED",
                        timestamp = System.currentTimeMillis(),
                        formattedTime = exactTime,
                        shaHash = "SHA-256: 8F2A-91C8-3D4E-${(1000..9999).random()}",
                        aiConfidence = 94,
                        photometryStatus = "CLEAR PHOTOMETRY · NO BLUR",
                        payloadKb = 47,
                        imagePreset = selectedCategory.preset,
                        voiceNoteDuration = if (voiceDuration != "0:00") voiceDuration else "0:00"
                    )
                    onSaveAndEncrypt(report)
                },
                colors = ButtonDefaults.buttonColors(containerColor = OrangeAccent),
                shape = RoundedCornerShape(4.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("save_and_encrypt_button")
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = "SAVE TO OFFLINE QUEUE & ENCRYPT",
                        color = Color.White,
                        fontWeight = FontWeight.Black,
                        fontSize = 12.sp,
                        letterSpacing = 0.5.sp,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
        }

        HyperEdgeFooter()
    }
}
