package com.ahmedg.cudweatherapp.data.repository

import androidx.lifecycle.MutableLiveData
import com.ahmedg.cudweatherapp.data.local.WeatherDAO
import com.ahmedg.cudweatherapp.data.remote.ApiWeather
import com.ahmedg.cudweatherapp.data.remote.Constant
import com.ahmedg.cudweatherapp.model.CreateAlert
import com.ahmedg.cudweatherapp.model.WeatherResult
import com.ahmedg.cudweatherapp.model.WeatherResultCurrent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

//this package and class that get data from local or remote

class WeatherRepository(

    private val db: WeatherDAO
) {

    private var weatherResponseMutableLiveData: MutableLiveData<WeatherResult> = MutableLiveData()
    private var weatherResponseMutableLiveDataCurrent: MutableLiveData<WeatherResultCurrent> =
        MutableLiveData()

    fun fetchWeatherAPI(
        lat: Double,
        lon: Double,
        lang: String,
        units: String
    ): MutableLiveData<WeatherResult> {
        GlobalScope.launch {
            Dispatchers.IO
            val response = ApiWeather.getApiService()
                .getWeather(
                    lat.toString(),
                    lon.toString(),
                    lang,
                    "minutely",
                    units,
                    Constant.APP_ID
                )
            if (response.isSuccessful) {
                withContext(Dispatchers.Main) {
                    weatherResponseMutableLiveData.value = response.body()
                }
            }
        }
        return weatherResponseMutableLiveData
    }

    fun fetchWeatherAPICurrent(
        lat: Double,
        lon: Double,
        lang: String,
        units: String
    ): MutableLiveData<WeatherResultCurrent> {
        GlobalScope.launch {
            Dispatchers.IO
            val response = ApiWeather.getApiService()
                .getWeatherCurrent(
                    lat.toString(),
                    lon.toString(),
                    lang,
                    "minutely",
                    units,
                    Constant.APP_ID
                )
            if (response.isSuccessful) {
                withContext(Dispatchers.Main) {
                    weatherResponseMutableLiveDataCurrent.value = response.body()
                }
            }
        }
        return weatherResponseMutableLiveDataCurrent
    }

    fun insert(weatherResult: WeatherResult) {
        GlobalScope.launch {
            Dispatchers.IO
            db.insertWeather(weatherResult)
        }
    }
    fun insertAlert(createAlert: CreateAlert) {
        GlobalScope.launch {
            Dispatchers.IO
            db.insertAlert(createAlert)
        }
    }
    fun insertCurrent(weatherResult: WeatherResultCurrent) {
        GlobalScope.launch {
            Dispatchers.IO
            db.insertCurrentWeather(weatherResult)
        }
    }

    fun delete(weatherResult: WeatherResult) {
        GlobalScope.launch {
            Dispatchers.IO
            db.deleteWeather(weatherResult)
        }
    }
    fun deleteAlert(createAlert: CreateAlert) {
        GlobalScope.launch {
            Dispatchers.IO
            db.deleteAlert(createAlert)
        }
    }
    val weatherList = db.getAllWeather()
    val weatherCurrentList = db.getAllCurrent()
    val alertList = db.getAllAlert()


}