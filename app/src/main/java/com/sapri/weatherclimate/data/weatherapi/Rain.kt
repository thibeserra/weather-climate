package com.sapri.weatherclimate.data.weatherapi

import com.google.gson.annotations.SerializedName

data class Rain(
    @SerializedName("3h")
    val threeH: Double
)