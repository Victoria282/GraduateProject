package com.example.graduateproject.expense.repository

import com.example.graduateproject.expense.dao.ExpenseDao
import com.example.graduateproject.expense.model.Expense
import javax.inject.Inject

class ExpenseRepositoryImpl @Inject constructor(
    private val expenseDao: ExpenseDao
) : ExpenseRepository {
    override fun getAllExpenses(): List<Expense> {
        return expenseDao.getTransaction()
    }

    override fun getMonthlyExpenses(month: Int, Year: Int): List<Expense> {
        return expenseDao.getMonthlyTransaction(month, Year)
    }

    override fun getYearlyExpenses(year: Int): List<Expense> {
        return expenseDao.getYearlyTransaction(year)
    }

    override suspend fun insertExpense(expense: Expense) {
        expenseDao.insertTransaction(expense)
    }

    override suspend fun deleteExpense(expense: Expense) {
        expenseDao.deleteTransaction(expense)
    }

    override suspend fun updateExpense(exp: Expense) {
        expenseDao.updateTransaction(exp)
    }
}