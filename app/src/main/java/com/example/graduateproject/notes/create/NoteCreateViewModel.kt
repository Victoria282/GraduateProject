package com.example.graduateproject.notes.create

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.graduateproject.notes.model.Note
import com.example.graduateproject.notes.repository.NoteRepository
import com.example.graduateproject.schedule.database.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NoteCreateViewModel(application: Application) : AndroidViewModel(application) {
    private val noteRepository: NoteRepository
    val notes: LiveData<List<Note>>

    init {
        val noteDao = AppDatabase.invoke(application).noteDao()
        noteRepository = NoteRepository(noteDao)
        notes = noteRepository.notes
    }

    fun insertNote(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            noteRepository.insertNote(note)
        }
    }

    fun updateNote(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            noteRepository.updateNote(note)
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            noteRepository.deleteNote(note)
        }
    }
}