package com.xcnk.cryptot.api

import com.xcnk.cryptot.models.auth.AuthData
import com.xcnk.cryptot.utils.Constants
import com.xcnk.cryptot.utils.SharedPreferencesAssistant

object AuthDataStorage {
    private val preferences = SharedPreferencesAssistant.encrypted

    fun save(authData: AuthData) {
        with(preferences.edit()) {
            putString(Constants.SharedPreferences.Encrypted.EMAIL_KEY, authData.email)
            putString(Constants.SharedPreferences.Encrypted.PASSWORD_KEY, authData.password)
            commit()
        }
    }

    fun delete() {
        with(preferences.edit()) {
            remove(Constants.SharedPreferences.Encrypted.EMAIL_KEY)
            remove(Constants.SharedPreferences.Encrypted.PASSWORD_KEY)
            commit()
        }
    }

    fun restore(): AuthData? {
        val email = preferences.getString(Constants.SharedPreferences.Encrypted.EMAIL_KEY, null)
        val password = preferences.getString(Constants.SharedPreferences.Encrypted.PASSWORD_KEY, null)
        if (email != null && password != null) {
            return AuthData(email, password)
        }
        return null
    }
}