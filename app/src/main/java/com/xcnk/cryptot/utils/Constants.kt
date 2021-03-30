package com.xcnk.cryptot.utils

object Constants {
    object Api {
        object Firebase {
            const val assetsCollectionName = "assets"
            const val imagesFolderName = "images"
            const val videosFolderName = "videos"
        }
    }
    object SharedPreferences {
        object Standard {
            const val PREFERENCES_NAME = "standard"
            const val FORCED_THEME_KEY = "forced_theme"
            const val FORCED_THEME_LIGHT_VALUE = "forced_theme_light"
            const val FORCED_THEME_DARK_VALUE = "forced_theme_dark"
        }
        object Encrypted {
            const val PREFERENCES_NAME = "encrypted"
            const val EMAIL_KEY = "email"
            const val PASSWORD_KEY = "password"
        }
    }
}