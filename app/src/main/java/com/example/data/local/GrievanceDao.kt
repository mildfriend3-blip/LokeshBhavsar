package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.GrievanceReport
import kotlinx.coroutines.flow.Flow

@Dao
interface GrievanceDao {
    @Query("SELECT * FROM grievance_reports ORDER BY timestamp DESC")
    fun getAllGrievances(): Flow<List<GrievanceReport>>

    @Query("SELECT * FROM grievance_reports WHERE status IN ('PENDING', 'SEALED') ORDER BY timestamp DESC")
    fun getQueuedGrievances(): Flow<List<GrievanceReport>>

    @Query("SELECT * FROM grievance_reports WHERE id = :id LIMIT 1")
    suspend fun getGrievanceById(id: String): GrievanceReport?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGrievance(report: GrievanceReport)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(reports: List<GrievanceReport>)

    @Update
    suspend fun updateGrievance(report: GrievanceReport)

    @Query("UPDATE grievance_reports SET status = :newStatus WHERE id = :id")
    suspend fun updateStatus(id: String, newStatus: String)

    @Query("UPDATE grievance_reports SET status = 'SYNCED' WHERE status IN ('PENDING', 'SEALED', 'SYNCING')")
    suspend fun markAllSynced()

    @Query("DELETE FROM grievance_reports WHERE id = :id")
    suspend fun deleteGrievance(id: String)
}
