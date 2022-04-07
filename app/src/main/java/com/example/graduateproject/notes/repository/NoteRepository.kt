package com.example.graduateproject.notes.repository

import androidx.lifecycle.LiveData
import com.example.graduateproject.notes.dao.NoteDao
import com.example.graduateproject.notes.model.Note

class NoteRepository(
    private val noteDao: NoteDao
) {
    val notes: LiveData<List<Note>> = noteDao.getNotes()

    suspend fun insertNote(note: Note) {
        noteDao.insertNotes(note)
    }

    suspend fun updateNote(note: Note) {
        noteDao.updateNote(note)
    }

    suspend fun deleteNote(note: Note) {
        noteDao.deleteNote(note)
    }
}