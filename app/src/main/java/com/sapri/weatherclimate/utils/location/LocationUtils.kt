package com.sapri.weatherclimate.utils.location

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import com.sapri.weatherclimate.adapter.CityWeatherAdapter
import com.sapri.weatherclimate.data.weatherapi.WeatherApiResponse
import retrofit2.Call


class LocationUtils(ctx: Context, actv: AppCompatActivity) {

    private val context = ctx
    private val appCompatActivity = actv
    val permissionId = 44
    var latitude: String? = null
    var longitude: String? = null
    var fusedLocationClient: FusedLocationProviderClient? = null


    fun isLocationEnabled() : Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    fun requestPermissions() {
        ActivityCompat.requestPermissions(
            appCompatActivity,
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION), permissionId)
    }

    fun checkPermissions(): Boolean {
        if(ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            return true

        }

        return false
    }

    fun newLocationRequest() {

        var locationRequest: LocationRequest = LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 0
        locationRequest.fastestInterval = 0
        locationRequest.numUpdates = 1

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(appCompatActivity)
        fusedLocationClient?.requestLocationUpdates(locationRequest, locationCallBack, Looper.myLooper())

    }

    val locationCallBack = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult?) {
            super.onLocationResult(locationResult)

            val mLastLocation: Location? = locationResult?.lastLocation

            latitude = mLastLocation?.latitude.toString()
            longitude = mLastLocation?.longitude.toString()

        }
    }

}