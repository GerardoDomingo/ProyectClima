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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import com.example.android.wearable.composeforwearos.theme.Purple200
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.items
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.material.CircularProgressIndicator
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.PositionIndicator
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
import androidx.wear.compose.material.Vignette
import androidx.wear.compose.material.VignettePosition
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

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
                        Spacer(modifier = Modifier.height(15.dp))
                        Text("Lugar: ${data.location.name}", fontSize = 14.sp)
                        Spacer(modifier = Modifier.height(5.dp))
                        Icon(
                            imageVector = Icons.Default.WbSunny,
                            contentDescription = null,
                            modifier = Modifier.size(40.dp)
                        )
                        Spacer(modifier = Modifier.height(5.dp))
                        Text("${data.current.temp_c}°C", fontSize = 14.sp)
                        Text("Humedad: ${data.current.humidity}%", fontSize = 12.sp)
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(
                            onClick = onViewWeekClick,
                            modifier = Modifier
                                .height(30.dp)
                                .width(70.dp),
                            shape = RoundedCornerShape(50.dp),
                            colors = ButtonDefaults.primaryButtonColors(
                                backgroundColor = Purple200,
                                contentColor = Color.Black
                            )
                        ) {
                            Text("Más", fontSize = 12.sp)
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
    var forecastData by remember { mutableStateOf<ForecastResponse?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        val query = "21.154670,-98.379956"
        try {
            forecastData = RetrofitClient.service.getForecastWeather(
                apiKey = Constants.API_KEY,
                query = query,
                days = 7
            )
        } catch (e: Exception) {
            Log.e("ForecastError", "Error al obtener el pronóstico: ${e.message}", e)
        }
        isLoading = false
    }

    Scaffold(
        timeText = { TimeText() },
        vignette = { Vignette(vignettePosition = VignettePosition.TopAndBottom) },
        positionIndicator = { PositionIndicator(scalingLazyListState = listState) }
    ) {
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            forecastData?.let { forecast ->
                val sortedForecast = forecast.forecast.forecastday.sortedBy {
                    LocalDate.parse(it.date, DateTimeFormatter.ISO_DATE)
                }

                val today = LocalDate.now()

                ScalingLazyColumn(
                    state = listState,
                    modifier = Modifier
                        .fillMaxSize()
                        .offset(y = (-16).dp), // <- Esto sube todo visualmente
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp)
                ) {
                    item {
                        Text(
                            text = "Clima: ${forecast.location.name}",
                            fontSize = 12.sp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 15.dp),
                            textAlign = TextAlign.Center
                        )
                    }

                    items(sortedForecast) { day ->
                        val localDate = LocalDate.parse(day.date, DateTimeFormatter.ISO_DATE)
                        val dayName = if (localDate.isEqual(today)) {
                            "Hoy"
                        } else {
                            localDate.dayOfWeek.getDisplayName(TextStyle.FULL, Locale("es"))
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(dayName, fontSize = 12.sp)
                            Icon(
                                imageVector = Icons.Filled.Cloud,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp)
                            )
                            Text("${day.day.avgtemp_c}°C", fontSize = 12.sp)
                            Text("${day.day.avghumidity}%", fontSize = 12.sp)
                        }
                    }
                }
            } ?: Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No se pudo obtener el pronóstico")
            }
        }
    }
}
