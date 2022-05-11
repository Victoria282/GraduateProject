package com.example.graduateproject.schedule.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.graduateproject.schedule.model.Lesson
import com.example.graduateproject.schedule.repository.ScheduleRepository
import com.example.graduateproject.shared_preferences.Storage
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class ScheduleViewModel @Inject constructor(
    application: Application,
    private val scheduleRepository: ScheduleRepository
) : AndroidViewModel(application) {

    private val _lessons = MutableLiveData<List<Lesson>?>()
    val lessons: MutableLiveData<List<Lesson>?>
        get() = _lessons

    private val _weekDay = MutableLiveData<String>()
    val weekDay: MutableLiveData<String>
        get() = _weekDay

    fun setWeekDay(dayOfWeek: String) {
        Storage.weekDay = dayOfWeek
        _weekDay.postValue(dayOfWeek)
    }

    fun getLessons() = viewModelScope.launch(Dispatchers.IO) {
        val result = scheduleRepository.getLessons()
        _lessons.postValue(result)
    }

    fun deleteSchedule() = viewModelScope.launch(Dispatchers.IO) {
        scheduleRepository.deleteSchedule()
        _lessons.postValue(emptyList())
    }
}