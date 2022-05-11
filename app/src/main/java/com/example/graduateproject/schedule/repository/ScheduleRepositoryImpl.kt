package com.example.graduateproject.schedule.repository

import com.example.graduateproject.schedule.dao.LessonDao
import com.example.graduateproject.schedule.model.Lesson
import javax.inject.Inject

class ScheduleRepositoryImpl @Inject constructor(
    private val lessonDao: LessonDao
) : ScheduleRepository {
    override suspend fun getLessons(): List<Lesson> {
        return lessonDao.getLessons()
    }

    override suspend fun insertLesson(lesson: Lesson) {
        lessonDao.insertLesson(lesson)
    }

    override suspend fun updateLesson(lesson: Lesson) {
        lessonDao.updateLesson(lesson)
    }

    override suspend fun deleteLesson(lesson: Lesson) {
        lessonDao.deleteLesson(lesson)
    }

    override fun deleteSchedule() {
        lessonDao.deleteSchedule()
    }
}