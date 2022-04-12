package com.example.graduateproject.di.modules

import android.app.Application
import androidx.room.Room
import com.example.graduateproject.database.AppDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(app: Application): AppDatabase = Room
        .databaseBuilder(app, AppDatabase::class.java, AppDatabase.DATABASE_NAME)
        .build()

    @Singleton
    @Provides
    fun provideLessonDao(appDatabase: AppDatabase) = appDatabase.lessonDao()

    @Singleton
    @Provides
    fun provideNotesDao(appDatabase: AppDatabase) = appDatabase.noteDao()
}
