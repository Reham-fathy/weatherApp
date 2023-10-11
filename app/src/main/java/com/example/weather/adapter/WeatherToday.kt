package com.example.weather.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.Forecast.WeatherList
import com.example.weather.R
import java.text.SimpleDateFormat
import java.util.*

class WeatherToday :RecyclerView.Adapter<WeatherToday.TodayHolder> (){

private var listOfTodayWeather= listOf<WeatherList>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodayHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.todayforcastlist,parent,false)
        return TodayHolder(view)
    }

    override fun onBindViewHolder(holder: TodayHolder, position: Int) {
     val todayForecast=listOfTodayWeather[position]
        holder.timeDisplay.text=todayForecast.dtTxt!!.substring(11,16).toString()
        val temperatureFahrenheit=todayForecast.main?.temp
        val temperatureCelsius=(temperatureFahrenheit?.minus(273.15))
        val temperatureFormatted=String.format("%.2f",temperatureCelsius)
        holder.tempDisplay.text="$temperatureFormatted C"
        val calender=Calendar.getInstance()
        val dateFormated=SimpleDateFormat("HH::mm")
        val formatedTime=dateFormated.format(calender.time)

       val timeofapi=todayForecast.dtTxt!!.split(" ")
        val partafterspace=timeofapi[1]
        Log.e("time","formated time :${formatedTime}, timeofapi: ${partafterspace}")
        for (i in todayForecast.weather){
            if (i.icon=="01d"){
                holder.imageDisplay.setImageResource(R.drawable.oned)
            }
            if (i.icon=="01n"){
                holder.imageDisplay.setImageResource(R.drawable.onen)
            }
            if (i.icon=="02n"){
                holder.imageDisplay.setImageResource(R.drawable.twon)
            }
            if (i.icon=="03d"||i.icon=="03n"){
                holder.imageDisplay.setImageResource(R.drawable.threedn)
            }
            if (i.icon=="10n"){
                holder.imageDisplay.setImageResource(R.drawable.tenn)
            }
            if (i.icon=="04d"||i.icon=="04n"){
                holder.imageDisplay.setImageResource(R.drawable.fourdn)
            }
            if (i.icon=="09d"||i.icon=="09n"){
                holder.imageDisplay.setImageResource(R.drawable.ninedn)
            }
            if (i.icon=="11d"||i.icon=="11n"){
                holder.imageDisplay.setImageResource(R.drawable.elevend)
            }
            if (i.icon=="13d"||i.icon=="13n"){
                holder.imageDisplay.setImageResource(R.drawable.thirteend)
            }
            if (i.icon=="50d"||i.icon=="50n"){
                holder.imageDisplay.setImageResource(R.drawable.fiftydn)
            }
        }
    }

    override fun getItemCount(): Int {
       return listOfTodayWeather.size
    }
    fun setList(newlist:List<WeatherList>){
        this.listOfTodayWeather=newlist
    }

    class TodayHolder(itemView: View):RecyclerView.ViewHolder(itemView){
     val imageDisplay:ImageView=itemView.findViewById(R.id.imageDisplay)
     val tempDisplay:TextView=itemView.findViewById(R.id.tempDisplay)
     val timeDisplay:TextView=itemView.findViewById(R.id.timeDisplay)

    }
}


