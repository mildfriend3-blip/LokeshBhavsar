package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "grievance_reports")
data class GrievanceReport(
    @PrimaryKey val id: String,
    val category: String,
    val description: String,
    val location: String,
    val coordinates: String = "32.7058° N, 74.8732° E",
    val status: String, // "PENDING", "SEALED", "SYNCING", "SYNCED"
    val timestamp: Long = System.currentTimeMillis(),
    val formattedTime: String,
    val shaHash: String,
    val aiConfidence: Int = 94,
    val photometryStatus: String = "CLEAR PHOTOMETRY · NO BLUR",
    val cryptoKey: String = "ChaCha20-Poly1305 · Hardware Keyring Sealed",
    val payloadKb: Int = 47,
    val imagePreset: String = "sanitation",
    val voiceNoteDuration: String = "0:00"
)
