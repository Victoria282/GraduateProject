package com.example.graduateproject.schedule.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "lessons")
data class Lesson(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo(name = "weekDay")
    val weekDay: String,
    @ColumnInfo(name = "lessonPosition")
    val lesson: Int,
    @ColumnInfo(name = "subject")
    val subject: String,
    @ColumnInfo(name = "teacher")
    val teacher: String,
    @ColumnInfo(name = "lessonType")
    val lessonType: Int,
    @ColumnInfo(name = "cabinet")
    val cabinet: String,
    @ColumnInfo(name = "studyWeek")
    val week: Boolean,
    @ColumnInfo(name = "startTime")
    val startTime: String,
    @ColumnInfo(name = "endTime")
    val endTime: String
) : Parcelable
