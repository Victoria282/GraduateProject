package com.example.graduateproject.schedule.repository

import com.example.graduateproject.schedule.model.Lesson

interface ScheduleRepository {
    suspend fun getLessons(): List<Lesson>
    suspend fun insertLesson(lesson: Lesson)
    suspend fun updateLesson(lesson: Lesson)
    suspend fun deleteLesson(lesson: Lesson)
    fun deleteSchedule()
}