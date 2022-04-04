package com.example.graduateproject.schedule.repository

import androidx.lifecycle.LiveData
import com.example.graduateproject.schedule.database.LessonDao
import com.example.graduateproject.schedule.model.Lesson

class ScheduleRepository(
    private val lessonDao: LessonDao
) {
    val lessons: LiveData<List<Lesson>> = lessonDao.getLessons()

    suspend fun insertLesson(lesson: Lesson) {
        lessonDao.insertLesson(lesson)
    }

    suspend fun updateLesson(lesson: Lesson) {
        lessonDao.updateLesson(lesson)
    }

    suspend fun deleteLesson(lesson: Lesson) {
        lessonDao.deleteLesson(lesson)
    }
}