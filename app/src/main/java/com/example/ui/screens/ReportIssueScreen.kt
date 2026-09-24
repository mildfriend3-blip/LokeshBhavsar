package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.GrievanceReport

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportIssueScreen(
    onBackClick: () -> Unit = {},
    onSaveToVault: (GrievanceReport) -> Unit = {}
) {
    var selectedCategory by remember { mutableStateOf("Sadak") }
    val categories = listOf("Sadak", "Kachra", "Bijli")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Report Issue") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            // 1. A large Box (height 300dp, black background) representing a Camera Preview.
            // Inside this Box, at the bottom, place a semi-transparent dark row containing text: "Edge AI Active: Pothole Detected (94%)".
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.Black)
                    .testTag("camera_preview_box")
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .background(Color.Black.copy(alpha = 0.65f))
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Edge AI Active: Pothole Detected (94%)",
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 2. A ScrollableRow with 3 FilterChips: "Sadak", "Kachra", "Bijli".
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                categories.forEach { category ->
                    FilterChip(
                        selected = selectedCategory == category,
                        onClick = { selectedCategory = category },
                        label = { Text(category) },
                        modifier = Modifier.testTag("chip_$category")
                    )
                }
            }

            // 3. A Spacer to push content up.
            Spacer(modifier = Modifier.weight(1f))

            // 4. A large, prominent Button at the bottom (fillMaxWidth) with text: "Save to Offline Vault (ChaCha20)".
            Button(
                onClick = {
                    val report = GrievanceReport(
                        id = "HYE-00${(40..99).random()}",
                        category = selectedCategory,
                        description = "Hazard reported in Ward 12",
                        location = "Canal Rd, Ward 12, Jammu",
                        coordinates = "32.7058° N, 74.8732° E",
                        status = "PENDING",
                        formattedTime = "Just now",
                        shaHash = "SHA-256: 8F2A-91C8",
                        aiConfidence = 94,
                        imagePreset = selectedCategory.lowercase()
                    )
                    onSaveToVault(report)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("save_vault_button"),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFE65100),
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = "Save to Offline Vault (ChaCha20)",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            }
        }
    }
}
