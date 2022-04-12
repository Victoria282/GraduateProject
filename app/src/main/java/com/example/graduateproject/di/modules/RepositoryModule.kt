package com.example.graduateproject.di.modules

import com.example.graduateproject.notes.repository.NoteRepository
import com.example.graduateproject.notes.repository.NoteRepositoryImpl
import com.example.graduateproject.schedule.repository.ScheduleRepository
import com.example.graduateproject.schedule.repository.ScheduleRepositoryImpl
import dagger.Binds
import dagger.Module

@Module
interface RepositoryModule {
    @Binds
    fun bindScheduleRepository(scheduleRepositoryImpl: ScheduleRepositoryImpl): ScheduleRepository

    @Binds
    fun bindNoteRepository(noteRepositoryImpl: NoteRepositoryImpl): NoteRepository
}