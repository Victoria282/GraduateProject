package com.example.graduateproject.schedule.model

data class Lesson(
    val subject: String,
    val teacher: String,
    val typeOfLesson: Int,
    val cabinet: Int,
    val startTimeLesson: String,
    val endTimeLesson: String
)
