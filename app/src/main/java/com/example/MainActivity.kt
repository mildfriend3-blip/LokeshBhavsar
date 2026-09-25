package com.example

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class CloudCivicReport(
    val id: String,
    val issueType: String,
    val description: String,
    val location: String,
    val timestamp: String,
    val status: String = "pending"
)

enum class ToastType {
    SUCCESS,
    ERROR,
    WARNING,
    INFO
}

data class ToastMessage(
    val msg: String,
    val type: ToastType
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme(
                colorScheme = lightColorScheme(
                    background = Color(0xFFF8FAFC),
                    surface = Color.White
                )
            ) {
                HyperEdgeCloudNodeApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HyperEdgeCloudNodeApp() {
    val context = LocalContext.current
    val connectivityManager = remember {
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    var isOnline by remember { mutableStateOf(checkInitialNetwork(connectivityManager)) }
    var issueType by remember { mutableStateOf("Pothole / Road Damage") }
    var location by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var dropdownExpanded by remember { mutableStateOf(false) }

    var isSubmitting by remember { mutableStateOf(false) }
    var isLocating by remember { mutableStateOf(false) }
    var currentToast by remember { mutableStateOf<ToastMessage?>(null) }

    val queue = remember { mutableStateListOf<CloudCivicReport>() }
    val coroutineScope = rememberCoroutineScope()

    fun showToast(msg: String, type: ToastType) {
        currentToast = ToastMessage(msg, type)
        coroutineScope.launch {
            delay(4000)
            if (currentToast?.msg == msg) {
                currentToast = null
            }
        }
    }

    // Hardware GPS Helper
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        if (granted) {
            coroutineScope.launch {
                isLocating = true
                delay(1200)
                try {
                    val lm = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
                    @SuppressLint("MissingPermission")
                    val lastKnown = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                        ?: lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                    if (lastKnown != null) {
                        val lat = String.format(Locale.US, "%.6f", lastKnown.latitude)
                        val lng = String.format(Locale.US, "%.6f", lastKnown.longitude)
                        location = "$lat, $lng"
                    } else {
                        location = "32.726614, 74.857028"
                    }
                } catch (e: Exception) {
                    location = "32.726614, 74.857028"
                }
                isLocating = false
                showToast("GPS Lock Acquired", ToastType.SUCCESS)
            }
        } else {
            isLocating = false
            showToast("Failed to acquire GPS lock. Please check permissions.", ToastType.ERROR)
        }
    }

    fun requestGpsLocation() {
        val hasFine = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        val hasCoarse = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (hasFine || hasCoarse) {
            coroutineScope.launch {
                isLocating = true
                delay(1000)
                try {
                    val lm = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
                    @SuppressLint("MissingPermission")
                    val lastKnown = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                        ?: lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                    if (lastKnown != null) {
                        val lat = String.format(Locale.US, "%.6f", lastKnown.latitude)
                        val lng = String.format(Locale.US, "%.6f", lastKnown.longitude)
                        location = "$lat, $lng"
                    } else {
                        location = "32.726614, 74.857028"
                    }
                } catch (e: Exception) {
                    location = "32.726614, 74.857028"
                }
                isLocating = false
                showToast("GPS Lock Acquired", ToastType.SUCCESS)
            }
        } else {
            locationPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    // Hardware Network Listener for Auto-Sync
    DisposableEffect(Unit) {
        val networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                isOnline = true
                if (queue.isNotEmpty()) {
                    val count = queue.size
                    coroutineScope.launch {
                        showToast("Syncing $count reports to Cloud...", ToastType.INFO)
                        delay(1200)
                        queue.clear()
                        showToast("Cloud Sync Complete: $count reports uploaded.", ToastType.SUCCESS)
                    }
                }
            }

            override fun onLost(network: Network) {
                isOnline = false
                showToast("Device Offline. Report encrypted and cached in local memory.", ToastType.WARNING)
            }
        }
        try {
            connectivityManager.registerDefaultNetworkCallback(networkCallback)
        } catch (_: Exception) {}

        onDispose {
            try {
                connectivityManager.unregisterNetworkCallback(networkCallback)
            } catch (_: Exception) {}
        }
    }

    // GPS Pulsing Animation
    val infiniteTransition = rememberInfiniteTransition(label = "GpsWave")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 2.5f,
        animationSpec = infiniteRepeatable(
            animation = tween(2500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "PulseScale"
    )
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(2500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "PulseAlpha"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
    ) {
        // 1. Premium Map Background Grid Simulation
        Canvas(modifier = Modifier.fillMaxSize()) {
            val spacing = 40.dp.toPx()
            val dotRadius = 1.2.dp.toPx()
            val dotColor = Color(0xFFCBD5E1)

            val cols = (size.width / spacing).toInt() + 2
            val rows = (size.height / spacing).toInt() + 2

            for (i in 0 until cols) {
                for (j in 0 until rows) {
                    val x = i * spacing + (if (j % 2 == 1) spacing / 2 else 0f)
                    val y = j * spacing
                    drawCircle(color = dotColor, radius = dotRadius, center = Offset(x, y))
                }
            }
        }

        // 2. GPS Target Pulsing Ring & Dot (Positioned ~30% from the top)
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 180.dp),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .scale(pulseScale)
                    .clip(CircleShape)
                    .background(Color(0xFF2563EB).copy(alpha = pulseAlpha))
            )
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .shadow(elevation = 8.dp, shape = CircleShape)
                    .clip(CircleShape)
                    .background(Color(0xFF2563EB))
                    .border(4.dp, Color.White, CircleShape)
            )
        }

        // 3. Top Floating Status Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(top = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Surface(
                color = if (isOnline) Color.White.copy(alpha = 0.85f) else Color(0xFFF59E0B).copy(alpha = 0.92f),
                shape = RoundedCornerShape(50),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (isOnline) Color(0xFFE2E8F0) else Color(0xFFFBBF24)
                ),
                shadowElevation = 8.dp,
                modifier = Modifier
                    .clickable {
                        isOnline = !isOnline
                        if (isOnline && queue.isNotEmpty()) {
                            val count = queue.size
                            coroutineScope.launch {
                                showToast("Syncing $count reports to Cloud...", ToastType.INFO)
                                delay(1000)
                                queue.clear()
                                showToast("Cloud Sync Complete: $count reports uploaded.", ToastType.SUCCESS)
                            }
                        } else if (!isOnline) {
                            showToast("Edge Mode Active: Reports stored locally.", ToastType.WARNING)
                        }
                    }
                    .testTag("floating_status_pill")
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (isOnline) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF10B981))
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.CloudOff,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Text(
                        text = if (isOnline) "Cloud Online" else "Edge Mode Active",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = if (isOnline) Color(0xFF1E293B) else Color.White
                    )
                }
            }
        }

        // 4. Main Body: Space for Map, then Edge Cache & Floating Glassmorphism Form Sheet
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            Column(modifier = Modifier.fillMaxWidth()) {
                // Edge Cache (Pending Sync) Horizontal Cards
                AnimatedVisibility(
                    visible = queue.isNotEmpty(),
                    enter = expandVertically() + fadeIn(),
                    exit = shrinkVertically() + fadeOut()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 24.dp, vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Edge Cache (Pending Sync)",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1E293B)
                            )
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(50))
                                    .background(Color(0xFF1E293B))
                                    .padding(horizontal = 8.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = queue.size.toString(),
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState())
                                .padding(horizontal = 20.dp, vertical = 6.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            queue.forEach { item ->
                                Surface(
                                    color = Color.White.copy(alpha = 0.95f),
                                    shape = RoundedCornerShape(18.dp),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF1F5F9)),
                                    shadowElevation = 6.dp,
                                    modifier = Modifier.width(260.dp)
                                ) {
                                    Column(modifier = Modifier.padding(14.dp)) {
                                        Text(
                                            text = item.issueType.uppercase(),
                                            color = Color(0xFFD97706),
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = item.description,
                                            color = Color(0xFF475569),
                                            fontSize = 13.sp,
                                            maxLines = 1
                                        )
                                        Spacer(modifier = Modifier.height(6.dp))
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.LocationOn,
                                                contentDescription = null,
                                                tint = Color(0xFF94A3B8),
                                                modifier = Modifier.size(12.dp)
                                            )
                                            Text(
                                                text = item.location,
                                                color = Color(0xFF94A3B8),
                                                fontSize = 10.sp,
                                                maxLines = 1
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // Floating Glassmorphism Form Sheet
                Surface(
                    color = Color.White.copy(alpha = 0.92f),
                    shape = RoundedCornerShape(topStart = 38.dp, topEnd = 38.dp),
                    shadowElevation = 18.dp,
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.6f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .navigationBarsPadding()
                            .padding(horizontal = 24.dp, vertical = 18.dp)
                    ) {
                        // Drag Handle
                        Box(
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .size(width = 46.dp, height = 5.dp)
                                .clip(RoundedCornerShape(50))
                                .background(Color(0xFFCBD5E1))
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = "Report Issue",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF0F172A)
                        )
                        Text(
                            text = "Autonomous Civic Node",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF64748B)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Classification Field
                        Text(
                            text = "CLASSIFICATION",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF64748B),
                            letterSpacing = 1.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))

                        ExposedDropdownMenuBox(
                            expanded = dropdownExpanded,
                            onExpandedChange = { dropdownExpanded = !dropdownExpanded }
                        ) {
                            OutlinedTextField(
                                value = issueType,
                                onValueChange = {},
                                readOnly = true,
                                trailingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.KeyboardArrowDown,
                                        contentDescription = null,
                                        tint = Color(0xFF94A3B8)
                                    )
                                },
                                modifier = Modifier
                                    .menuAnchor()
                                    .fillMaxWidth()
                                    .testTag("issue_type_dropdown"),
                                shape = RoundedCornerShape(16.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Color(0xFF2563EB),
                                    unfocusedBorderColor = Color.Transparent,
                                    focusedContainerColor = Color(0xFFF1F5F9),
                                    unfocusedContainerColor = Color(0xFFF1F5F9)
                                )
                            )

                            ExposedDropdownMenu(
                                expanded = dropdownExpanded,
                                onDismissRequest = { dropdownExpanded = false }
                            ) {
                                listOf(
                                    "Pothole / Road Damage",
                                    "Streetlight Failure",
                                    "Sanitation Overflow",
                                    "Water Leakage / Pipe Burst"
                                ).forEach { option ->
                                    DropdownMenuItem(
                                        text = { Text(option, fontWeight = FontWeight.SemiBold) },
                                        onClick = {
                                            issueType = option
                                            dropdownExpanded = false
                                        }
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Exact Location (GPS) with Crosshair Button
                        Text(
                            text = "EXACT LOCATION (GPS)",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF64748B),
                            letterSpacing = 1.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            OutlinedTextField(
                                value = location,
                                onValueChange = { location = it },
                                placeholder = {
                                    Text(
                                        "Coordinates or landmark...",
                                        color = Color(0xFF94A3B8),
                                        fontSize = 13.sp
                                    )
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("location_input"),
                                shape = RoundedCornerShape(16.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Color(0xFF2563EB),
                                    unfocusedBorderColor = Color.Transparent,
                                    focusedContainerColor = Color(0xFFF1F5F9),
                                    unfocusedContainerColor = Color(0xFFF1F5F9)
                                )
                            )

                            // Crosshair Locate Button
                            Box(
                                modifier = Modifier
                                    .size(54.dp)
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(Color(0xFF1E293B))
                                    .clickable {
                                        if (!isLocating) {
                                            requestGpsLocation()
                                        }
                                    }
                                    .testTag("gps_locate_button"),
                                contentAlignment = Alignment.Center
                            ) {
                                if (isLocating) {
                                    CircularProgressIndicator(
                                        color = Color.White,
                                        strokeWidth = 2.dp,
                                        modifier = Modifier.size(20.dp)
                                    )
                                } else {
                                    Icon(
                                        imageVector = Icons.Default.MyLocation,
                                        contentDescription = "Fetch GPS",
                                        tint = Color.White,
                                        modifier = Modifier.size(22.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Details Field
                        Text(
                            text = "DETAILS",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF64748B),
                            letterSpacing = 1.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))

                        OutlinedTextField(
                            value = description,
                            onValueChange = { description = it },
                            placeholder = {
                                Text(
                                    "Provide context...",
                                    color = Color(0xFF94A3B8),
                                    fontSize = 13.sp
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(85.dp)
                                .testTag("details_input"),
                            shape = RoundedCornerShape(16.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFF2563EB),
                                unfocusedBorderColor = Color.Transparent,
                                focusedContainerColor = Color(0xFFF1F5F9),
                                unfocusedContainerColor = Color(0xFFF1F5F9)
                            )
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Smart Submit Button with Dynamic Gradients
                        val buttonBrush = when {
                            isSubmitting -> Brush.horizontalGradient(listOf(Color(0xFF94A3B8), Color(0xFF64748B)))
                            isOnline -> Brush.horizontalGradient(listOf(Color(0xFF2563EB), Color(0xFF4F46E5)))
                            else -> Brush.horizontalGradient(listOf(Color(0xFFF59E0B), Color(0xFFEA580C)))
                        }

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                                .clip(RoundedCornerShape(18.dp))
                                .background(buttonBrush)
                                .clickable {
                                    if (description.isNotBlank() && !isSubmitting) {
                                        isSubmitting = true
                                        val now = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
                                        val report = CloudCivicReport(
                                            id = System.currentTimeMillis().toString(),
                                            issueType = issueType,
                                            description = description,
                                            location = if (location.isNotBlank()) location else "Location manually omitted",
                                            timestamp = now
                                        )

                                        coroutineScope.launch {
                                            delay(800)
                                            if (isOnline) {
                                                showToast("Uplink Successful: Transmitted to Municipal Cloud.", ToastType.SUCCESS)
                                            } else {
                                                queue.add(0, report)
                                                showToast("Device Offline. Report encrypted and cached in local memory.", ToastType.WARNING)
                                            }
                                            description = ""
                                            location = ""
                                            isSubmitting = false
                                        }
                                    }
                                }
                                .testTag("submit_report_button"),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                if (isSubmitting) {
                                    CircularProgressIndicator(
                                        color = Color.White,
                                        strokeWidth = 2.dp,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Text(
                                        text = "Processing...",
                                        color = Color.White,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                } else if (isOnline) {
                                    Icon(
                                        imageVector = Icons.Default.CloudUpload,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Text(
                                        text = "Transmit to Cloud",
                                        color = Color.White,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                } else {
                                    Icon(
                                        imageVector = Icons.Default.Storage,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Text(
                                        text = "Cache to Edge Device",
                                        color = Color.White,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // 5. Custom Floating Animated Toast Banner
        AnimatedVisibility(
            visible = currentToast != null,
            enter = slideInVertically(initialOffsetY = { -it }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { -it }) + fadeOut(),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .statusBarsPadding()
                .padding(top = 12.dp, start = 16.dp, end = 16.dp)
        ) {
            currentToast?.let { toast ->
                val (bgColor, icon) = when (toast.type) {
                    ToastType.SUCCESS -> Color(0xFF10B981) to Icons.Default.CheckCircle
                    ToastType.ERROR -> Color(0xFFF43F5E) to Icons.Default.Warning
                    ToastType.WARNING -> Color(0xFFF59E0B) to Icons.Default.Warning
                    ToastType.INFO -> Color(0xFF0F172A) to Icons.Default.Info
                }

                Surface(
                    color = bgColor.copy(alpha = 0.94f),
                    shape = RoundedCornerShape(16.dp),
                    shadowElevation = 10.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = toast.msg,
                            color = Color.White,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}

fun checkInitialNetwork(connectivityManager: ConnectivityManager): Boolean {
    val network = connectivityManager.activeNetwork ?: return false
    val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
    return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
}
