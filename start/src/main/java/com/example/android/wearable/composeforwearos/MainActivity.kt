// MainActivity.kt
package com.example.android.wearable.composeforwearos

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.compose.runtime.Composable
import androidx.wear.compose.navigation.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestLocationPermission()
        setContent {
            WeatherApp()
        }
    }

    private fun requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                1
            )
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
                },
                onViewDetailsClick = {
                    navController.navigate("weather_detail")
                }
            )
        }

        composable("weather_list") {
            WeatherListScreen(
                onBackClick = {
                    navController.navigateUp()
                }
            )
        }

        composable("weather_detail") {
            WeatherDetailScreen(
                onBackClick = {
                    navController.navigateUp()
                }
            )
        }
    }
}