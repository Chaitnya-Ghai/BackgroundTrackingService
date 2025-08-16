package com.example.backgroundtrackingservice

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.*
import com.example.backgroundtrackingservice.ui.theme.BackgroundTrackingServiceTheme

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BackgroundTrackingServiceTheme {
                Scaffold { innerPadding ->
                    PermissionAwareContent(Modifier.padding(innerPadding))
                }
            }
        }
    }
}

