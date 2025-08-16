package com.example.backgroundtrackingservice

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat

@Composable
fun LocationServiceScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    var isTracking by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = if (isTracking) "Tracking is ON" else "Tracking is OFF",
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            if (hasLocationPermissions(context)) {
                val intent = Intent(context, LocationService::class.java)
                ContextCompat.startForegroundService(context, intent)
                Toast.makeText(context, "Tracking started", Toast.LENGTH_SHORT).show()
                isTracking = true
            } else {
                Toast.makeText(context, "Permission not granted", Toast.LENGTH_SHORT).show()
            }
        }) {
            Text("Start Location Tracking")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val intent = Intent(context, LocationService::class.java)
                context.stopService(intent)
                Toast.makeText(context, "Tracking stopped", Toast.LENGTH_SHORT).show()
                isTracking = false
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
        ) {
            Text("Stop Location Tracking")
        }
    }
}


fun hasLocationPermissions(context: Context): Boolean {
    val fine = ContextCompat.checkSelfPermission(
        context, android.Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

    val background = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        ContextCompat.checkSelfPermission(
            context, android.Manifest.permission.ACCESS_BACKGROUND_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    } else true

    return fine && background
}

