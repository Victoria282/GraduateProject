package com.example.graduateproject.expense.repository

import androidx.lifecycle.LiveData
import com.example.graduateproject.expense.model.Expense

interface ExpenseRepository {
    fun getAllExpenses(): LiveData<List<Expense>>
    fun getMonthlyExpenses(month: Int, Year: Int): LiveData<List<Expense>>
    fun getYearlyExpenses(year: Int): LiveData<List<Expense>>
    fun insertExpense(expense: Expense)
    fun deleteExpense(id: Int)
    fun updateExpense(exp: Expense)
}