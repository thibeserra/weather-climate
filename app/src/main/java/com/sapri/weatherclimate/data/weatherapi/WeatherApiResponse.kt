package com.sapri.weatherclimate.data.weatherapi

import kotlin.collections.List

data class WeatherApiResponse (
    val message: String,
    val cod: String,
    val count: Int,
    val list: List<ListPlaces>
)