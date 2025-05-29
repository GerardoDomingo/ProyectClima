// WeatherService.kt
package com.example.android.wearable.composeforwearos

import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

data class WeatherResponse(
    val location: Location,
    val current: Current
)


data class Current(
    val temp_c: Float,
    val humidity: Int,
    val condition: Condition
)

data class Condition(
    val text: String,
    val icon: String
)

interface WeatherService {
    @GET("current.json")
    suspend fun getCurrentWeather(
        @Query("key") apiKey: String,
        @Query("q") query: String
    ): WeatherResponse

    @GET("forecast.json")
    suspend fun getForecastWeather(
        @Query("key") apiKey: String,
        @Query("q") query: String,
        @Query("days") days: Int
    ): ForecastResponse
}
data class Location(
    val name: String,
    val region: String,
    val country: String,
    val lat: Double,
    val lon: Double,
    val tz_id: String,
    val localtime: String
)

object RetrofitClient {
    val service: WeatherService by lazy {
        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WeatherService::class.java)
    }
}

