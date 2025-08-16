package com.example.backgroundtrackingservice

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import android.Manifest
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale


@RequiresApi(Build.VERSION_CODES.Q)
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PermissionAwareContent(modifier: Modifier = Modifier) {
    var permissionGranted by remember { mutableStateOf(false) }

    if (permissionGranted) {
        LocationServiceScreen(modifier = modifier)
    } else {
        RequestLocationPermissionsProperly(
            onAllPermissionsGranted = {
                permissionGranted = true
            },
            onPermissionDenied = {
                permissionGranted = false
            }
        )
    }
}


@RequiresApi(Build.VERSION_CODES.Q)
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RequestLocationPermissionsProperly(
    onAllPermissionsGranted: () -> Unit,
    onPermissionDenied: () -> Unit
) {
    val context = LocalContext.current

    val fineAndCoarseState = rememberMultiplePermissionsState(
        listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    )

    val backgroundState = rememberPermissionState(
        Manifest.permission.ACCESS_BACKGROUND_LOCATION
    )

    var fineRequested by remember { mutableStateOf(false) }
    var backgroundRequested by remember { mutableStateOf(false) }

    // Step 1: Request foreground permissions
    LaunchedEffect(Unit) {
        if (!fineAndCoarseState.allPermissionsGranted && !fineRequested) {
            fineRequested = true
            fineAndCoarseState.launchMultiplePermissionRequest()
        }
    }

    // Step 2: Request background after foreground granted
    LaunchedEffect(fineAndCoarseState.allPermissionsGranted) {
        if (fineAndCoarseState.allPermissionsGranted && !backgroundRequested) {
            backgroundRequested = true
            backgroundState.launchPermissionRequest()
        }
    }

    // Final Check
    LaunchedEffect(backgroundState.status) {
        if (
            fineAndCoarseState.allPermissionsGranted &&
            backgroundState.status.isGranted
        ) {
            onAllPermissionsGranted()
        } else if (
            backgroundRequested &&
            !backgroundState.status.isGranted &&
            !backgroundState.status.shouldShowRationale
        ) {
            // Permanently denied → Open settings
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = Uri.fromParts("package", context.packageName, null)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
            onPermissionDenied()
        }
    }

    // Optional Text UI while permissions are being processed
    Text("Requesting location permissions...")
}




