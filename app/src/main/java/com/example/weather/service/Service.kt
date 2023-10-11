package com.example.weather.service

import com.example.Forecast.Forecast
import com.example.weather.Utils
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface Service {
    @GET("forecast?")
    fun getCurrentWeather(
        @Query("let")
        lat:String,
        @Query("lon")
        lon:String,
        @Query("appid")
        appid:String=Utils.API_KEY
    ):Call<Forecast>


    @GET("forecast?")
    fun getWeatherByCity(
        @Query("q")
        city:String,
        @Query("appid")
        appid:String=Utils.API_KEY
    ):Call<Forecast>
}