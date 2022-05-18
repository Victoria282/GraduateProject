package com.example.graduateproject.notes.repository

import com.example.graduateproject.notes.model.Note

interface NoteRepository {
    suspend fun getNotes():List<Note>
    suspend fun insertNote(note: Note)
    suspend fun updateNote(note: Note)
    suspend fun deleteNote(note: Note)
}