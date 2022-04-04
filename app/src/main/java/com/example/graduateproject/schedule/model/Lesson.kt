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
    @ColumnInfo(name = "positionOfWeekDay")
    val positionOfWeekDay: Int,
    @ColumnInfo(name = "numberOfLesson")
    val numberOfLesson: Int,
    @ColumnInfo(name = "subject")
    val subject: String,
    @ColumnInfo(name = "teacher")
    val teacher: String,
    @ColumnInfo(name = "typeOfLesson")
    val typeOfLesson: Int,
    @ColumnInfo(name = "cabinet")
    val cabinet: String,
    @ColumnInfo(name = "countOfWeek")
    val countOfWeek: Int,
    @ColumnInfo(name = "startTime")
    val startTime: String,
    @ColumnInfo(name = "endTime")
    val endTime: String,
) : Parcelable
