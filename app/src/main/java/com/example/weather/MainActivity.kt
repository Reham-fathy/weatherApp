package com.example.weather

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.AsyncTask
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.Forecast.WeatherList
import com.example.weather.adapter.WeatherToday
import com.example.weather.databinding.ActivityMainBinding
import com.example.weather.mvvm.WeatherVm
import org.json.JSONObject
import java.lang.Exception
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*
@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {


 lateinit var vm:WeatherVm
 lateinit var adapter: WeatherToday
 private lateinit var binding:ActivityMainBinding

 var longi:String=""
    var lati:String=""

    val API:String="b16f61004a0dfa983d5edc2db25e92f7"
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=DataBindingUtil.setContentView(this,R.layout.activity_main)
       vm=ViewModelProvider(this).get(WeatherVm::class.java)
        binding.lifecycleOwner=this
        binding.vm=vm
        adapter= WeatherToday()
        val sharedPrefs=SharedPrefs.getInstance(this@MainActivity)
        sharedPrefs.clearCityValue()
        vm.todayWeatherLiveData.observe(this, androidx.lifecycle.Observer {
            val setNewlist=it as List<WeatherList>

            Log.e("TODAYweather list",it.toString())
            adapter.setList(setNewlist)
            binding.forecastRecyclerView.adapter=adapter


        })
        vm.closeorexactlysameweatherdata.observe(this, androidx.lifecycle.Observer {
            val temperatureFahrenheit=it!!.main?.temp
            val temperatureCelsius=(temperatureFahrenheit?.minus(273.15))
            val temperatureFormatted=String.format("%.2f",temperatureCelsius)

            for (i in it.weather){
                binding.descMain.text=i.description
                if (i.main.toString()=="Rain"||
                        i.main.toString()=="Drizzle"||
                        i.main.toString()=="Thunderstorm"||
                        i.main.toString()=="Clear"){
                    Log.e("MAIN",i.main.toString())
                }
            }
            binding.humidityMain.text=it.main!!.humidity.toString()
            binding.windSpeed.text=it.wind?.speed.toString()

           val inputFormat=SimpleDateFormat("yyyy-MM-dd HH:mm",Locale.getDefault())
            val date= inputFormat.parse(it.dtTxt!!)
            val outputFormat=SimpleDateFormat("d MMMM EEEE",Locale.getDefault())
            val dateanddayname=outputFormat.format(date!!)

            binding.dateDayMain.text=dateanddayname
            binding.chanceofrain.text="${it.pop.toString()}%"
            for (i in it.weather){
                if (i.icon=="01d"){
                   binding.imageMain.setImageResource(R.drawable.oned)
                }
                if (i.icon=="01n"){
                    binding.imageMain.setImageResource(R.drawable.onen)

                }
                if (i.icon=="02d"){
                    binding.imageMain.setImageResource(R.drawable.twod)

                }
                if (i.icon=="02n"){
                    binding.imageMain.setImageResource(R.drawable.twon)

                }
                if (i.icon=="03d"||i.icon=="03n"){
                    binding.imageMain.setImageResource(R.drawable.threedn)

                }
                if (i.icon=="10d"){
                    binding.imageMain.setImageResource(R.drawable.tend)

                }
                if (i.icon=="10n"){
                    binding.imageMain.setImageResource(R.drawable.tenn)

                }
                if (i.icon=="04d"||i.icon=="04n"){
                    binding.imageMain.setImageResource(R.drawable.fourdn)

                }
                if (i.icon=="09d"||i.icon=="09n"){
                    binding.imageMain.setImageResource(R.drawable.ninedn)

                }
                if (i.icon=="11d"||i.icon=="11n"){
                    binding.imageMain.setImageResource(R.drawable.elevend)

                }
                if (i.icon=="13d"||i.icon=="13n"){
                    binding.imageMain.setImageResource(R.drawable.thirteend)

                }
                if (i.icon=="50d"||i.icon=="50n"){
                    binding.imageMain.setImageResource(R.drawable.fiftydn)

                }
            }


        })
//        val searchEditText=
//            binding.searchView.findViewById<EditText>(R.id.searchView)
//        searchEditText.setTextColor(Color.WHITE)
        binding.next5Days.setOnClickListener {
            startActivity(Intent(this,MainActivity::class.java))
        }
        binding.searchView.setOnQueryTextListener(object:SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                val sharedPrefs=SharedPrefs.getInstance(this@MainActivity)
                sharedPrefs.setValueOrNull("city",query!!)
                if (!query.isNullOrEmpty()){
                    vm.getWeather(query)
                    binding.searchView.setQuery("",false)
                    binding.searchView.clearFocus()
                    binding.searchView.isIconified=true
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })

    }


}



