package com.example.graduateproject.expense

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
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

    private val _monthlyExpenses = MutableLiveData<List<Expense>>()
    val monthlyExpenses: MutableLiveData<List<Expense>>
        get() = _monthlyExpenses

    fun getMonthlyTransaction(month: Int, Year: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _monthlyExpenses.postValue(expenseRepository.getMonthlyExpenses(month, Year))
        }
    }

    fun getYearlyTransaction(year: Int): List<Expense> = expenseRepository.getYearlyExpenses(year)
}