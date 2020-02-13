package com.sapri.weatherclimate.data.weatherapi

data class Weather(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
)