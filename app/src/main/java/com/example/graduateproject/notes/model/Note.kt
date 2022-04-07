package com.example.graduateproject.notes.model

import android.net.Uri
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "Notes")
data class Note(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    @ColumnInfo(name = "title")
    var title: String? = null,
    @ColumnInfo(name = "sub_title")
    var subTitle: String? = null,
    @ColumnInfo(name = "date_time")
    var dateTime: String? = null,
    @ColumnInfo(name = "note_text")
    var noteText: String? = null,
    @ColumnInfo(name = "img_path")
    var imgPath: String? = null
) : Parcelable