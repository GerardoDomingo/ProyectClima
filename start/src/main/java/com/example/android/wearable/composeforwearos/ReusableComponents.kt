package com.example.android.wearable.composeforwearos

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.items
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.CircularProgressIndicator
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.PositionIndicator
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
import androidx.wear.compose.material.Vignette
import androidx.wear.compose.material.VignettePosition

@Composable
fun WeatherTodayScreen(onViewWeekClick: () -> Unit) {
    val context = LocalContext.current
    var weatherData by remember { mutableStateOf<WeatherResponse?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        val query = "21.154670,-98.379956"
        try {
            weatherData = RetrofitClient.service.getCurrentWeather(
                apiKey = Constants.API_KEY,
                query = query
            )
        } catch (e: Exception) {
            Log.e("WeatherError", "Error al obtener el clima: ${e.message}", e)
        }
        isLoading = false
    }

    Scaffold(timeText = { TimeText() }) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            if (isLoading) {
                CircularProgressIndicator()
            } else {
                weatherData?.let { data ->
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.WbSunny,
                            contentDescription = null,
                            modifier = Modifier.size(40.dp)
                        )
                        Text("Temp: ${data.current.temp_c}°C", fontSize = 14.sp)
                        Text("Humedad: ${data.current.humidity}%", fontSize = 12.sp)
                        Text("Condición: ${data.current.condition.text}", fontSize = 12.sp)
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(onClick = onViewWeekClick) {
                            Text("Ver semana", fontSize = 12.sp)
                        }
                    }
                } ?: Text("No se pudo obtener el clima.")
            }
        }
    }
}

@Composable
fun WeatherListScreen(onDetailsClick: () -> Unit) {
    val listState = rememberScalingLazyListState()

    Scaffold(
        timeText = { TimeText() },
        vignette = { Vignette(vignettePosition = VignettePosition.TopAndBottom) },
        positionIndicator = { PositionIndicator(scalingLazyListState = listState) }
    ) {
        ScalingLazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.Start,
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 16.dp)
        ) {
            item {
                Text("Semana actual (datos simulados)", fontSize = 12.sp)
            }
            items(listOf("Lun", "Mar", "Mié", "Jue", "Vie", "Sáb", "Dom")) { day ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(day, fontSize = 12.sp)
                    Icon(imageVector = Icons.Filled.Cloud, contentDescription = null, modifier = Modifier.size(20.dp))
                    Text("24°C", fontSize = 12.sp)
                    Text("65%", fontSize = 12.sp)
                }
            }
            item {
                Button(onClick = onDetailsClick, modifier = Modifier.fillMaxWidth()) {
                    Text("Detalles", fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
fun WeatherDetailScreen() {
    Scaffold(
        timeText = { TimeText() }
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Detalles del clima semanal", fontSize = 14.sp)
        }
    }
}
