package com.example.graduateproject.notes.repository

import androidx.lifecycle.MutableLiveData
import com.example.graduateproject.notes.dao.NoteDao
import com.example.graduateproject.notes.model.Note
import javax.inject.Inject

class NoteRepositoryImpl @Inject constructor(
    private val noteDao: NoteDao
) : NoteRepository {

    override suspend fun getNotes(): List<Note> {
        return noteDao.getNotes()
    }

    override suspend fun insertNote(note: Note) {
        noteDao.insertNote(note)
    }

    override suspend fun updateNote(note: Note) {
        noteDao.updateNote(note)
    }

    override suspend fun deleteNote(note: Note) {
        noteDao.deleteNote(note)
    }
}