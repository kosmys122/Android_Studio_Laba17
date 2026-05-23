package com.shapkin.weatherdashboard.data

data class WeatherData(
    val temperatyre:Int?=null,
    val humidity:Int?=null,
    val windSpeed:Int?=null,
    val isLoading: Boolean=false,
    val error: String?=null
)