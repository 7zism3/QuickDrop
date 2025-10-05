package com.sevenhallo.quickdrop.ui

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.wifi.aware.WifiAwareManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.sevenhallo.quickdrop.AndroidNearbyDiscovery
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WifiAwareApp() {
    val ctx = LocalContext.current

    var hasPermission by remember { mutableStateOf(false) }
    var supported by remember { mutableStateOf(false) }
    var available by remember { mutableStateOf(false) }

    val discovery = remember { AndroidNearbyDiscovery(ctx) }

    val scope = rememberCoroutineScope()

    // launcher xin quyền runtime
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { result ->
        hasPermission = result.values.all { it }
    }

    // khởi tạo trạng thái khi UI start
    LaunchedEffect(Unit) {
        hasPermission = ensureWifiAwarePermissions(ctx, launcher)
        supported = checkWifiAwareFeature(ctx)
        available = checkWifiAwareAvailable(ctx)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Wi-Fi Aware Checker") },
                navigationIcon = { Icon(Icons.Default.Wifi, contentDescription = null) }
            )
        }
    ) { inner ->
        Column(
            modifier = Modifier
                .padding(inner)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Chips trạng thái
            AssistChip(onClick = {}, label = {
                Text("Permission: ${if (hasPermission) "GRANTED" else "MISSING"}")
            })
            AssistChip(onClick = {}, label = {
                Text("Feature: ${if (supported) "SUPPORTED" else "NOT SUPPORTED"}")
            })
            AssistChip(onClick = {}, label = {
                Text("Available: ${if (available) "YES" else "NO"}")
            })

            Spacer(Modifier.height(16.dp))

            // Nút hành động
            Button(
                onClick = {
                    scope.launch {
                        discovery.startAdvertising(service = "quickdrop")
                    }
                },
                enabled = hasPermission && supported && available,
                modifier = Modifier.fillMaxWidth()
            ) { Text("Start Advertise") }

            Button(
                onClick = {
                    scope.launch {
                        discovery.startScanning("quickdrop")
                    }
                },
                enabled = hasPermission && supported && available,
                modifier = Modifier.fillMaxWidth()
            ) { Text("Start Scan") }

            OutlinedButton(
                onClick = { discovery.stop() },
                enabled = available,
                modifier = Modifier.fillMaxWidth()
            ) { Text("Stop") }
        }
    }
}

/** ---- Helpers (KHÔNG @Composable) ---- */

private fun ensureWifiAwarePermissions(
    context: Context,
    launcher: ActivityResultLauncher<Array<String>>
): Boolean {
    val need = if (Build.VERSION.SDK_INT >= 33) {
        arrayOf(Manifest.permission.NEARBY_WIFI_DEVICES)
    } else {
        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
    }
    val granted = need.all {
        ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
    }
    if (!granted) launcher.launch(need)
    return granted
}

private fun checkWifiAwareFeature(context: Context): Boolean =
    context.packageManager.hasSystemFeature(PackageManager.FEATURE_WIFI_AWARE)

private fun checkWifiAwareAvailable(context: Context): Boolean {
    val mgr = context.getSystemService(Context.WIFI_AWARE_SERVICE) as? WifiAwareManager ?: return false
    return mgr.isAvailable
}
