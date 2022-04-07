package com.example.graduateproject.notes.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.graduateproject.notes.model.Note

@Dao
interface NoteDao {
    @Query("SELECT * FROM notes ORDER BY id DESC")
    fun getNotes(): LiveData<List<Note>>

    @Query("SELECT * FROM notes WHERE id =:id")
    suspend fun getNote(id: Int): Note

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotes(note: Note)

    @Delete
    suspend fun deleteNote(note: Note)

    @Update
    suspend fun updateNote(note: Note)
}