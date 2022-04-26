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
    private const val SAVED_MONTH_EXPENSE = "month expenses"
    private const val EXPENSE_ONBOARDING = "show onBoarding in expenses"
    private const val SCHEDULE_ONBOARDING = "show onBoarding in schedule"
    private const val NOTE_ONBOARDING = "show onBoarding in note"

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

    var saveStudyMode: Boolean
        get() = preferences.getBoolean(SWITCH_STUDY_MODE, false)
        set(value) = preferences.edit {
            putBoolean(SWITCH_STUDY_MODE, value)
        }

    var saveExpenseMonth: String?
        get() = preferences.getString(SAVED_MONTH_EXPENSE, "")
        set(value) = preferences.edit {
            putString(SAVED_MONTH_EXPENSE, value)
        }

    var expenseOnBoarding: Boolean
        get() = preferences.getBoolean(EXPENSE_ONBOARDING, false)
        set(value) = preferences.edit {
            putBoolean(EXPENSE_ONBOARDING, value)
        }

    var scheduleOnBoarding: Boolean
        get() = preferences.getBoolean(SCHEDULE_ONBOARDING, false)
        set(value) = preferences.edit {
            putBoolean(SCHEDULE_ONBOARDING, value)
        }

    var noteOnBoarding: Boolean
        get() = preferences.getBoolean(NOTE_ONBOARDING, false)
        set(value) = preferences.edit {
            putBoolean(NOTE_ONBOARDING, value)
        }
}