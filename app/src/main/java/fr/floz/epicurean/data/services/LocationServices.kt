package fr.floz.epicurean.data.services

import android.Manifest.permission
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.SystemClock
import androidx.annotation.RequiresPermission
import androidx.core.content.ContextCompat
import androidx.core.content.getSystemService
import androidx.core.location.LocationManagerCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlin.time.Duration.Companion.days

class LocationServices @Inject constructor(
    @ApplicationContext val context: Context
) {
    private val locationFixTimeMaximum = 1.days // 8.64E13 nanoseconds


    @RequiresPermission(permission.ACCESS_COARSE_LOCATION)
    fun resolveLocation() : Location?  {
        // https://fobo66.dev/post/play-services-location-migration/#how-to-request-current-location-with-locationmanager
        val locationManager = context.getSystemService<LocationManager>() ?: return null
        locationManager.allProviders
        return if (LocationManagerCompat.isLocationEnabled(locationManager)) {
            val location = locationManager.getProviders(true)
                .asSequence()
                .map { locationManager.getLastKnownLocation(it) }
                .filterNotNull()
                .filter {
                    SystemClock.elapsedRealtimeNanos() - it.elapsedRealtimeNanos <= locationFixTimeMaximum.inWholeNanoseconds
                }
                .maxByOrNull { it.elapsedRealtimeNanos }

            location
        } else {
            null
        }
    }

    fun isLocationPermissionAlreadyGranted() : Boolean {
        return ContextCompat.checkSelfPermission(context, permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }
}