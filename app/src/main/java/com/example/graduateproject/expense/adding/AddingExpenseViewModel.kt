package com.example.graduateproject.expense.adding

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.graduateproject.expense.model.Expense
import com.example.graduateproject.expense.repository.ExpenseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class AddingExpenseViewModel @Inject constructor(
    application: Application,
    private val expenseRepository: ExpenseRepository
) : AndroidViewModel(application) {

    fun addExpense(transaction: Expense) = viewModelScope.launch(Dispatchers.IO) {
        expenseRepository.insertExpense(transaction)
    }

    fun deleteExpense(expense: Expense) = viewModelScope.launch(Dispatchers.IO) {
        expenseRepository.deleteExpense(expense)
    }

    fun updateExpense(transaction: Expense) = viewModelScope.launch(Dispatchers.IO) {
        expenseRepository.updateExpense(transaction)
    }
}