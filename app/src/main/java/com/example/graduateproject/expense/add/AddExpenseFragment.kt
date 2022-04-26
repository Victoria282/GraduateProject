package com.example.graduateproject.expense.add

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.graduateproject.R
import com.example.graduateproject.databinding.AddExpenseLayoutBinding
import com.example.graduateproject.di.utils.ViewModelFactory
import com.example.graduateproject.expense.ExpenseViewModel
import com.example.graduateproject.expense.model.Expense
import com.example.graduateproject.utils.Utils
import com.google.android.material.button.MaterialButton
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class AddExpenseFragment @Inject constructor(
    viewModelFactory: ViewModelFactory
) : Fragment(R.layout.add_expense_layout), View.OnClickListener {
    private lateinit var binding: AddExpenseLayoutBinding
    private val viewModel: ExpenseViewModel by viewModels { viewModelFactory }
    private val args by navArgs<AddExpenseFragmentArgs>()

    private var day = 0
    private var month = 0
    private var year = 0
    private var categoryExpense = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = AddExpenseLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Locale.setDefault(APP_LOCALE_RU)
        args.expenseItem?.let {
            setExpenseInfo()
        }
        datePicker()
        initListeners()
        binding.delete.setOnClickListener {
            val expense = args.expenseItem
            expense?.let { viewModel.deleteTransaction(expense) }
            backToExpenseFragment()
        }
    }

    private fun datePicker() = with(binding) {
        val cal = Calendar.getInstance(APP_LOCALE_RU)

        editDate.setText(dateFormat.format(System.currentTimeMillis()))

        day = dayFormat.format(System.currentTimeMillis()).toInt()
        month = monthFormat.format(System.currentTimeMillis()).toInt()
        year = yearFormat.format(System.currentTimeMillis()).toInt()

        val dateSetListener =
            DatePickerDialog.OnDateSetListener { _, Year, monthOfYear, dayOfMonth ->
                cal.set(Calendar.YEAR, Year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                editDate.setText(dateFormat.format(cal.time))

                day = dayFormat.format(cal.time).toInt()
                month = monthFormat.format(cal.time).toInt()
                year = yearFormat.format(cal.time).toInt()
            }

        editDate.setOnClickListener {
            DatePickerDialog(
                requireContext(), dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    private fun initListeners() {
        binding.food.setOnClickListener(this)
        binding.shopping.setOnClickListener(this)
        binding.transport.setOnClickListener(this)
        binding.health.setOnClickListener(this)
        binding.others.setOnClickListener(this)
        binding.academics.setOnClickListener(this)

        binding.addExpense.setOnClickListener { addExpense() }
    }

    private fun addExpense() {
        val title = binding.editTitle.text.toString()
        val amount = binding.editMoney.text.toString()
        val note = binding.editNote.text.toString()
        val date = binding.editDate.text.toString()

        if (title == "" || amount == "" || date == "" || categoryExpense == "") {
            Utils.showMessage(R.string.message_input_empty_fields, requireContext())
        } else {
            val id = if (args.expenseItem == null) null else args.expenseItem!!.id

            val expense = Expense(
                id = id,
                title = title,
                amount = amount.toDouble(),
                note = note,
                date = date,
                day = day,
                month = month,
                year = year,
                category = categoryExpense
            )
            if (id == null)
                viewModel.addTransaction(expense)
            else
                viewModel.updateTransaction(expense)

            backToExpenseFragment()
        }
    }

    private fun backToExpenseFragment() {
        val direction = AddExpenseFragmentDirections.toExpenseFragment()
        findNavController().navigate(direction)
    }

    override fun onClick(btn: View?) {
        setCategory(btn)
    }

    private fun makeSelectedButton(v: View?) = with((v as MaterialButton)) {
        setBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.light_gray
            )
        )
        setIconTintResource(R.color.main_color_icon)
        setTextColor(ContextCompat.getColor(requireContext(), R.color.main_color_icon))
    }

    private fun setExpenseInfo() {
        with(binding) {
            editTitle.setText(args.expenseItem?.title)
            editDate.setText((args.expenseItem?.date))
            editMoney.setText((args.expenseItem?.amount.toString()))
            editNote.setText((args.expenseItem?.note))
        }
        categoryExpense = (args.expenseItem!!.category)

        when (categoryExpense) {
            "Еда" -> setCategory(binding.food)
            "Покупки" -> setCategory(binding.shopping)
            "Транспорт" -> setCategory(binding.transport)
            "Здоровье" -> setCategory(binding.health)
            "Другое" -> setCategory(binding.others)
            "Учёба" -> setCategory(binding.academics)
        }
    }

    private fun removeBackground(button: MaterialButton) = with(button) {
        setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.main_color_icon))
        setIconTintResource(R.color.white)
        setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
    }

    private fun setCategory(v: View?) {
        categoryExpense = (v as Button).text.toString()

        makeSelectedButton(v)

        when (v) {
            binding.food -> {
                removeBackground(binding.shopping)
                removeBackground(binding.transport)
                removeBackground(binding.health)
                removeBackground(binding.others)
                removeBackground(binding.academics)
            }
            binding.shopping -> {
                removeBackground(binding.food)
                removeBackground(binding.transport)
                removeBackground(binding.health)
                removeBackground(binding.others)
                removeBackground(binding.academics)
            }
            binding.transport -> {
                removeBackground(binding.shopping)
                removeBackground(binding.food)
                removeBackground(binding.health)
                removeBackground(binding.others)
                removeBackground(binding.academics)
            }
            binding.health -> {
                removeBackground(binding.shopping)
                removeBackground(binding.transport)
                removeBackground(binding.food)
                removeBackground(binding.others)
                removeBackground(binding.academics)
            }
            binding.others -> {
                removeBackground(binding.shopping)
                removeBackground(binding.transport)
                removeBackground(binding.health)
                removeBackground(binding.food)
                removeBackground(binding.academics)
            }
            binding.academics -> {
                removeBackground(binding.shopping)
                removeBackground(binding.transport)
                removeBackground(binding.health)
                removeBackground(binding.others)
                removeBackground(binding.food)
            }
        }
    }

    companion object {
        val APP_LOCALE_RU = Locale("ru")

        val dayFormat = SimpleDateFormat("dd", APP_LOCALE_RU)
        var monthFormat = SimpleDateFormat("MM", APP_LOCALE_RU)
        val yearFormat = SimpleDateFormat("yyyy", APP_LOCALE_RU)
        val dateFormat = SimpleDateFormat("dd MMMM  yyyy", APP_LOCALE_RU)
    }
}