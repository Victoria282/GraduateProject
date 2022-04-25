package com.example.graduateproject.expense

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.graduateproject.expense.model.Expense
import com.example.graduateproject.expense.repository.ExpenseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class ExpenseViewModel @Inject constructor(
    application: Application,
    private val expenseRepository: ExpenseRepository
) : AndroidViewModel(application) {

    fun addTransaction(transaction: Expense) {
        viewModelScope.launch(Dispatchers.IO) {
            expenseRepository.insertExpense(transaction)
        }
    }

    fun getTransaction(): LiveData<List<Expense>> = expenseRepository.getAllExpenses()

    fun getMonthlyTransaction(month: Int, Year: Int): LiveData<List<Expense>> =
        expenseRepository.getMonthlyExpenses(month, Year)

    fun getYearlyTransaction(year: Int): LiveData<List<Expense>> =
        expenseRepository.getYearlyExpenses(year)

    fun deleteTransaction(id: Int) {
        expenseRepository.deleteExpense(id)
    }

    fun updateTransaction(transaction: Expense) {
        viewModelScope.launch(Dispatchers.IO) {
            expenseRepository.updateExpense(transaction)
        }
    }
}