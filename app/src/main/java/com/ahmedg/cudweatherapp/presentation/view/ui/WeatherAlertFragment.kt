package com.ahmedg.cudweatherapp.presentation.view.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ahmedg.cudweatherapp.R
import com.ahmedg.cudweatherapp.databinding.FragmentWeatherAlertBinding
import com.ahmedg.cudweatherapp.model.CreateAlert
import com.ahmedg.cudweatherapp.model.WeatherResult
import com.ahmedg.cudweatherapp.presentation.view.adapters.CreateAlertAdapter
import com.ahmedg.cudweatherapp.presentation.view.adapters.WeatherFavouriteAdapter
import com.ahmedg.cudweatherapp.presentation.viewmodel.WeatherViewModel
import com.google.android.material.snackbar.Snackbar

class WeatherAlertFragment : Fragment() {
    private lateinit var binding: FragmentWeatherAlertBinding
    private lateinit var weatherViewModel: WeatherViewModel
    private lateinit var adapter: CreateAlertAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_weather_alert, container, false)
        weatherViewModel = ViewModelProvider(this).get(WeatherViewModel::class.java)
        binding.btnFabAlert.setOnClickListener {
            it.findNavController().navigate(R.id.action_weatherAlertFragment_to_createAlertFragment)
        }
        ItemTouchHelper(itemTouchHelper).apply {
            attachToRecyclerView(binding.rcAlert)
        }
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        initRecyclerView()
    }
    private val itemTouchHelper = object : ItemTouchHelper.SimpleCallback(
        0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
    ) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val position = viewHolder.adapterPosition
            val removeAt = adapter.getAlertList().removeAt(position)
            deleteAlert(removeAt)
            adapter.notifyDataSetChanged()
            Snackbar.make(requireView(), "Successfully deleted", Snackbar.LENGTH_LONG).apply {
                setAction("Undo") {
                    // Add To Room
                    insertAlert(removeAt)
                }
                show()
            }
        }
    }
    private fun initRecyclerView() {
        binding.rcAlert.layoutManager = LinearLayoutManager(context)
        adapter = CreateAlertAdapter()
        binding.rcAlert.adapter = adapter
        displayAlertList()
    }

    private fun displayAlertList() {
        weatherViewModel.alertList.observe(this, Observer {
            adapter.setList(it)
            adapter.notifyDataSetChanged()
        })
    }

    private fun insertAlert(createAlert: CreateAlert) {
        weatherViewModel.insertAlert(createAlert)
    }

    private fun deleteAlert(createAlert: CreateAlert) {
        weatherViewModel.deleteAlert(createAlert)
    }
}