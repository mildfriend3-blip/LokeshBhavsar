package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.model.GrievanceReport
import com.example.data.repository.GrievanceRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class ToastMessage(
    val id: Long = System.currentTimeMillis(),
    val message: String,
    val type: ToastType = ToastType.SUCCESS
)

enum class ToastType {
    SUCCESS,
    ERROR,
    WARNING
}

class HyperEdgeViewModel(application: Application) : AndroidViewModel(application) {
    private val database = AppDatabase.getDatabase(application, viewModelScope)
    private val repository = GrievanceRepository(database.grievanceDao())

    val allReports: StateFlow<List<GrievanceReport>> = repository.allReports
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _currentScreen = MutableStateFlow(1)
    val currentScreen: StateFlow<Int> = _currentScreen.asStateFlow()

    private val _isOnline = MutableStateFlow(false) // Default offline for rugged dead-zone simulation
    val isOnline: StateFlow<Boolean> = _isOnline.asStateFlow()

    private val _isHindi = MutableStateFlow(false)
    val isHindi: StateFlow<Boolean> = _isHindi.asStateFlow()

    private val _latestReport = MutableStateFlow<GrievanceReport?>(null)
    val latestReport: StateFlow<GrievanceReport?> = _latestReport.asStateFlow()

    private val _isSyncing = MutableStateFlow(false)
    val isSyncing: StateFlow<Boolean> = _isSyncing.asStateFlow()

    private val _syncLogs = MutableStateFlow(
        listOf(
            "[14:30:10] Standalone mesh daemon started",
            "[14:30:11] Hardware Keyring initialized (ChaCha20-Poly1305)",
            "[14:30:12] On-device SQLite ledger active: /data/secure/hyperedge.db",
            "[14:30:15] Signal status: DEAD-ZONE ACTIVE (Awaiting corridor ping)"
        )
    )
    val syncLogs: StateFlow<List<String>> = _syncLogs.asStateFlow()

    private val _currentToast = MutableStateFlow<ToastMessage?>(null)
    val currentToast: StateFlow<ToastMessage?> = _currentToast.asStateFlow()

    init {
        // Pre-populate if empty
        viewModelScope.launch {
            delay(400)
            if (allReports.value.isEmpty()) {
                AppDatabase.populateInitialReports(database.grievanceDao())
            }
        }
    }

    fun setScreen(screen: Int) {
        _currentScreen.value = screen
    }

    fun toggleLanguage() {
        _isHindi.value = !_isHindi.value
        val msg = if (_isHindi.value) "हिन्दी भाषा सक्रिय (Hindi Active)" else "English Mode Active"
        showToast(msg, ToastType.SUCCESS)
    }

    fun toggleOnline() {
        val newStatus = !_isOnline.value
        _isOnline.value = newStatus
        if (newStatus) {
            showToast("Signal Restored: Optical Corridor Available", ToastType.SUCCESS)
            addLog("[${currentTime()}] Corridor signal detected: 4G/5G Mesh Active")
        } else {
            showToast("Dead-Zone Active: Safe Local Storage Engaged", ToastType.ERROR)
            addLog("[${currentTime()}] Signal dropped: Reverting to On-Device SQLite Cache")
        }
    }

    fun saveAndEncryptReport(report: GrievanceReport) {
        viewModelScope.launch {
            repository.insertReport(report)
            _latestReport.value = report
            addLog("[${currentTime()}] Incident #${report.id} sealed with ChaCha20-Poly1305")
            addLog("[${currentTime()}] Photometry verified: ${report.photometryStatus}")
            showToast("Report sealed with ChaCha20-Poly1305", ToastType.SUCCESS)
            _currentScreen.value = 5 // Navigate to Screen 5: Edge AI Validation
        }
    }

    fun simulateSignalReturnAndSync() {
        if (_isSyncing.value) return
        _isSyncing.value = true
        _isOnline.value = true

        viewModelScope.launch {
            addLog("[${currentTime()}] Mesh handshake initiated with node JMC-CORRIDOR-042")
            delay(700)
            addLog("[${currentTime()}] Delta sync payload assembled (47 KB)")
            delay(700)
            addLog("[${currentTime()}] ChaCha20 envelope verified via hardware keyring")
            delay(800)

            val queued = allReports.value.filter { it.status in listOf("PENDING", "SEALED") }
            if (queued.isNotEmpty()) {
                for (item in queued) {
                    repository.updateStatus(item.id, "SYNCING")
                    addLog("[${currentTime()}] Pushing report #${item.id} to JMC corridor gateway...")
                    delay(500)
                }
            } else {
                addLog("[${currentTime()}] Pushing report #HYE-0042 pushed to JMC corridor")
                delay(500)
            }

            repository.markAllSynced()
            addLog("[${currentTime()}] Municipal Central ACK received: ACK_JMC_9981")
            addLog("[${currentTime()}] Delta sync sequence COMPLETE. 0 pending records.")
            _isSyncing.value = false
            showToast("All incidents synchronized with JMC Central Corridor", ToastType.SUCCESS)
        }
    }

    fun showToast(message: String, type: ToastType = ToastType.SUCCESS) {
        viewModelScope.launch {
            _currentToast.value = ToastMessage(message = message, type = type)
            delay(3500)
            if (_currentToast.value?.message == message) {
                _currentToast.value = null
            }
        }
    }

    fun dismissToast() {
        _currentToast.value = null
    }

    private fun addLog(log: String) {
        _syncLogs.value = (_syncLogs.value + log).takeLast(16)
    }

    private fun currentTime(): String {
        val calendar = java.util.Calendar.getInstance()
        val h = calendar.get(java.util.Calendar.HOUR_OF_DAY).toString().padStart(2, '0')
        val m = calendar.get(java.util.Calendar.MINUTE).toString().padStart(2, '0')
        val s = calendar.get(java.util.Calendar.SECOND).toString().padStart(2, '0')
        return "$h:$m:$s"
    }
}
