package com.allosh.martyes.location

import android.app.Activity
import android.content.Intent
import android.content.IntentSender.SendIntentException
import android.provider.Settings
import com.allosh.martyes.R
import com.allosh.martyes.util.UIUtil
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task


/** Utility class to ask for location services  */
internal class LocationServicesManager(var activity: Activity) {
    var locationBuilder: LocationSettingsRequest.Builder

    fun askForLocationServices(requestCode: Int) {
        val result = LocationServices
            .getSettingsClient(activity)
            .checkLocationSettings(locationBuilder.build())
        result.addOnCompleteListener { task: Task<LocationSettingsResponse?> ->
            try {
                task.getResult(ApiException::class.java)
            } catch (exception: ApiException) {
                when (exception.statusCode) {
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> try {
                        val resolvable = exception as ResolvableApiException
                        // Show dialog to turn on location services
                        resolvable.startResolutionForResult(activity, requestCode)
                    } catch (ignored: SendIntentException) {
                    } catch (ignored: ClassCastException) {
                    }
                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> openLocationSettings()
                }
            }
        }
    }

    // Get location services the old fashioned way
    private fun openLocationSettings() {
        UIUtil.showLongToast(R.string.turn_on_location_services, activity)
        activity.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
    }

    init {
        val locationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationBuilder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
        locationBuilder.setAlwaysShow(true)
    }
}