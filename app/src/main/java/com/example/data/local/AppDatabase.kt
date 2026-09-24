package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.model.GrievanceReport
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [GrievanceReport::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun grievanceDao(): GrievanceDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "hyperedge_grievance_db"
                )
                .addCallback(DatabaseCallback(scope))
                .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback(
            private val scope: CoroutineScope
        ) : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateInitialReports(database.grievanceDao())
                    }
                }
            }
        }

        suspend fun populateInitialReports(dao: GrievanceDao) {
            val initialList = listOf(
                GrievanceReport(
                    id = "HYE-0042",
                    category = "Sanitation & Garbage Overflow",
                    description = "Heavy municipal waste accumulation outside Sector 4 primary dump enclosure. Poses severe sanitary block along corridor.",
                    location = "Ward 12 · Trikuta Nagar, Sector 4 / Zone 2 Division",
                    coordinates = "32.7058° N, 74.8732° E",
                    status = "SEALED",
                    timestamp = System.currentTimeMillis() - 1200000,
                    formattedTime = "14:12:08 IST",
                    shaHash = "SHA-256: 8F2A-91C8-3D4E-7B21",
                    aiConfidence = 94,
                    photometryStatus = "CLEAR PHOTOMETRY · NO BLUR",
                    payloadKb = 47,
                    imagePreset = "sanitation",
                    voiceNoteDuration = "0:14"
                ),
                GrievanceReport(
                    id = "HYE-0041",
                    category = "Pothole / Road Gap",
                    description = "Deep structural depression (approx 1.2m wide) near Canal Road bend causing traffic congestion and axle damage risk.",
                    location = "Ward 12 · Trikuta Nagar Canal Road, Sector 3",
                    coordinates = "32.7042° N, 74.8710° E",
                    status = "PENDING",
                    timestamp = System.currentTimeMillis() - 3600000,
                    formattedTime = "13:38:40 IST",
                    shaHash = "SHA-256: 4A7B-89E1-C34F-5D90",
                    aiConfidence = 91,
                    photometryStatus = "CLEAR PHOTOMETRY · NO BLUR",
                    payloadKb = 52,
                    imagePreset = "pothole",
                    voiceNoteDuration = "0:08"
                ),
                GrievanceReport(
                    id = "HYE-0040",
                    category = "Drainage Clog",
                    description = "Monsoon run-off drain choked with construction debris and silt, backing water into lane 6 residential entry.",
                    location = "Ward 14 · Gandhi Nagar Block C, Main Drain Link",
                    coordinates = "32.7115° N, 74.8690° E",
                    status = "PENDING",
                    timestamp = System.currentTimeMillis() - 7200000,
                    formattedTime = "12:45:15 IST",
                    shaHash = "SHA-256: 9F1E-23B4-88AC-011D",
                    aiConfidence = 96,
                    photometryStatus = "CLEAR PHOTOMETRY · NO BLUR",
                    payloadKb = 43,
                    imagePreset = "drainage",
                    voiceNoteDuration = "0:00"
                ),
                GrievanceReport(
                    id = "HYE-0039",
                    category = "Streetlight Outage",
                    description = "Sodium lamp array failed across 3 consecutive poles on inner bypass road creating pedestrian dead zone after dusk.",
                    location = "Ward 8 · Bakshi Nagar, Medical Enclave Lane",
                    coordinates = "32.7290° N, 74.8510° E",
                    status = "SEALED",
                    timestamp = System.currentTimeMillis() - 14400000,
                    formattedTime = "10:15:30 IST",
                    shaHash = "SHA-256: 3D4E-7B21-8F2A-91C8",
                    aiConfidence = 89,
                    photometryStatus = "CLEAR PHOTOMETRY · NO BLUR",
                    payloadKb = 38,
                    imagePreset = "light",
                    voiceNoteDuration = "0:06"
                )
            )
            dao.insertAll(initialList)
        }
    }
}
