package com.example.android.wearable.composeforwearos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.wear.compose.navigation.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeatherApp()
        }
    }
}
@Composable
fun WeatherApp() {
    val navController = rememberSwipeDismissableNavController()

    SwipeDismissableNavHost(
        navController = navController,
        startDestination = "weather_today"
    ) {
        composable("weather_today") {
            WeatherTodayScreen(
                onViewWeekClick = {
                    navController.navigate("weather_list")
                }
            )
        }
        composable("weather_list") {
            WeatherListScreen(
                onDetailsClick = {
                    navController.navigate("weather_detail")
                }
            )
        }
        composable("weather_detail") {
            WeatherDetailScreen()
        }
    }
}
