package com.ahmedg.cudweatherapp.presentation.view.ui

import android.content.Intent
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import androidx.preference.SwitchPreferenceCompat
import com.ahmedg.cudweatherapp.R
import com.ahmedg.cudweatherapp.helperclass.HelperClass
import java.util.*

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)

        val mapFragment: SwitchPreferenceCompat? = findPreference("deviceLocation")
        val weatherAlertFragment: Preference? = findPreference("customNotification")
        val languageSystem: Preference? = findPreference("languageSystem")
        mapFragment?.onPreferenceChangeListener =
            Preference.OnPreferenceChangeListener { _, newValue ->
                val isChecked = newValue as? Boolean ?: false
                if (isChecked) {
                    val action =
                        SettingsFragmentDirections.actionSettingsFragmentToMapsFragment(true)
                    view?.findNavController()?.navigate(action)
                }
                true
            }
        weatherAlertFragment?.onPreferenceClickListener = Preference.OnPreferenceClickListener {
            view?.findNavController()
                ?.navigate(R.id.action_settingsFragment_to_weatherAlertFragment)
            true
        }
        languageSystem?.onPreferenceChangeListener =
            Preference.OnPreferenceChangeListener { _, _ ->
                startActivity(Intent(requireContext(), MainActivity::class.java))
                true
            }
//        languageSystem?.onPreferenceClickListener = Preference.OnPreferenceClickListener {
//
//            val sp = PreferenceManager.getDefaultSharedPreferences(context)
//            val lang = sp.getString("languageSystem", Locale.getDefault().language)!!
//            if (lang == "ar") {
//                activity?.let {
//                    HelperClass.setLocale(it, "ar")
//                }
//                startActivity(Intent(requireContext(), MainActivity::class.java))
//            } else {
//                activity?.let {
//                    HelperClass.setLocale(it, "en")
//                }
//                startActivity(Intent(requireContext(), MainActivity::class.java))
//            }
//            true
//        }
    }

}