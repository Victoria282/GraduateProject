package com.example.graduateproject.expense.adding

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
import com.example.graduateproject.expense.model.Expense
import com.example.graduateproject.utils.Utils
import com.google.android.material.button.MaterialButton
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class AddingExpenseFragment @Inject constructor(
    viewModelFactory: ViewModelFactory
) : Fragment(R.layout.add_expense_layout), View.OnClickListener {
    private lateinit var binding: AddExpenseLayoutBinding
    private val viewModel: AddingExpenseViewModel by viewModels { viewModelFactory }
    private val args by navArgs<AddingExpenseFragmentArgs>()

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
        args.expenseItem?.let { setExpenseInfo() }
        datePicker()
        initListeners()
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

    private fun initListeners() = with(binding) {
        addExpense.setOnClickListener { addExpense() }
        delete.setOnClickListener {
            val expense = args.expenseItem
            expense?.let { viewModel.deleteExpense(expense) }
            backToExpenseFragment()
        }
        initCategoryButtons()
    }

    private fun initCategoryButtons() {
        binding.food.setOnClickListener(this)
        binding.shopping.setOnClickListener(this)
        binding.transport.setOnClickListener(this)
        binding.health.setOnClickListener(this)
        binding.others.setOnClickListener(this)
        binding.academics.setOnClickListener(this)
    }

    private fun addExpense() = with(binding) {
        val title = editTitle.text.toString()
        val amount = editMoney.text.toString()
        val note = editNote.text.toString()
        val date = editDate.text.toString()

        if (title == "" || amount == "" || date == "" || categoryExpense == "")
            Utils.showMessage(R.string.message_input_empty_fields, requireContext())
        else {
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
                viewModel.addExpense(expense)
            else
                viewModel.updateExpense(expense)

            backToExpenseFragment()
        }
    }

    private fun backToExpenseFragment() {
        val direction = AddingExpenseFragmentDirections.toExpenseFragment()
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

    private fun setExpenseInfo() = with(binding) {
        editTitle.setText(args.expenseItem?.title)
        editDate.setText((args.expenseItem?.date))
        editMoney.setText((args.expenseItem?.amount.toString()))
        editNote.setText((args.expenseItem?.note))

        categoryExpense = (args.expenseItem!!.category)

        when (categoryExpense) {
            "Еда" -> setCategory(food)
            "Покупки" -> setCategory(shopping)
            "Транспорт" -> setCategory(transport)
            "Здоровье" -> setCategory(health)
            "Другое" -> setCategory(others)
            "Учёба" -> setCategory(academics)
        }
    }

    private fun removeBackground(button: MaterialButton) = with(button) {
        setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.main_color_icon))
        setIconTintResource(R.color.white)
        setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
    }

    private fun setCategory(view: View?) = with(binding) {
        categoryExpense = (view as Button).text.toString()

        makeSelectedButton(view)

        when (view) {
            food -> {
                removeBackground(shopping)
                removeBackground(transport)
                removeBackground(health)
                removeBackground(others)
                removeBackground(academics)
            }
            shopping -> {
                removeBackground(food)
                removeBackground(transport)
                removeBackground(health)
                removeBackground(others)
                removeBackground(academics)
            }
            transport -> {
                removeBackground(shopping)
                removeBackground(food)
                removeBackground(health)
                removeBackground(others)
                removeBackground(academics)
            }
            health -> {
                removeBackground(shopping)
                removeBackground(transport)
                removeBackground(food)
                removeBackground(others)
                removeBackground(academics)
            }
            others -> {
                removeBackground(shopping)
                removeBackground(transport)
                removeBackground(health)
                removeBackground(food)
                removeBackground(academics)
            }
            academics -> {
                removeBackground(shopping)
                removeBackground(transport)
                removeBackground(health)
                removeBackground(others)
                removeBackground(food)
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