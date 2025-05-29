// WeatherService.kt
package com.example.android.wearable.composeforwearos

import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

data class WeatherResponse(
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
}

object RetrofitClient {
    val service: WeatherService by lazy {
        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WeatherService::class.java)
    }
}

