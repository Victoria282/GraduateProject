package com.example.graduateproject.schedule.lessons.usecase

import com.example.graduateproject.schedule.model.Lesson

object FakeLessons {
    val fakeData1week = arrayListOf(
        Lesson(
            1,
            1,
            "Матанализ",
            "Борель Лидия Викторовна",
            1,
            "422",
            1
        ),
        Lesson(
            2,
            2,
            "Математическая статистика",
            "Борель Лидия Викторовна",
            0,
            "A15",
            1
        ),
        Lesson(
            3,
            3,
            "Программирование на языке Java",
            "Изергин Дмитрий Борисович",
            1,
            "A17",
            1
        ),
    )

    val fakeData2week = arrayListOf(
        Lesson(
            1,
            1,
            "Эконометрика",
            "Сбродова Елена Александровна",
            0,
            "А17",
            2
        ),
        Lesson(
            2,
            2,
            "Эконометрика",
            "Сбродова Елена Александровна",
            1,
            "A15",
            2
        ),
        Lesson(
            3,
            3,
            "Эконометрика",
            "Сбродова Елена Александровна",
            0,
            "A17",
            2
        ),
    )
}