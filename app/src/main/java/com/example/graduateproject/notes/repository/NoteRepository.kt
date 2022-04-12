package com.example.graduateproject.notes.repository

import androidx.lifecycle.LiveData
import com.example.graduateproject.notes.model.Note

interface NoteRepository {
    val notes: LiveData<List<Note>>
    suspend fun insertNote(note: Note)
    suspend fun updateNote(note: Note)
    suspend fun deleteNote(note: Note)
}