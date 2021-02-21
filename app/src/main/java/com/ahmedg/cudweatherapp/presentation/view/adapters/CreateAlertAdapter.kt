package com.ahmedg.cudweatherapp.presentation.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ahmedg.cudweatherapp.R
import com.ahmedg.cudweatherapp.databinding.AlertViewBinding
import com.ahmedg.cudweatherapp.model.CreateAlert

class CreateAlertAdapter : RecyclerView.Adapter<CreateAlertAdapter.CreateAlertViewHolder>() {
    private val alertList = ArrayList<CreateAlert>()

    class CreateAlertViewHolder(private var binding: AlertViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CreateAlert) {
            val timeFormat = java.text.SimpleDateFormat("h:mm a")
            val selectedFormat = java.text.SimpleDateFormat("EEE, MMM d, h:mm a")
            val startTime = java.util.Date(item.startTime)
            val endTime = java.util.Date(item.endTime)
            val selectedTime = java.util.Date(item.selectedDT)
            binding.tvStartTime.text = timeFormat.format(startTime).toString()
            binding.tvEndTime.text = timeFormat.format(endTime).toString()
            binding.tvSelectedTime.text = selectedFormat.format(selectedTime).toString()
            binding.tvRepeatition.text = "${item.alarmOrNotification} / ${item.repeat}"
        }
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CreateAlertAdapter.CreateAlertViewHolder {
        return CreateAlertAdapter.CreateAlertViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.alert_view,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CreateAlertAdapter.CreateAlertViewHolder, position: Int) {
        holder.bind(alertList[position])
    }

    override fun getItemCount(): Int {
        return alertList.size
    }

    fun setList(alert: List<CreateAlert>) {
        alertList.clear()
        alertList.addAll(alert)
    }

    fun getAlertList(): ArrayList<CreateAlert> {
        return alertList
    }
}