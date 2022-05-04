package com.example.graduateproject.schedule.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.graduateproject.schedule.model.Lesson
import com.example.graduateproject.schedule.repository.ScheduleRepository
import com.example.graduateproject.shared_preferences.SharedPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class ScheduleViewModel @Inject constructor(
    application: Application,
    private val scheduleRepository: ScheduleRepository
) : AndroidViewModel(application) {

    val lessons: LiveData<List<Lesson>> = scheduleRepository.lessons

    private val _weekDay = MutableLiveData<String>()
    val weekDay: MutableLiveData<String>
        get() = _weekDay

    fun setWeekDay(dayOfWeek: String) {
        SharedPreferences.savedWeekDay = dayOfWeek
        _weekDay.postValue(dayOfWeek)
    }
}