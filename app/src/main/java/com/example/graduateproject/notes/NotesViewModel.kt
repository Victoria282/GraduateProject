package com.example.graduateproject.notes

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.graduateproject.notes.model.Note
import com.example.graduateproject.notes.repository.NoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class NotesViewModel @Inject constructor(
    application: Application,
    private val noteRepository: NoteRepository
) : AndroidViewModel(application) {
    private val _notes = MutableLiveData<List<Note>>()
    val notes: MutableLiveData<List<Note>>
        get() = _notes

    fun getNotes() = viewModelScope.launch(Dispatchers.IO) {
        _notes.postValue(noteRepository.getNotes())
    }
}