package com.example.graduateproject.shared_preferences

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

object SharedPreferences {
    private lateinit var preferences: SharedPreferences
    private const val MODE = Context.MODE_PRIVATE

    private const val PREF_NAME = "saved data user"

    private const val STUDY_WEEK_DAY = "day of week"
    private const val SWITCH_WEEK = "switch week"
    private const val SWITCH_PERMISSION_NOTIFICATION = "switch permission notification"
    private const val SWITCH_STUDY_MODE = "switch study mode"
    private const val SAVED_PASSWORD = "user password"

    fun init(context: Context) {
        preferences = context.getSharedPreferences(PREF_NAME, MODE)
    }

    var savedPassword: String?
        get() = preferences.getString(SAVED_PASSWORD, "")
        set(value) = preferences.edit {
            putString(SAVED_PASSWORD, value)
        }

    var savedWeekDay: Int
        get() = preferences.getInt(STUDY_WEEK_DAY, 1)
        set(value) = preferences.edit {
            putInt(STUDY_WEEK_DAY, value)
        }

    var saveSwitchWeek: Boolean
        get() = preferences.getBoolean(SWITCH_WEEK, false)
        set(value) = preferences.edit {
            putBoolean(SWITCH_WEEK, value)
        }

    var savePermissionNotification: Boolean
        get() = preferences.getBoolean(SWITCH_PERMISSION_NOTIFICATION, false)
        set(value) = preferences.edit {
            putBoolean(SWITCH_PERMISSION_NOTIFICATION, value)
        }

    var saveStudyMode: Boolean
        get() = preferences.getBoolean(SWITCH_STUDY_MODE, false)
        set(value) = preferences.edit {
            putBoolean(SWITCH_STUDY_MODE, value)
        }
}