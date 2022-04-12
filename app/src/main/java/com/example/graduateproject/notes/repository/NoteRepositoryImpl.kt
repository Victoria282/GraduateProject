package com.example.graduateproject.notes.repository

import androidx.lifecycle.LiveData
import com.example.graduateproject.notes.dao.NoteDao
import com.example.graduateproject.notes.model.Note
import javax.inject.Inject

class NoteRepositoryImpl @Inject constructor(
    private val noteDao: NoteDao
): NoteRepository {
    override val notes: LiveData<List<Note>> = noteDao.getNotes()

    override suspend fun insertNote(note: Note) {
        noteDao.insertNotes(note)
    }

    override suspend fun updateNote(note: Note) {
        noteDao.updateNote(note)
    }

    override suspend fun deleteNote(note: Note) {
        noteDao.deleteNote(note)
    }
}