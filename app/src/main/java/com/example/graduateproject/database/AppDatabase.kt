package com.example.graduateproject.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.graduateproject.expense.dao.ExpenseDao
import com.example.graduateproject.expense.model.Expense
import com.example.graduateproject.notes.dao.NoteDao
import com.example.graduateproject.notes.model.Note
import com.example.graduateproject.schedule.dao.LessonDao
import com.example.graduateproject.schedule.model.Lesson

@Database(
    entities = [Lesson::class, Note::class, Expense::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun lessonDao(): LessonDao
    abstract fun noteDao(): NoteDao
    abstract fun expenseDao(): ExpenseDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        const val DATABASE_NAME = "STUDY_DATABASE"

        fun invoke(context: Context): AppDatabase {
            val temp = INSTANCE
            if (temp != null) {
                return temp
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DATABASE_NAME
                ).build()
                INSTANCE = instance
                return INSTANCE!!
            }
        }
    }
}