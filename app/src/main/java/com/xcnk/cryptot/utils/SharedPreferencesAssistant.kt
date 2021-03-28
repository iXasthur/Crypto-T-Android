package com.xcnk.cryptot.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.xcnk.cryptot.App


object SharedPreferencesAssistant {
    val standard: SharedPreferences = App.applicationContext().getSharedPreferences(
        Constants.SharedPreferences.Standard.PREFERENCES_NAME,
        Context.MODE_PRIVATE
    )
    val encrypted: SharedPreferences = EncryptedSharedPreferences.create(
        Constants.SharedPreferences.Encrypted.PREFERENCES_NAME,
        MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC),
        App.applicationContext(),
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )
}