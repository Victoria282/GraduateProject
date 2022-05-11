package com.example.graduateproject.shared_preferences

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.example.graduateproject.utils.Constants.EXPENSE_ON_BOARDING
import com.example.graduateproject.utils.Constants.NOTE_ON_BOARDING
import com.example.graduateproject.utils.Constants.RATE_US
import com.example.graduateproject.utils.Constants.SAVED_EMAIL
import com.example.graduateproject.utils.Constants.SAVED_MONTH_EXPENSE
import com.example.graduateproject.utils.Constants.SAVED_PASSWORD
import com.example.graduateproject.utils.Constants.SCHEDULE_ON_BOARDING
import com.example.graduateproject.utils.Constants.STUDY_WEEK
import com.example.graduateproject.utils.Constants.STUDY_WEEK_DAY
import com.example.graduateproject.utils.Constants.SWITCH_PERMISSION_NOTIFICATION
import com.example.graduateproject.utils.Constants.UPDATED_SAVED_PASSWORD
import com.example.graduateproject.utils.Constants.VISITING_APP

object Storage {
    private lateinit var preferences: SharedPreferences
    private const val MODE = Context.MODE_PRIVATE

    private const val PREF_NAME = "saved data"

    fun init(context: Context) {
        preferences = context.getSharedPreferences(PREF_NAME, MODE)
    }

    var password: String?
        get() = preferences.getString(SAVED_PASSWORD, "")
        set(value) = preferences.edit {
            putString(SAVED_PASSWORD, value)
        }

    var updatedPassword: String?
        get() = preferences.getString(UPDATED_SAVED_PASSWORD, "")
        set(value) = preferences.edit {
            putString(UPDATED_SAVED_PASSWORD, value)
        }

    var email: String?
        get() = preferences.getString(SAVED_EMAIL, "")
        set(value) = preferences.edit {
            putString(SAVED_EMAIL, value)
        }

    var weekDay: String?
        get() = preferences.getString(STUDY_WEEK_DAY, "")
        set(value) = preferences.edit {
            putString(STUDY_WEEK_DAY, value)
        }

    var studyWeek: Boolean
        get() = preferences.getBoolean(STUDY_WEEK, false)
        set(value) = preferences.edit {
            putBoolean(STUDY_WEEK, value)
        }

    var notificationSettings: Boolean
        get() = preferences.getBoolean(SWITCH_PERMISSION_NOTIFICATION, false)
        set(value) = preferences.edit {
            putBoolean(SWITCH_PERMISSION_NOTIFICATION, value)
        }

    var monthBudget: String?
        get() = preferences.getString(SAVED_MONTH_EXPENSE, "")
        set(value) = preferences.edit {
            putString(SAVED_MONTH_EXPENSE, value)
        }

    var expenseOnBoarding: Boolean
        get() = preferences.getBoolean(EXPENSE_ON_BOARDING, false)
        set(value) = preferences.edit {
            putBoolean(EXPENSE_ON_BOARDING, value)
        }

    var scheduleOnBoarding: Boolean
        get() = preferences.getBoolean(SCHEDULE_ON_BOARDING, false)
        set(value) = preferences.edit {
            putBoolean(SCHEDULE_ON_BOARDING, value)
        }

    var noteOnBoarding: Boolean
        get() = preferences.getBoolean(NOTE_ON_BOARDING, false)
        set(value) = preferences.edit {
            putBoolean(NOTE_ON_BOARDING, value)
        }

    var rateUs: Boolean
        get() = preferences.getBoolean(RATE_US, false)
        set(value) = preferences.edit {
            putBoolean(RATE_US, value)
        }

    var visitingApp: Int
        get() = preferences.getInt(VISITING_APP, 1)
        set(value) = preferences.edit {
            putInt(VISITING_APP, value)
        }
}