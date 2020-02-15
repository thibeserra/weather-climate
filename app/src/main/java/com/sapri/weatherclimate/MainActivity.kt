package com.sapri.weatherclimate


import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import androidx.appcompat.widget.Toolbar
import com.google.android.gms.location.*
import com.sapri.weatherclimate.adapter.CityWeatherAdapter
import com.sapri.weatherclimate.data.weatherapi.CityWeather
import com.sapri.weatherclimate.data.weatherapi.WeatherApiResponse
import com.sapri.weatherclimate.utils.http.HttpRequestClient
import com.sapri.weatherclimate.utils.http.weatherapi.WeatherService
import com.sapri.weatherclimate.utils.location.LocationUtils
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.roundToInt


class MainActivity : AppCompatActivity() {

    private var mFusedLocationClient: FusedLocationProviderClient? = null
    private var call: Call<WeatherApiResponse>? = null
    private var cityWeatherAdapter: CityWeatherAdapter? = null
    private var citiesLatLongList: ArrayList<String>? = null
    private val locationUtils: LocationUtils = LocationUtils(this, this)
    private var weatherMetric = "metric"
    private val weatherCnt = "50"
    private val weatherAppId = "b50fbcbc9df2a587a22fb630435df38d"
    private val weatherLanguage = "pt_br"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<Toolbar>(R.id.main_toolbar)
        setSupportActionBar(toolbar)

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        cityWeatherAdapter = CityWeatherAdapter(this)

        getLastLocation(cityWeatherAdapter, weatherMetric)

        list_view.setOnItemClickListener { _: AdapterView<*>, _: View, position: Int, _: Long ->

            val item = cityWeatherAdapter?.getItem(position)

            val intent = Intent(this, WeatherMapActivity::class.java)
            intent.putExtra("lat", item?.lat)
            intent.putExtra("lon", item?.lon)
            intent.putStringArrayListExtra("latLngList", citiesLatLongList)

            startActivity(intent)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.weather_menu, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId) {
            R.id.celsius -> weatherMetric = "metric"
            R.id.fahrenheit -> weatherMetric = "imperial"
            R.id.kelvin -> weatherMetric = "kelvin"
        }

        getLastLocation(cityWeatherAdapter, weatherMetric)

        return super.onOptionsItemSelected(item)
    }

    private fun getAllLatLng(weatherApiResponse: WeatherApiResponse?) {

        citiesLatLongList = ArrayList()

        weatherApiResponse?.list?.map { place ->
            citiesLatLongList?.add("${place.coord.lat},${place.coord.lon}")
        }
    }

    private fun callApi(latitude: String?,
                        longitude: String?,
                        cnt: String,
                        units: String?,
                        lang: String,
                        appId: String) : Call<WeatherApiResponse>? {

        if (latitude != null && longitude != null) {

            call = callWeatherApi(
                latitude,
                longitude,
                cnt,
                units,
                lang,
                appId
            )

            return call
        }

        return null
    }

    private fun mountListView(call: Call<WeatherApiResponse>?, cityWeatherAdapter: CityWeatherAdapter?) {

        cityWeatherAdapter?.clear()

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
        units: String?,
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
                place.weather[0].description,
                formatWeatherTemperature(place.main.temp),
                "Min: ${formatWeatherTemperature(place.main.tempMin)}",
                "Máx: ${formatWeatherTemperature(place.main.tempMax)}",
                place.weather[0].icon)
        }

    }

    private fun formatWeatherTemperature(temperature: Double) : String? {

        var weatherTemperature: String? = null

        when(weatherMetric) {
            "metric" -> weatherTemperature = "${temperature.roundToInt()} ºC"
            "imperial" -> weatherTemperature = "${temperature.roundToInt()} ºF"
            "kelvin" -> weatherTemperature = "${temperature.roundToInt()} K"
        }

        return weatherTemperature
    }

    private fun getLastLocation(cityWeatherAdapter: CityWeatherAdapter?, weatherMetric: String?) {
        if (locationUtils.checkPermissions()) {
            if(locationUtils.isLocationEnabled()) {
                mFusedLocationClient!!.lastLocation.addOnCompleteListener { task ->
                    val location: Location? = task.result

                    if(location == null) {
                        locationUtils.newLocationRequest()
                    } else {
                        locationUtils.latitude = location.latitude.toString()
                        locationUtils.longitude = location.longitude.toString()

                        call = callApi(
                            locationUtils.latitude,
                            locationUtils.longitude,
                            weatherCnt,
                            weatherMetric,
                            weatherLanguage,
                            weatherAppId)

                        mountListView(call, cityWeatherAdapter)

                    }
                }
            }
        } else {
            locationUtils.requestPermissions()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode == locationUtils.permissionId) {
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation(cityWeatherAdapter, weatherMetric)
            }
        }
    }

}
