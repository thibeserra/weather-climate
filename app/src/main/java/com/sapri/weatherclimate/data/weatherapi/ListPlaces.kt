package com.sapri.weatherclimate.data.weatherapi

data class ListPlaces(
    val id: String,
    val name: String,
    val coord: Coord,
    val main: Main,
    val dt: Long,
    val wind: Wind,
    val sys: Sys,
    val rain: Rain,
    val snow: String?,
    val clouds: Clouds,
    val weather: List<Weather>
)