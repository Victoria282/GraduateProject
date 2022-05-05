package com.example.graduateproject.schedule.editor

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.graduateproject.schedule.model.Lesson
import com.example.graduateproject.schedule.repository.ScheduleRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class LessonsEditorViewModel @Inject constructor(
    application: Application,
    private val scheduleRepository: ScheduleRepository
) : AndroidViewModel(application) {
    fun insertLesson(lesson: Lesson) = viewModelScope.launch(Dispatchers.IO) {
        scheduleRepository.insertLesson(lesson)
    }

    fun updateLesson(lesson: Lesson) = viewModelScope.launch(Dispatchers.IO) {
        scheduleRepository.updateLesson(lesson)
    }

    fun deleteLesson(lesson: Lesson) = viewModelScope.launch(Dispatchers.IO) {
        scheduleRepository.deleteLesson(lesson)
    }
}