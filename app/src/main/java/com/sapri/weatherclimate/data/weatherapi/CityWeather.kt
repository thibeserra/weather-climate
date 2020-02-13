package com.sapri.weatherclimate.data.weatherapi

data class CityWeather(
    val lat: Double,
    val lon: Double,
    val city: String,
    val wheatherDescription: String,
    val currentTemperature: String,
    val minTemperature: String,
    val maxTemperature: String,
    val iconId: String
)