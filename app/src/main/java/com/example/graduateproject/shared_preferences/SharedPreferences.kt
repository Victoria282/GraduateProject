package com.example.graduateproject.shared_preferences

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

object SharedPreferences {
    private lateinit var preferences: SharedPreferences
    private const val MODE = Context.MODE_PRIVATE

    private const val PREF_NAME = "saved data user"
    private const val STUDY_WEEK = "study week"
    private const val STUDY_WEEK_DAY = "day of week"

    fun init(context: Context) {
        preferences = context.getSharedPreferences(PREF_NAME, MODE)
    }

    var savedStudyWeek: Int
        get() = preferences.getInt(STUDY_WEEK, 1)
        set(value) = preferences.edit {
            putInt(STUDY_WEEK, value)
        }
    var savedWeekDay: Int
        get() = preferences.getInt(STUDY_WEEK_DAY, 1)
        set(value) = preferences.edit {
            putInt(STUDY_WEEK_DAY, value)
        }
}