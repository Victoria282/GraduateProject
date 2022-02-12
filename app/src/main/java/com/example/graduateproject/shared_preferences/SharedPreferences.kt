package com.example.graduateproject.shared_preferences

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class SharedPreferences(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    var savedEmailUser: String?
        get() = sharedPreferences.getString(EMAIL, "")
        set(value) = sharedPreferences.edit { putString(EMAIL, value) }

    var savedPasswordUser: String?
        get() = sharedPreferences.getString(PASSWORD, "")
        set(value) = sharedPreferences.edit {putString(PASSWORD, value)}

    companion object {
        private const val PREF_NAME = "saved data user"
        private const val EMAIL = "email"
        private const val PASSWORD = "password"
    }
}