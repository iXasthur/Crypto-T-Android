package com.xcnk.cryptot

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import com.xcnk.cryptot.utils.Constants
import com.xcnk.cryptot.utils.SharedPreferencesAssistant
import com.zeugmasolutions.localehelper.LocaleAwareApplication
import com.zeugmasolutions.localehelper.Locales
import java.util.*

class App : LocaleAwareApplication() {

    override fun onCreate() {
        super.onCreate()
        when (SharedPreferencesAssistant.standard.getString(Constants.SharedPreferences.Standard.FORCED_THEME_KEY, "null")) {
            Constants.SharedPreferences.Standard.FORCED_THEME_LIGHT_VALUE -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            Constants.SharedPreferences.Standard.FORCED_THEME_DARK_VALUE -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
        }
    }

    init {
        instance = this
        systemLocale = Locale.getDefault()
    }

    companion object {
        private var systemLocale: Locale? = null
        private var instance: App? = null

        fun applicationContext(): Context {
            return instance!!.applicationContext
        }

        fun systemLocale(): Locale {
            return systemLocale ?: Locales.English
        }
    }

}