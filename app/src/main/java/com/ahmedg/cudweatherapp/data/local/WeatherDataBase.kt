package com.ahmedg.cudweatherapp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ahmedg.cudweatherapp.model.CreateAlert
import com.ahmedg.cudweatherapp.model.WeatherResult
import com.ahmedg.cudweatherapp.model.WeatherResultCurrent

@Database(
    entities = [WeatherResult::class, WeatherResultCurrent::class ,CreateAlert::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class WeatherDataBase : RoomDatabase() {
    abstract val weatherDAO: WeatherDAO

    companion object {
        @Volatile
        private var INSTANCE: WeatherDataBase? = null
        fun getInstance(context: Context): WeatherDataBase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        WeatherDataBase::class.java, "WeatherResult"
                    ).build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}


