package com.example.graduateproject.expense.repository

import com.example.graduateproject.expense.model.Expense

interface ExpenseRepository {
    fun getMonthlyExpenses(month: Int, Year: Int): List<Expense>
    fun getYearlyExpenses(year: Int): List<Expense>
    suspend fun insertExpense(expense: Expense)
    suspend fun deleteExpense(expense: Expense)
    suspend fun updateExpense(exp: Expense)
}