package com.example.graduateproject

import androidx.room.Room
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.graduateproject.database.AppDatabase
import com.example.graduateproject.expense.model.Expense
import com.example.graduateproject.notes.model.Note
import com.example.graduateproject.schedule.model.Lesson
import com.example.graduateproject.utils.Constants.FOOD
import kotlinx.coroutines.runBlocking
import org.hamcrest.Matchers
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.hasSize
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DatabaseTest {
    private val db = Room.inMemoryDatabaseBuilder(
        InstrumentationRegistry.getInstrumentation().targetContext,
        AppDatabase::class.java
    ).build()

    private val lessonsTest = db.lessonDao()
    private val notesTest = db.noteDao()
    private val expensesTest = db.expenseDao()

    private val lesson = Lesson(
        id = 1,
        weekDay = "Понедельник",
        lesson = 1,
        subject = "Математический анализ",
        teacher = "Борель Л.В.",
        lessonType = 0,
        cabinet = "A17",
        week = true,
        startTime = "8:00",
        endTime = "9:40"
    )

    private val note = Note(
        id = 1,
        title = "Заметка",
        subTitle = "Подзаголовок",
        dateTime = "8:45",
        noteText = "Что-то сделать..",
        imgPath = null
    )

    private val expense = Expense(
        id = 1,
        category = FOOD,
        title = "Обед в столовой",
        amount = 125.0,
        date = "15.05.2022",
        day = 15,
        month = 5,
        year = 2022,
        note = "Заметка"
    )

    @Test
    fun lessonsTest() = runBlocking {
        assertThat(lessonsTest.getLessons(), Matchers.`is`(listOf()))

        lessonsTest.insertLesson(lesson)

        lessonsTest.getLessons().let {
            assertThat(it, hasSize(equalTo(1)))
            assertThat(it[0], equalTo(lesson))
        }

        lessonsTest.deleteLesson(lesson)

        assertThat(lessonsTest.getLessons(), Matchers.`is`(listOf()))
    }

    @Test
    fun notesTest() = runBlocking {
        assertThat(notesTest.getNotes(), Matchers.`is`(listOf()))

        notesTest.insertNote(note)

        notesTest.getNotes().let {
            assertThat(it, hasSize(equalTo(1)))
            assertThat(it[0], equalTo(note))
        }

        notesTest.deleteNote(note)

        assertThat(lessonsTest.getLessons(), Matchers.`is`(listOf()))
    }

    @Test
    fun expensesTest() = runBlocking {
        assertThat(expensesTest.getMonthlyTransaction(5, 2022), Matchers.`is`(listOf()))

        expensesTest.insertTransaction(expense)

        expensesTest.getMonthlyTransaction(5, 2022).let {
            assertThat(it, hasSize(equalTo(1)))
            assertThat(it[0], equalTo(expense))
        }

        expensesTest.deleteTransaction(expense)

        assertThat(expensesTest.getMonthlyTransaction(5, 2022), Matchers.`is`(listOf()))
    }
}
