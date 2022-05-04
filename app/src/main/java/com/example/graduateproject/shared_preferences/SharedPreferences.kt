package com.example.graduateproject.shared_preferences

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

object SharedPreferences {
    private lateinit var preferences: SharedPreferences
    private const val MODE = Context.MODE_PRIVATE

    private const val PREF_NAME = "saved data user"

    private const val STUDY_WEEK_DAY = "week's day"
    private const val SWITCH_WEEK = "study week"
    private const val SWITCH_PERMISSION_NOTIFICATION = "study notification"
    private const val SAVED_PASSWORD = "password"
    private const val SAVED_MONTH_EXPENSE = "month budget"
    private const val EXPENSE_ON_BOARDING = "expenses on boarding"
    private const val SCHEDULE_ON_BOARDING = "schedule on boarding"
    private const val NOTE_ON_BOARDING = "note on boarding"

    fun init(context: Context) {
        preferences = context.getSharedPreferences(PREF_NAME, MODE)
    }

    var savedPassword: String?
        get() = preferences.getString(SAVED_PASSWORD, "")
        set(value) = preferences.edit {
            putString(SAVED_PASSWORD, value)
        }

    var savedWeekDay: String?
        get() = preferences.getString(STUDY_WEEK_DAY, "")
        set(value) = preferences.edit {
            putString(STUDY_WEEK_DAY, value)
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

    var saveMonthBudget: String?
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
}