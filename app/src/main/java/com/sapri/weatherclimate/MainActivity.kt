package com.sapri.weatherclimate

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.view.View
import android.widget.AdapterView
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import com.sapri.weatherclimate.adapter.CityWeatherAdapter
import com.sapri.weatherclimate.data.weatherapi.CityWeather
import com.sapri.weatherclimate.data.weatherapi.LatitudeLongitude
import com.sapri.weatherclimate.data.weatherapi.WeatherApiResponse
import com.sapri.weatherclimate.utils.http.HttpRequestClient
import com.sapri.weatherclimate.utils.http.weatherapi.WeatherService
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.roundToInt


class MainActivity : AppCompatActivity() {

    private val permissionId = 44
    var latitude: String? = null
    var longitude: String? = null
    var mFusedLocationClient: FusedLocationProviderClient? = null
    private var call: Call<WeatherApiResponse>? = null
    var cityWeatherAdapter: CityWeatherAdapter? = null
    var citiesLatLongList: ArrayList<String>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        cityWeatherAdapter = CityWeatherAdapter(this)

        getLastLocation(cityWeatherAdapter)

        call = callApi(
            latitude,
            longitude,
            "50",
            "metric",
            "pt_br",
            "b50fbcbc9df2a587a22fb630435df38d")

        mountListView(call, cityWeatherAdapter, this)

        // refatorar
        list_view.setOnItemClickListener { adapterView: AdapterView<*>, view: View, position: Int, id: Long ->

            val item = cityWeatherAdapter?.getItem(position)

            val intent = Intent(this, WeatherMapActivity::class.java)
            intent.putExtra("lat", item?.lat)
            intent.putExtra("lon", item?.lon)
            intent.putExtra("city", item?.city)
            intent.putStringArrayListExtra("latLngList", citiesLatLongList)

            startActivity(intent)
        }

    }

    private fun getAllLatLng(weatherApiResponse: WeatherApiResponse?) {

        citiesLatLongList = ArrayList<String>()

        weatherApiResponse?.list?.map { place ->
            citiesLatLongList?.add("${place.coord.lat},${place.coord.lon}")
        }
    }

    private fun callApi(latitude: String?,
                        longitude: String?,
                        cnt: String,
                        units: String,
                        lang: String,
                        appId: String) : Call<WeatherApiResponse>? {

        if (latitude != null && longitude != null) {

            call = callWeatherApi(
                latitude!!,
                longitude!!,
                cnt,
                units,
                lang,
                appId
            )

            return call
        }

        return null
    }

    private fun mountListView(call: Call<WeatherApiResponse>?, cityWeatherAdapter: CityWeatherAdapter?, context: Context) {

        call?.enqueue(object : Callback<WeatherApiResponse> {
            override fun onResponse(call: Call<WeatherApiResponse>, response: Response<WeatherApiResponse>) {

                if(response.code() == 200) {
                    val responseBody: WeatherApiResponse? = response.body()

                    cityWeatherAdapter?.addAll(map(responseBody!!))
                    list_view.adapter = cityWeatherAdapter

                    getAllLatLng(responseBody)

                }

            }

            override fun onFailure(call: Call<WeatherApiResponse>, t: Throwable) {

            }
        })

    }

    private fun callWeatherApi(
        latitude: String,
        longitude: String,
        cnt: String,
        units: String,
        lang: String,
        appId: String) : Call<WeatherApiResponse>  {

        val retrofitClient = HttpRequestClient.getRetrofitInstance("http://api.openweathermap.org/")

        val service = retrofitClient.create(WeatherService::class.java)
        return service.getCurrentWeatherData(
            latitude,
            longitude,
            cnt,
            units,
            lang,
            appId)

    }

    private fun map(weatherApiResponse : WeatherApiResponse) : List<CityWeather> {

        return weatherApiResponse.list.map { place ->
            CityWeather(place.coord.lat,
                        place.coord.lon,
                        place.name,
                        place.weather.get(0).description, place.main.temp.roundToInt().toString() + "º",
                        place.main.tempMin.roundToInt().toString() + "º",
                        place.main.tempMax.roundToInt().toString() + "º",
                        place.weather.get(0).icon)
        }

    }

    private fun getLastLocation(cityWeatherAdapter: CityWeatherAdapter?) {
        if (checkPermissions()) {
            if(isLocationEnabled()) {
                mFusedLocationClient!!.lastLocation.addOnCompleteListener { task ->
                    val location: Location? = task.result

                    if(location == null) {
                        requestNewLocationData()
                    } else {
                        latitude = location.latitude.toString()
                        longitude = location.longitude.toString()

                        call = callApi(
                            latitude,
                            longitude,
                            "50",
                            "metric",
                            "pt_br",
                            "b50fbcbc9df2a587a22fb630435df38d")

                        mountListView(call, cityWeatherAdapter, this)

                    }
                }
            }
        } else {
            requestPermissions()
        }
    }

    private fun requestNewLocationData() {

        var mLocationRequest: LocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 0
        mLocationRequest.fastestInterval = 0
        mLocationRequest.numUpdates = 1

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        mFusedLocationClient!!.requestLocationUpdates(mLocationRequest,
                                                    mLocationCallBack,
                                                    Looper.myLooper())

    }

    private val mLocationCallBack = object : LocationCallback() {

        override fun onLocationResult(locationResult: LocationResult?) {

            val mLastLocation: Location? = locationResult!!.lastLocation

            latitude = mLastLocation!!.latitude.toString()
            longitude = mLastLocation.longitude.toString()

        }

    }

    private fun checkPermissions(): Boolean {
        if(ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(
                    this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                return true

        }

        return false
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION), permissionId)
    }

    private fun isLocationEnabled() : Boolean {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode == permissionId) {
            if(grantResults.size > 0 && grantResults.get(0) == PackageManager.PERMISSION_GRANTED) {
                getLastLocation(cityWeatherAdapter)
            }
        }
    }

}
