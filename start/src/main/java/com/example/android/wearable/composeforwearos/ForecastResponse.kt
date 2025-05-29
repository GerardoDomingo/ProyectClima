// ForecastResponse.kt
package com.example.android.wearable.composeforwearos

data class ForecastResponse(
    val location: Location,
    val forecast: Forecast
)


data class Forecast(
    val forecastday: List<ForecastDay>
)

data class ForecastDay(
    val date: String,
    val day: Day
)

data class Day(
    val avgtemp_c: Float,
    val avghumidity: Float
)
