package com.uzlov.weatherapp.services

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import androidx.annotation.NonNull
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*

public class LocationService(
    @NonNull val context: Context,
    var locationListener: LocationListener?
) {
    private var UPDATE_INTERVAL_IN_MILLISECONDS: Long = 5000
    private val FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = UPDATE_INTERVAL_IN_MILLISECONDS / 2

    private var mFusedLocationClient: FusedLocationProviderClient
    private var mSettingsClient: SettingsClient
    val mSettings get() = mSettingsClient
    private lateinit var mLocationRequest: LocationRequest
    private lateinit var mLocationCallback: LocationCallback
    var mCurrentLocation: Location? = null
    private var mRequestingLocationUpdates: Boolean = false

    init {
        mRequestingLocationUpdates = false

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        mSettingsClient = LocationServices.getSettingsClient(context)

        createLocationCallback()
        createLocationRequest()
    }

    private fun createLocationRequest() {
        mLocationRequest = LocationRequest().apply {
            interval = UPDATE_INTERVAL_IN_MILLISECONDS
            fastestInterval = FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
    }

    private fun createLocationCallback() {
        mLocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                mCurrentLocation = locationResult.lastLocation
                locationListener?.onLocationChanged(locationResult.lastLocation)
            }
        }
    }

    fun buildLocationSettingsRequest(): LocationSettingsRequest {
        val builder = LocationSettingsRequest.Builder()
        builder.addLocationRequest(mLocationRequest)
        return builder.build()
    }

    fun requestLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // разрешения не получены, выходим
            return
        }

        // делаем запрос
        mFusedLocationClient.requestLocationUpdates(
            mLocationRequest,
            mLocationCallback, Looper.myLooper()
        )
    }

    fun startUpdatesButtonHandler() {
        if (!mRequestingLocationUpdates) {
            mRequestingLocationUpdates = true
        }
    }

    fun stopLocationUpdates() {
        locationListener = null

        // проверка на то, что хоть раз обновления координат были
        if (!mRequestingLocationUpdates) {
            return
        }

        mFusedLocationClient.removeLocationUpdates(mLocationCallback)
            .addOnCompleteListener {
                mRequestingLocationUpdates = false
            }
    }
}