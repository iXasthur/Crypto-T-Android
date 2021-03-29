package com.xcnk.cryptot

import android.content.Context
import com.zeugmasolutions.localehelper.LocaleAwareApplication
import com.zeugmasolutions.localehelper.Locales
import java.util.*

class App : LocaleAwareApplication() {

    init {
        systemLocale = Locale.getDefault()
        instance = this
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