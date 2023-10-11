package com.example.weather.mvvm

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.Forecast.WeatherList
import com.example.weather.MyApplication
import com.example.weather.service.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
class WeatherVm :ViewModel() {

    val todayWeatherLiveData = MutableLiveData<List<WeatherList>>()
    val forecastWeatherLiveData = MutableLiveData<List<WeatherList>>()

    val closeorexactlysameweatherdata = MutableLiveData<WeatherList?>()
    val cityName = MutableLiveData<String>()
    val context = MyApplication.instance

    fun getWeather(city: String? = null, lati: String? = null, longi: String? = null) =
        viewModelScope.launch(Dispatchers.IO) {
            val todayWeatherList = mutableListOf<WeatherList>()
            val currentDateTime = LocalDateTime.now()
            val currentDateO = currentDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            Log.e("ViewModelCoordinates", "$lati $longi")
            val call = if (city != null) {
                RetrofitInstance.api.getWeatherByCity(city)
            } else {
                RetrofitInstance.api.getCurrentWeather(lati!!, longi!!)
            }
            val response = call.execute()
            if (response.isSuccessful) {
                val weatherList = response.body()?.list
                cityName.postValue(response.body()?.city!!.name)
                val currentDate = currentDateO
                weatherList?.forEach { weather ->
                    if (weather.dtTxt!!.split("\\s".toRegex()).contains(currentDate)) {
                        todayWeatherList.add(weather)
                    }

                }
                val closesWeather = findClosestWeather(todayWeatherList)
                closeorexactlysameweatherdata.postValue(closesWeather)
                todayWeatherLiveData.postValue(todayWeatherList)
            } else {
                val errorMessage = response.message()
                Log.e("CurrentWeatherError", "Error :$errorMessage")
            }
        }
fun getForecastUpcoming(city:String?=null,lati:String,longi:String?=null)=viewModelScope.launch(Dispatchers.IO){
val forecastWeatherList= mutableListOf<WeatherList>()
    val currentDateTime=LocalDateTime.now()
    val currentDateO=currentDateTime.format(DateTimeFormatter.ofPattern(("yyyy-MM-dd")))
    val call=if (city !=null){
        RetrofitInstance.api.getWeatherByCity(city)
    }
    else{
        RetrofitInstance.api.getCurrentWeather(lati!!,longi!!)
    }
    val response=call.execute()
    if (response.isSuccessful){
        val weatherList =response.body()?.list
        val currentDate =currentDateO
        weatherList?.forEach{ weather->
            if (!weather.dtTxt!!.split("\\s".toRegex()).contains(currentDate))
            {
                if (weather.dtTxt!!.substring(11, 16) == "12:00") {
                    forecastWeatherList.add(weather)


                }
            }

        }
        forecastWeatherLiveData.postValue(forecastWeatherList)
        Log.d("Forecast LiveData",forecastWeatherLiveData.value.toString())

    }else{
        val errorMessage=response.message()
        Log.e("currentWeatherError","Error : $errorMessage")
    }
}
    private fun findClosestWeather(weatherList:List<WeatherList>):WeatherList?{
        val systemTime=LocalDateTime.now().format(DateTimeFormatter.ofPattern(("HH:mm")))
        var closestWeather:WeatherList?=null
        var minTimeDifference=Int.MAX_VALUE
        for (weather in weatherList)
        {
            val weatherTime=weather.dtTxt!!.substring(11,16)
            val timeDifference=Math.abs(timeToMinutes(weatherTime)-timeToMinutes(systemTime))
            if (timeDifference<minTimeDifference){
                minTimeDifference=timeDifference
                closestWeather=weather
            }
        }
        return closestWeather
    }

    private fun timeToMinutes(time:String):Int{
        val parts=time.split(":")
        return parts[0].toInt()*60+parts[1].toInt()
    }
}
