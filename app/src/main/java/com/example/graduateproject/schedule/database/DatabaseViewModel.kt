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
    private val scheduleRepository: ScheduleRepository
    val lessons: LiveData<List<Lesson>>

    init {
        val lessonDao = AppDatabase.invoke(application).lessonDao()
        scheduleRepository = ScheduleRepository(lessonDao)
        lessons = scheduleRepository.lessons
    }

    fun insertLesson(lesson: Lesson) {
        viewModelScope.launch(Dispatchers.IO) {
            scheduleRepository.insertLesson(lesson)
        }
    }

    fun updateLesson(lesson: Lesson) {
        viewModelScope.launch(Dispatchers.IO) {
            val t = lesson.teacher
            scheduleRepository.updateLesson(lesson)
        }
    }

    fun deleteLesson(lesson: Lesson) {
        viewModelScope.launch(Dispatchers.IO) {
            scheduleRepository.deleteLesson(lesson)
        }
    }
}