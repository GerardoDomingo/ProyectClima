package com.example.android.wearable.composeforwearos

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.*
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.items
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.vector.ImageVector

data class DayWeather(
    val day: String,
    val temperature: String,
    val humidity: String,
    val icon: ImageVector
)

val sampleData = listOf(
    DayWeather("Mon", "25°C", "60%", Icons.Filled.WbSunny),
    DayWeather("Tue", "22°C", "70%", Icons.Filled.Cloud),
    DayWeather("Wed", "24°C", "65%", Icons.Filled.WbSunny),
    DayWeather("Thu", "20°C", "80%", Icons.Filled.Cloud),
    DayWeather("Fri", "26°C", "55%", Icons.Filled.WbSunny),
    DayWeather("Sat", "23°C", "60%", Icons.Filled.Cloud),
    DayWeather("Sun", "27°C", "50%", Icons.Filled.WbSunny),
)

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
            items(sampleData) { day ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(day.day, fontSize = 12.sp)
                    Icon(imageVector = day.icon, contentDescription = null, modifier = Modifier.size(20.dp))
                    Text(day.temperature, fontSize = 12.sp)
                    Text(day.humidity, fontSize = 12.sp)
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
@Composable
fun WeatherTodayScreen(onViewWeekClick: () -> Unit) {
    val todayWeather = DayWeather("Hoy", "25°C", "60%", Icons.Filled.WbSunny)

    Scaffold(
        timeText = { TimeText() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = todayWeather.icon,
                contentDescription = null,
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text("Clima de hoy", fontSize = 14.sp)
            Text("Temperatura: ${todayWeather.temperature}", fontSize = 12.sp)
            Text("Humedad: ${todayWeather.humidity}", fontSize = 12.sp)
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onViewWeekClick) {
                Text("Ver semana", fontSize = 12.sp)
            }
        }
    }
}
