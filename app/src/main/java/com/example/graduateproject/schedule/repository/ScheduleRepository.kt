package com.example.graduateproject.schedule.repository

import androidx.lifecycle.LiveData
import com.example.graduateproject.schedule.database.LessonDao
import com.example.graduateproject.schedule.model.Lesson

class ScheduleRepository(private val lessonDao: LessonDao) {
    val listLessons: LiveData<List<Lesson>> = lessonDao.getLesson(1)

    /*suspend fun addUser(user_db:UserDB) {
        lessonDao.insertUser(user_db)
    }
    suspend fun getUser(id: Int) {
        lessonDao.getUser(id)
    }
    suspend fun updateUser(user: UserDB) {
        lessonDao.updateUser(user)
    }
    suspend fun deleteUser(user: UserDB) {
        lessonDao.deleteUser(user)
    }*/
}