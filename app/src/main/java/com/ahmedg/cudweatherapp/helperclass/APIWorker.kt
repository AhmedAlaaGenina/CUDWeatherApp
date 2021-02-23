package com.ahmedg.cudweatherapp.helperclass

import android.content.Context
import android.content.Intent
import android.provider.AlarmClock
import android.util.Log
import androidx.work.*
import com.ahmedg.cudweatherapp.data.local.WeatherDataBase
import com.ahmedg.cudweatherapp.data.remote.ApiWeather
import com.ahmedg.cudweatherapp.data.remote.Constant
import com.ahmedg.cudweatherapp.model.WeatherResultCurrent
import com.ahmedg.cudweatherapp.presentation.view.ui.HomeFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import java.util.concurrent.TimeUnit

class APIWorker(context: Context, params: WorkerParameters) : Worker(context, params) {
    private val dao = WeatherDataBase.getInstance(applicationContext).weatherDAO
    override fun doWork(): Result {
        return try {
            Log.i("TAG", "doWork: Done")
            val lat = inputData.getDouble(API_LAT, 0.0)
            val lon = inputData.getDouble(API_LON, 0.0)
            val lang = inputData.getString(API_LANG)
            val units = inputData.getString(API_UNITS)
            val id = inputData.getDouble(API_ID, 0.0)
            if (units != null) {
                if (lang != null) {
                    fetchWeatherAPI(id, lat, lon, lang, units)
                }
            }
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }

    companion object {
        const val API_LAT = "CUDWeatherApp_API_LAT"
        const val API_LON = "CUDWeatherApp_API_LON"
        const val API_LANG = "CUDWeatherApp_API_LANG"
        const val API_UNITS = "CUDWeatherApp_API_UNITS"
        const val NOTIFICATION_ID = "CUDWeatherApp_notification_id"
        const val API_ID = "CUDWeatherApp_API_ID"
        const val NOTIFICATION_DES = "CUDWeatherApp_notification_Des"
        const val NOTIFICATION_END = "CUDWeatherApp_notification_END"
        const val NOTIFICATION_START = "CUDWeatherApp_notification_START"
    }

    private fun fetchWeatherAPI(id: Double, lat: Double, lon: Double, lang: String, units: String) {
        val currentTime = Calendar.getInstance().timeInMillis / 1000
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
                    insertCurrent(response.body()!!)
                    if (response.body()!!.alerts != null) {
                        notificationWorkerFun(
                            id.toLong(),
                            response.body()!!.alerts!![0].event,
                            response.body()!!.alerts!![0].start - currentTime,
                            response.body()!!.alerts!![0].end,
                            response.body()!!.alerts!![0].start
                        )
                    }
                }
            }
        }
    }

    private fun notificationWorkerFun(id: Long, alert: String, delay: Long, end: Int, start: Int) {
        val workManager = WorkManager.getInstance(applicationContext)
        val data = workDataOf(
            NOTIFICATION_ID to id, NOTIFICATION_DES to alert,
            NOTIFICATION_END to end, NOTIFICATION_START to start
        )
        val notificationWork = OneTimeWorkRequest.Builder(NotificationWorker::class.java)
            .setInputData(data).setInitialDelay(delay - 3600, TimeUnit.SECONDS).build()
        workManager.enqueue(notificationWork)
    }

    private fun insertCurrent(weatherResult: WeatherResultCurrent) {
        GlobalScope.launch {
            Dispatchers.IO
            dao.insertCurrentWeather(weatherResult)
        }
    }
}