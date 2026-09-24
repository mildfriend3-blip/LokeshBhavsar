package com.example.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.model.GrievanceReport

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OfflineVaultScreen(
    reports: List<GrievanceReport> = emptyList(),
    isOnline: Boolean = true,
    isSyncing: Boolean = false,
    onSyncNow: () -> Unit = {}
) {
    val pendingCount = if (reports.isNotEmpty()) {
        reports.count { it.status in listOf("PENDING", "SEALED", "SYNCING") }
    } else 2

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Sync Vault", fontWeight = FontWeight.Bold) }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            // Row containing Text ("2 Pending Uploads") and a Button ("Sync Now")
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "$pendingCount Pending Uploads",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )

                Button(
                    onClick = onSyncNow,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.testTag("sync_now_button")
                ) {
                    Text(if (isSyncing) "Syncing..." else "Sync Now")
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // LinearProgressIndicator (set to 0.5f progress) filling the width
            LinearProgressIndicator(
                progress = { 0.5f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .testTag("sync_progress_bar")
            )

            Spacer(modifier = Modifier.height(16.dp))

            // LazyColumn containing cards
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (reports.isNotEmpty()) {
                    items(reports, key = { it.id }) { report ->
                        TicketCard(
                            title = "Ticket #${report.id}",
                            subtitle = "Category: ${report.category} | Size: ${report.payloadKb / 1024f}MB"
                        )
                    }
                } else {
                    item {
                        TicketCard(
                            title = "Ticket #HYE-0042",
                            subtitle = "Category: Sadak | Size: 2.4MB"
                        )
                    }
                    item {
                        TicketCard(
                            title = "Ticket #HYE-0043",
                            subtitle = "Category: Kachra | Size: 1.8MB"
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TicketCard(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Text(
                    text = "Encrypted locally",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
