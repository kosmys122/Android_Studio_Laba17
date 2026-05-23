package com.shapkin.weatherdashboard.viewmodel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shapkin.weatherdashboard.data.WeatherData
import com.shapkin.weatherdashboard.data.WeatherRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.async

class WeatherViewModel: ViewModel(){
    private val repository= WeatherRepository()
    private val _weatherState=MutableStateFlow(WeatherData())
    val weatherState: StateFlow<WeatherData> =_weatherState.asStateFlow()
    init{
        loadWeatherData()
    }
    fun loadWeatherData(){
        viewModelScope.launch {
            _weatherState.value=_weatherState.value.copy(
                isLoading = true,
                error = null,
                loadingProgress = "Запуск загрузки..."
            )
            try {
                _weatherState.value=_weatherState.value.copy(
                    loadingProgress = "Загружаем температуру, влажность, скорость ветра..."
                )
                val temperatyreDeferred=async { repository.fetchTemperature() }
                val humidityDeferred=async { repository.fetchHumidity() }
                val windSpeedDeferred=async { repository.fetchWindSpeed() }
                val temperature=temperatyreDeferred.await()
                val humidity=humidityDeferred.await()
                val windSpeed=windSpeedDeferred.await()
                _weatherState.value= WeatherData(
                    temperatyre = temperature,
                    humidity = humidity,
                    windSpeed = windSpeed,
                    isLoading = false,
                    error = null,
                    loadingProgress = "Загрузка завершена!"
                )
//                val temperature=repository.fetchTemperature()
//                _weatherState.value=_weatherState.value.copy(temperatyre = temperature)
//                val humidity=repository.fetchHumidity()
//                _weatherState.value=weatherState.value.copy(humidity = humidity)
//                val windSpeed=repository.fetchWindSpeed()
//                _weatherState.value=_weatherState.value.copy(windSpeed = windSpeed)
//                _weatherState.value=_weatherState.value.copy(isLoading = false)
            } catch (e: Exception){
                _weatherState.value=_weatherState.value.copy(
                    isLoading = false,
                    error = "Ошибка загрузки: ${e.message}",
                    loadingProgress = ""
                )
            }
        }
    }

}
