package com.allosh.martyes.location

import android.Manifest
import android.app.Activity
import android.location.Location
import android.os.Handler
import androidx.fragment.app.Fragment
import com.afollestad.materialdialogs.MaterialDialog
import com.allosh.martyes.R
import com.allosh.martyes.util.PermissionUtil
import com.allosh.martyes.util.UIUtil
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.SettingsClient


class MyLocationManager(private val listener: Listener, private var activity: Activity, fragment: Fragment) {

    private lateinit var fragment: Fragment
    private lateinit var locationServicesManager: LocationServicesManager
    private lateinit var locationDenialDialog: MaterialDialog
    private lateinit var locationFetcher: FusedLocationProviderClient
    private lateinit var locationPermissionDialog: MaterialDialog

    private val locationChecker = Handler()
    private var locationRequest: LocationRequest? = null
    private var locationFetched = false
    private val locationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult?) {
            if (locationResult != null) {
                stopFetchingCurrentLocation()
                locationFetched = true
                val location: Location = locationResult.getLastLocation()
                listener.onLocationFound(location.latitude, location.longitude)
            }
        }
    }
    private val locationCheckTask = Runnable {
        stopFetchingCurrentLocation()
        if (!locationFetched) {
            onLocationFetchFail()
        }
    }

    private fun initNonContext() {
        locationServicesManager = LocationServicesManager(activity)
        locationDenialDialog = MaterialDialog.Builder(activity)
            .cancelable(false)
            .title(R.string.location_services_needed)
            .content(R.string.location_services_denial)
            .positiveText(R.string.location_services_confirm)
            .negativeText(R.string.cancel)
            .onPositive { dialog, which ->
                locationServicesManager.askForLocationServices(LOCATION_SERVICES_CODE)
                listener.onServicesOrPermissionChoice()
            }
            .onNegative { dialog, which ->
                onLocationFetchFail()
                listener.onServicesOrPermissionChoice()
            }
            .build()
        locationPermissionDialog = MaterialDialog.Builder(activity)
            .cancelable(false)
            .title(R.string.location_permission_needed)
            .content(R.string.location_permission_denial)
            .positiveText(R.string.give_location_permission)
            .negativeText(R.string.cancel)
            .onPositive { dialog, which ->
                requestLocationPermission()
                listener.onServicesOrPermissionChoice()
            }
            .onNegative { dialog, which ->
                onLocationFetchFail()
                listener.onServicesOrPermissionChoice()
            }
            .build()
        locationFetcher = LocationServices.getFusedLocationProviderClient(activity)
        locationRequest = LocationRequest.create()
            .setInterval(DESIRED_LOCATION_TURNAROUND)
            .setFastestInterval(DESIRED_LOCATION_TURNAROUND)
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
    }

    fun fetchCurrentLocation() {
        if (PermissionUtil.isPermissionGranted(
                Manifest.permission.ACCESS_FINE_LOCATION,
                activity
            )
        ) {
            checkLocationServicesAndFetchLocationIfOn()
        } else {
            requestLocationPermission()
        }
    }

    private fun checkLocationServicesAndFetchLocationIfOn() {
        val builder: LocationSettingsRequest.Builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
        val client: SettingsClient = LocationServices.getSettingsClient(activity)
        client.checkLocationSettings(builder.build())
            .addOnSuccessListener { locationSettingsResponse -> fetchAutomaticLocation() }
            .addOnFailureListener { exception ->
                if (exception is ResolvableApiException) {
                    locationServicesManager.askForLocationServices(LOCATION_SERVICES_CODE)
                } else {
                    onLocationFetchFail()
                }
            }
    }

    private fun requestLocationPermission() {
        if (this::fragment.isInitialized) {
            PermissionUtil.requestPermission(
                activity,
                Manifest.permission.ACCESS_FINE_LOCATION,
                LOCATION_PERMISSION_REQUEST_CODE
            )
        } else {
            PermissionUtil.requestPermissionOnFragment(
                fragment,
                Manifest.permission.ACCESS_FINE_LOCATION,
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }

    fun fetchAutomaticLocation() {
//        UIUtil.showLongToast(R.string.location_services_on, activity);
        locationFetched = false
        try {
            locationChecker.postDelayed(locationCheckTask, MILLISECONDS_BEFORE_FAILURE)
            locationFetcher.requestLocationUpdates(locationRequest, locationCallback, null)
        } catch (exception: SecurityException) {
            requestLocationPermission()
        }
    }

    private fun stopFetchingCurrentLocation() {
        locationChecker.removeCallbacks(locationCheckTask)
        locationFetcher.removeLocationUpdates(locationCallback)
    }

    fun showLocationDenialDialog() {
        locationDenialDialog.show()
    }

    fun showLocationPermissionDialog() {
        locationPermissionDialog.show()
    }

    private fun onLocationFetchFail() {
        UIUtil.showLongToast(R.string.auto_location_fail, activity)
    }

    interface Listener {
        fun onServicesOrPermissionChoice()
        fun onLocationFound(latitude: Double, longitude: Double)
    }

    companion object {
        // NOTE: If an activity uses this class, IT CANNOT USE MATCHING CODES
        const val LOCATION_SERVICES_CODE = 350
        const val LOCATION_PERMISSION_REQUEST_CODE = 9001
        private const val DESIRED_LOCATION_TURNAROUND = 1000L
        private const val MILLISECONDS_BEFORE_FAILURE = 10000L
    }

    init {
        this.fragment = fragment
        initNonContext()
    }
}