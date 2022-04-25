package com.example.graduateproject.expense.repository

import androidx.lifecycle.LiveData
import com.example.graduateproject.expense.dao.ExpenseDao
import com.example.graduateproject.expense.model.Expense
import javax.inject.Inject

class ExpenseRepositoryImpl @Inject constructor(
    private val expenseDao: ExpenseDao
) : ExpenseRepository {
    override fun getAllExpenses(): LiveData<List<Expense>> {
        return expenseDao.getTransaction()
    }

    override fun getMonthlyExpenses(month: Int, Year: Int): LiveData<List<Expense>> {
        return expenseDao.getMonthlyTransaction(month, Year)
    }

    override fun getYearlyExpenses(year: Int): LiveData<List<Expense>> {
        return expenseDao.getYearlyTransaction(year)
    }

    override fun insertExpense(expense: Expense) {
        expenseDao.insertTransaction(expense)
    }

    override fun deleteExpense(id: Int) {
        expenseDao.deleteTransaction(id)
    }

    override fun updateExpense(exp: Expense) {
        expenseDao.updateTransaction(exp)
    }
}