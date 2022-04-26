package com.example.graduateproject.expense.dao

import androidx.room.*
import com.example.graduateproject.expense.model.Expense

@Dao
interface ExpenseDao {
    @Query("SELECT * FROM `Expense` ORDER BY year DESC, month DESC, day DESC, category DESC")
    fun getTransaction(): List<Expense>

    @Query("SELECT * FROM `Expense` WHERE month=:month AND year=:year")
    fun getMonthlyTransaction(month: Int, year: Int): List<Expense>

    @Query("SELECT * FROM `Expense` WHERE year=:year")
    fun getYearlyTransaction(year: Int): List<Expense>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: Expense)

    @Delete
    suspend fun deleteTransaction(expense: Expense)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateTransaction(transaction: Expense)
}