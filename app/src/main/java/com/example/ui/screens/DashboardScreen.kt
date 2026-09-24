package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Badge
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    isOnline: Boolean = true,
    onToggleOnline: () -> Unit = {},
    onReportIssueClick: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "HyperEdge",
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.padding(end = 12.dp)
                    ) {
                        AssistChip(
                            onClick = onToggleOnline,
                            label = {
                                Text(
                                    text = if (isOnline) "ONLINE" else "OFFLINE",
                                    color = if (isOnline) Color(0xFF1B5E20) else Color(0xFFB71C1C),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp
                                )
                            },
                            colors = AssistChipDefaults.assistChipColors(
                                containerColor = if (isOnline) Color(0xFFE8F5E9) else Color(0xFFFFEBEE)
                            ),
                            border = AssistChipDefaults.assistChipBorder(
                                enabled = true,
                                borderColor = if (isOnline) Color(0xFF81C784) else Color(0xFFE57373)
                            ),
                            modifier = Modifier.testTag("signal_toggle_chip")
                        )

                        Badge(
                            containerColor = Color(0xFFE65100),
                            contentColor = Color.White
                        ) {
                            Text(
                                text = "2/8 Sync",
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onReportIssueClick,
                icon = { Icon(Icons.Default.Add, contentDescription = null) },
                text = { Text("Report Issue", fontWeight = FontWeight.Bold) },
                containerColor = Color(0xFFE65100),
                contentColor = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .testTag("report_issue_fab")
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.70f)
                    .background(Color(0xFFE0E0E0))
                    .testTag("map_placeholder_box"),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Place,
                    contentDescription = "Map Placeholder",
                    tint = Color.DarkGray,
                    modifier = Modifier.size(56.dp)
                )
            }
        }
    }
}
