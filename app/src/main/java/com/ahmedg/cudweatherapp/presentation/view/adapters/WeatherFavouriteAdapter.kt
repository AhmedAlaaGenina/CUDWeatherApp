package com.ahmedg.cudweatherapp.presentation.view.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ahmedg.cudweatherapp.R
import com.ahmedg.cudweatherapp.databinding.FavouriteRowBinding
import com.ahmedg.cudweatherapp.model.WeatherResult
import com.ahmedg.cudweatherapp.presentation.view.ui.CellClickListener
import com.bumptech.glide.Glide

class WeatherFavouriteAdapter(
    private val cellClickListener: CellClickListener
) : RecyclerView.Adapter<WeatherFavouriteAdapter.WeatherFavouriteViewHolder>() {
    private val weatherList = ArrayList<WeatherResult>()

    class WeatherFavouriteViewHolder(private var binding: FavouriteRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SimpleDateFormat")
        fun bind(item: WeatherResult, cellClickListener: CellClickListener) {
            binding.tvCityNameFavouriteRow.text = item.timezone
            binding.tvTempFavouriteRow.text = item.current.temp.toString()
            binding.tvDescFavouriteRow.text = item.current.weather[0].main
            itemView.setOnClickListener {
                cellClickListener.onCellClickListener(item)
            }
            Glide.with(binding.root.context)
                .load("https://openweathermap.org/img/wn/${item.current.weather[0].icon}@4x.png")
                .into(binding.ivImageFavouriteRow)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherFavouriteViewHolder {
        return WeatherFavouriteViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.favourite_row,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: WeatherFavouriteViewHolder, position: Int) {
        holder.bind(weatherList[position], cellClickListener)
    }

    override fun getItemCount(): Int {
        return weatherList.size
    }

    fun setList(weather: List<WeatherResult>) {
        weatherList.clear()
        weatherList.addAll(weather)
    }

    fun getWeatherList(): ArrayList<WeatherResult> {
        return weatherList
    }

}