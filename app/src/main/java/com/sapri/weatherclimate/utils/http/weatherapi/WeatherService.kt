package com.sapri.weatherclimate.utils.http.weatherapi

import com.sapri.weatherclimate.data.weatherapi.WeatherApiResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {

    @GET("data/2.5/find?")
    fun getCurrentWeatherData(@Query("lat") lat: String,
                              @Query("lon") lon: String,
                              @Query("cnt") cnt: String,
                              @Query("units") units: String?,
                              @Query("lang") lang: String,
                              @Query("APPID") app_id: String): Call<WeatherApiResponse>


}