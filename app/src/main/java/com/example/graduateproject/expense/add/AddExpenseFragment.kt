package com.example.graduateproject.expense.add

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

    var day = 0
    var month = 0
    var year = 0
    private var category = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = AddExpenseLayoutBinding.inflate(inflater, container, false)
        Locale.setDefault(APP_LOCALE_RU)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        args.expenseItem?.let {
            setExpenseInfo()
        }
        datePicker()
        initListeners()
    }

    private fun datePicker() = with(binding) {
        val cal = Calendar.getInstance(APP_LOCALE_RU)
        binding.editDate.setText(SimpleDateFormat("dd MMMM  yyyy").format(System.currentTimeMillis()))
        day = SimpleDateFormat("dd").format(System.currentTimeMillis()).toInt()
        month = SimpleDateFormat("MM").format(System.currentTimeMillis()).toInt()
        year = SimpleDateFormat("yyyy").format(System.currentTimeMillis()).toInt()
        val dateSetListener =
            DatePickerDialog.OnDateSetListener { _, Year, monthOfYear, dayOfMonth ->
                cal.set(Calendar.YEAR, Year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                var myFormat = "dd MMMM  yyyy"
                var sdf = SimpleDateFormat(myFormat, Locale.US)
                binding.editDate.setText(sdf.format(cal.time))
                myFormat = "dd"
                sdf = SimpleDateFormat(myFormat, Locale.US)
                day = sdf.format(cal.time).toInt()
                myFormat = "MM"
                sdf = SimpleDateFormat(myFormat, Locale.US)
                month = sdf.format(cal.time).toInt()
                myFormat = "yyyy"
                sdf = SimpleDateFormat(myFormat, Locale.US)
                year = sdf.format(cal.time).toInt()

            }

        binding.editDate.setOnClickListener {
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

        if (title == "" || amount == "" || note == "" || date == "" || category == "") {
            Utils.showMessage(R.string.message_input_empty_fields, requireContext())
        } else {
            val id = if (args.expenseItem == null) null else args.expenseItem!!.id
            val transaction = Expense(
                id = id,
                title = title,
                amount = amount.toDouble(),
                note = note,
                date = date,
                day = day,
                month = month,
                year = year,
                category = category
            )
            if (id == null) viewModel.addTransaction(transaction)
            else viewModel.updateTransaction(transaction)
            backToExpenseFragment()
        }
    }

    private fun backToExpenseFragment() {
        val direction = AddExpenseFragmentDirections.toExpenseFragment()
        findNavController().navigate(direction)
    }

    override fun onClick(btn: View?) {
        when (btn) {
            binding.food -> {
                setCategory(btn, binding.food)
            }
            binding.shopping -> {
                setCategory(btn, binding.shopping)
            }
            binding.transport -> {
                setCategory(btn, binding.transport)
            }
            binding.health -> {
                setCategory(btn, binding.health)
            }
            binding.others -> {
                setCategory(btn, binding.others)
            }
            binding.academics -> {
                setCategory(btn, binding.academics)
            }
        }
    }

    private fun setCategory(v: View, button: MaterialButton) {
        category = button.text.toString()
        button.setBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.button_not_clicked
            )
        )
        button.setIconTintResource(R.color.main_color_icon)
        button.setTextColor(ContextCompat.getColor(requireContext(), R.color.main_color_icon))

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

    private fun setExpenseInfo() {
        with(binding) {
            editTitle.setText(args.expenseItem?.title)
            editDate.setText((args.expenseItem?.date))
            editMoney.setText((args.expenseItem?.amount.toString()))
            editNote.setText((args.expenseItem?.note))
        }
        category = (args.expenseItem!!.category)

        when (category) {
            "Еда" -> {
                setCategory(binding.food, binding.food)
            }
            "Покупки" -> {
                setCategory(binding.shopping, binding.shopping)
            }
            "Транспорт" -> {
                setCategory(binding.transport, binding.transport)
            }
            "Здоровье" -> {
                setCategory(binding.health, binding.health)
            }
            "Другое" -> {
                setCategory(binding.others, binding.others)
            }
            "Учёба" -> {
                setCategory(binding.academics, binding.academics)
            }
        }
    }

    private fun removeBackground(button: MaterialButton) {
        button.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.main_color_icon))
        button.setIconTintResource(R.color.white)
        button.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
    }

    companion object {
        val APP_LOCALE_RU = Locale("ru")
    }
}