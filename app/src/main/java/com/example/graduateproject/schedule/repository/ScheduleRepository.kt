package com.example.graduateproject.schedule.repository

import androidx.lifecycle.LiveData
import com.example.graduateproject.schedule.model.Lesson

interface ScheduleRepository {
    val lessons: LiveData<List<Lesson>>
    suspend fun insertLesson(lesson: Lesson)
    suspend fun updateLesson(lesson: Lesson)
    suspend fun deleteLesson(lesson: Lesson)
}