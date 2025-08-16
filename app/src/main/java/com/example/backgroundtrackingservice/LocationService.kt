package com.example.backgroundtrackingservice

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority

const val channelId = "location_channel"
const val channelName = "Location Tracking"
class LocationService : Service() {
    private lateinit var fusedClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private lateinit var notificationManager: NotificationManager
    private lateinit var notificationBuilder: NotificationCompat.Builder

    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    override fun onCreate() {
        super.onCreate()
        fusedClient = LocationServices.getFusedLocationProviderClient(this)
        notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        val request = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            5000L
        ).apply {
            setMinUpdateIntervalMillis(2500L)
            setWaitForAccurateLocation(true)
        }.build()

        // Build initial notification
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId, channelName, NotificationManager.IMPORTANCE_LOW
            )
            notificationManager.createNotificationChannel(channel)
        }

        notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setContentTitle("Tracking Location")
            .setContentText("Waiting for location...")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationCompat.PRIORITY_LOW)

        // Start service in foreground with initial notification
        startForeground(1, notificationBuilder.build())

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                val location = result.lastLocation
                if (location != null) {
                    val lat = location.latitude
                    val lng = location.longitude
                    Log.d("BackgroundLocation", "Lat: $lat, Lng: $lng")

                    // Update notification text with new lat/lng
                    notificationBuilder.setContentText("Lat: $lat, Lng: $lng")
                    notificationManager.notify(1, notificationBuilder.build())
                }
            }
        }

        fusedClient.requestLocationUpdates(request, locationCallback, Looper.getMainLooper())
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    // Stop service when app is removed from recent
    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)

        fusedClient.removeLocationUpdates(locationCallback) // stop location updates
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            stopForeground(STOP_FOREGROUND_REMOVE)
        } else {
            @Suppress("DEPRECATION")
            stopForeground(true)
        }
        stopSelf()
    }
    //    as no activity-service interaction require so no set it to null
    //    No component is binding to it (no two-way interaction needed).
    override fun onBind(intent: Intent?): IBinder? = null
}
