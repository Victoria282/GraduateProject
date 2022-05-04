package com.example.graduateproject.notes

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.graduateproject.notes.model.Note
import com.example.graduateproject.notes.repository.NoteRepository
import javax.inject.Inject

class NotesViewModel @Inject constructor(
    application: Application,
    noteRepository: NoteRepository
) : AndroidViewModel(application) {
    val notes: LiveData<List<Note>> = noteRepository.notes
}