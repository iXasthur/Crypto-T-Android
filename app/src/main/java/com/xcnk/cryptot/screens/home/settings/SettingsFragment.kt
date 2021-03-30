package com.xcnk.cryptot.screens.home.settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatDelegate
import com.xcnk.cryptot.App
import com.xcnk.cryptot.R
import com.xcnk.cryptot.api.AuthDataStorage
import com.xcnk.cryptot.api.Session
import com.xcnk.cryptot.screens.MyActivity
import com.xcnk.cryptot.utils.Constants
import com.xcnk.cryptot.utils.SharedPreferencesAssistant
import com.zeugmasolutions.localehelper.Locales


class SettingsFragment : Fragment() {

    private lateinit var themeSystemButton: Button
    private lateinit var themeLightButton: Button
    private lateinit var themeDarkButton: Button

    private lateinit var languageSystemButton: Button
    private lateinit var languageEnglishButton: Button
    private lateinit var languageRussianButton: Button

    private lateinit var logOutButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_settings, container, false)
        initViewObjects(view)
        setupViewObjects()
        return view
    }

    private fun initViewObjects(view: View) {
        themeSystemButton = view.findViewById(R.id.fragment_settings_button_theme_system)
        themeLightButton = view.findViewById(R.id.fragment_settings_button_theme_light)
        themeDarkButton = view.findViewById(R.id.fragment_settings_button_theme_dark)
        languageSystemButton = view.findViewById(R.id.fragment_settings_button_language_system)
        languageEnglishButton = view.findViewById(R.id.fragment_settings_button_language_english)
        languageRussianButton = view.findViewById(R.id.fragment_settings_button_language_russian)
        logOutButton = view.findViewById(R.id.fragment_settings_button_log_out)
    }

    private fun setupViewObjects() {
        setupButtonListeners()
    }

    private fun setupButtonListeners() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            requireActivity().finishAffinity()
        }

        themeSystemButton.setOnClickListener {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            with(SharedPreferencesAssistant.standard.edit()) {
                remove(Constants.SharedPreferences.Standard.FORCED_THEME_KEY)
                apply()
            }
        }

        themeLightButton.setOnClickListener {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            with(SharedPreferencesAssistant.standard.edit()) {
                putString(Constants.SharedPreferences.Standard.FORCED_THEME_KEY, Constants.SharedPreferences.Standard.FORCED_THEME_LIGHT_VALUE)
                apply()
            }
        }

        themeDarkButton.setOnClickListener {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            with(SharedPreferencesAssistant.standard.edit()) {
                putString(Constants.SharedPreferences.Standard.FORCED_THEME_KEY, Constants.SharedPreferences.Standard.FORCED_THEME_DARK_VALUE)
                apply()
            }
        }

        languageSystemButton.setOnClickListener {
            (requireActivity() as MyActivity).updateLocale(App.systemLocale())
        }

        languageEnglishButton.setOnClickListener {
            (requireActivity() as MyActivity).updateLocale(Locales.English)
        }

        languageRussianButton.setOnClickListener {
            (requireActivity() as MyActivity).updateLocale(Locales.Russian)
        }

        logOutButton.setOnClickListener {
            Session.destroy()
            requireActivity().finish()
        }
    }

}