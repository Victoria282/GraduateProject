package com.example.graduateproject.schedule.repository

import androidx.lifecycle.LiveData
import com.example.graduateproject.schedule.dao.LessonDao
import com.example.graduateproject.schedule.model.Lesson
import javax.inject.Inject

class ScheduleRepositoryImpl @Inject constructor(
    private val lessonDao: LessonDao
) : ScheduleRepository {
    override val lessons: LiveData<List<Lesson>> = lessonDao.getLessons()

    override suspend fun insertLesson(lesson: Lesson) {
        lessonDao.insertLesson(lesson)
    }

    override suspend fun updateLesson(lesson: Lesson) {
        lessonDao.updateLesson(lesson)
    }

    override suspend fun deleteLesson(lesson: Lesson) {
        lessonDao.deleteLesson(lesson)
    }
}