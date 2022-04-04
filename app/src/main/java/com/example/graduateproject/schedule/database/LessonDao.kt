package com.example.graduateproject.schedule.database;

import androidx.lifecycle.LiveData;
import androidx.room.*
import com.example.graduateproject.schedule.model.Lesson;

@Dao
interface LessonDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLessons(lessons: List<Lesson>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLesson(lesson: Lesson)

    @Query("SELECT * FROM lessons")
    fun getLessons(): LiveData<List<Lesson>>

    @Query("SELECT * FROM lessons WHERE 'id' = :id")
    fun getLesson(id: Int): LiveData<List<Lesson>>

    @Update
    suspend fun updateLesson(lesson: Lesson)

    @Delete
    suspend fun deleteLesson(lesson: Lesson)
}
