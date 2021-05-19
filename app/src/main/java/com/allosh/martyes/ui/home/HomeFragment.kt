package com.allosh.martyes.ui.home

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.allosh.martyes.databinding.FragmentHomeBinding
import com.allosh.martyes.location.MyLocationManager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import android.util.Log


import android.content.res.Resources.NotFoundException
import com.allosh.martyes.R

import com.google.android.gms.maps.model.MapStyleOptions




class HomeFragment : Fragment(), MyLocationManager.Listener, OnMapReadyCallback {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var googleMap: GoogleMap
    private lateinit var myLocationManager: MyLocationManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        myLocationManager = MyLocationManager(this, requireActivity(), this)
        myLocationManager.fetchCurrentLocation()

        binding.mapView.onCreate(savedInstanceState)
        binding.mapView.getMapAsync(this)

        return binding.root
    }


    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap
        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            val success: Boolean = googleMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                    requireActivity(), R.raw.map_style
                )
            )
            if (!success) {
                Log.e("TAG", "Style parsing failed.")
            }
        } catch (e: NotFoundException) {
            Log.e("TAG", "Can't find style.", e)
        }
    }

    private fun zoomToLocation(latLng: LatLng) {
        googleMap.clear()
        googleMap.addMarker(
            MarkerOptions().position(latLng)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
        )
        val cameraPosition = CameraPosition.Builder()
            .target(latLng)
            .zoom(16f)
            .bearing(0f)
            .tilt(0f)
            .build()
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }

    override fun onServicesOrPermissionChoice() {
    }

    override fun onLocationFound(latitude: Double, longitude: Double) {
        zoomToLocation(LatLng(latitude, longitude))
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        if (requestCode != MyLocationManager.LOCATION_PERMISSION_REQUEST_CODE) {
            return
        }

        // No need to check if the location permission has been granted because of the onResume() block
        if (!(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
            myLocationManager.showLocationPermissionDialog()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == MyLocationManager.LOCATION_SERVICES_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                myLocationManager.fetchAutomaticLocation()
            } else {
                myLocationManager.showLocationDenialDialog()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
    }

    override fun onStart() {
        super.onStart()
        binding.mapView.onStart()
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.onPause()
    }

    override fun onStop() {
        super.onStop()
        binding.mapView.onStop()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.mapView.onDestroy()
    }
}