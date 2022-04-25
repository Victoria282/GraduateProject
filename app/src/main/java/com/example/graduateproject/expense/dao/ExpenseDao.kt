package com.example.graduateproject.expense.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.graduateproject.expense.model.Expense

@Dao
interface ExpenseDao {
    @Query("SELECT * FROM `Expense` ORDER BY year DESC, month DESC, day DESC, category DESC")
    fun getTransaction(): LiveData<List<Expense>>

    @Query("SELECT * FROM `Expense` WHERE month=:month AND year=:year")
    fun getMonthlyTransaction(month: Int, year: Int): LiveData<List<Expense>>

    @Query("SELECT * FROM `Expense` WHERE year=:year")
    fun getYearlyTransaction(year: Int): LiveData<List<Expense>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTransaction(transaction: Expense)

    @Query("DELETE FROM `Expense` WHERE id=:id")
    fun deleteTransaction(id: Int)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateTransaction(transaction: Expense)
}