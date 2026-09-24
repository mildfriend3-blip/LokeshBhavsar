package com.example.data.repository

import com.example.data.local.GrievanceDao
import com.example.data.model.GrievanceReport
import kotlinx.coroutines.flow.Flow

class GrievanceRepository(private val grievanceDao: GrievanceDao) {
    val allReports: Flow<List<GrievanceReport>> = grievanceDao.getAllGrievances()
    val queuedReports: Flow<List<GrievanceReport>> = grievanceDao.getQueuedGrievances()

    suspend fun getReportById(id: String): GrievanceReport? = grievanceDao.getGrievanceById(id)

    suspend fun insertReport(report: GrievanceReport) = grievanceDao.insertGrievance(report)

    suspend fun updateStatus(id: String, status: String) = grievanceDao.updateStatus(id, status)

    suspend fun markAllSynced() = grievanceDao.markAllSynced()

    suspend fun deleteReport(id: String) = grievanceDao.deleteGrievance(id)
}
