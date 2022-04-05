package com.example.graduateproject.schedule.database

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.graduateproject.schedule.model.Lesson
import com.example.graduateproject.schedule.repository.ScheduleRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DatabaseViewModel(application: Application) : AndroidViewModel(application) {
    private val lessonDao = AppDatabase.invoke(application).lessonDao()
    private val scheduleRepository: ScheduleRepository= ScheduleRepository(lessonDao)
    val lessons: LiveData<List<Lesson>> = scheduleRepository.lessons

    fun insertLesson(lesson: Lesson) {
        viewModelScope.launch(Dispatchers.IO) {
            scheduleRepository.insertLesson(lesson)
        }
    }

    fun updateLesson(lesson: Lesson) {
        viewModelScope.launch(Dispatchers.IO) {
            scheduleRepository.updateLesson(lesson)
        }
    }

    fun deleteLesson(lesson: Lesson) {
        viewModelScope.launch(Dispatchers.IO) {
            scheduleRepository.deleteLesson(lesson)
        }
    }
}