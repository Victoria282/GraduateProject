package com.example.graduateproject.expense

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.graduateproject.R
import com.example.graduateproject.databinding.FragmentExpenseLayoutBinding
import com.example.graduateproject.di.utils.ViewModelFactory
import com.example.graduateproject.expense.adapter.ExpenseAdapter
import com.example.graduateproject.expense.model.Expense
import kotlinx.android.synthetic.main.fragment_expense_layout.*
import org.eazegraph.lib.models.PieModel
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class ExpenseFragment @Inject constructor(
    viewModelFactory: ViewModelFactory
) : Fragment(R.layout.fragment_expense_layout), ExpenseAdapter.ExpenseClickListener {

    private val viewModel: ExpenseViewModel by viewModels { viewModelFactory }
    private lateinit var binding: FragmentExpenseLayoutBinding
    private lateinit var expenseAdapter: ExpenseAdapter

    private var totalExpense = 0.0
    private var totalGoal = 5000.0f
    private var totalFood = 0.0f
    private var totalShopping = 0.0f
    private var totalTransport = 0.0f
    private var totalHealth = 0.0f
    private var totalOthers = 0.0f
    private var totalAcademics = 0.0f

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentExpenseLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        initListeners()
        initObservers()
    }

    private fun initAdapter() {
        expenseAdapter = ExpenseAdapter(requireContext())
        expensesRecyclerView.setHasFixedSize(true)
        expensesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        expensesRecyclerView.adapter = expenseAdapter
        expenseAdapter.clickListener = this@ExpenseFragment
    }

    private fun initObservers() = with(viewModel) {
        val dayFormat = SimpleDateFormat("dd")
        val day = dayFormat.format(Calendar.getInstance().time)

        var format = SimpleDateFormat("MM")
        val currentMonth = format.format(Calendar.getInstance().time)
        format = SimpleDateFormat("yyyy")
        val currentYear = format.format(Calendar.getInstance().time)
        format = SimpleDateFormat("MMMM")
        binding.date.text = "$day ${format.format(Calendar.getInstance().time)} $currentYear"

        totalExpense = 0.0
        totalFood = 0.0f
        totalShopping = 0.0f
        totalTransport = 0.0f
        totalHealth = 0.0f
        totalOthers = 0.0f
        totalAcademics = 0.0f

        getMonthlyTransaction(currentMonth.toInt(), currentYear.toInt()).observe(
            viewLifecycleOwner,
            { expenses ->
                expenses.forEach {
                    expenseAdapter.updateExpenses(it)
                }

                for (i in expenses) {
                    totalExpense += i.amount
                    when (i.category) {
                        "Еда" -> {
                            totalFood += (i.amount.toFloat())
                        }
                        "Покупки" -> {
                            totalShopping += (i.amount.toFloat())
                        }
                        "Транспорт" -> {
                            totalTransport += (i.amount.toFloat())
                        }
                        "Здоровье" -> {
                            totalHealth += (i.amount.toFloat())
                        }
                        "Другое" -> {
                            totalOthers += (i.amount.toFloat())
                        }
                        "Учёба" -> {
                            totalAcademics += (i.amount.toFloat())
                        }
                    }
                }
                binding.expense.text = "${totalExpense.toInt()} ₽"
                binding.budget.text = "${totalGoal.toInt()} ₽"
                if (totalExpense > totalGoal) {
                    binding.indicator.setImageResource(R.drawable.ic_negative_balance)
                    binding.expense.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.error_color
                        )
                    )
                } else {
                    binding.indicator.setImageResource(R.drawable.ic_positive_balance)
                }
                showPiChart()
            })
    }

    private fun showPiChart() {
        val mPieChart = binding.expenseAnalytics

        mPieChart.addPieSlice(
            PieModel(
                "Еда",
                totalFood,
                ContextCompat.getColor(requireContext(), R.color.green_color)
            )
        )
        mPieChart.addPieSlice(
            PieModel(
                "Покупки",
                totalShopping,
                ContextCompat.getColor(requireContext(), R.color.blue_color)
            )
        )
        mPieChart.addPieSlice(
            PieModel(
                "Здоровье",
                totalHealth,
                ContextCompat.getColor(requireContext(), R.color.red_color)
            )
        )
        mPieChart.addPieSlice(
            PieModel(
                "Другое",
                totalOthers,
                ContextCompat.getColor(requireContext(), R.color.violet_color)
            )
        )
        mPieChart.addPieSlice(
            PieModel(
                "Транспорт",
                totalTransport,
                ContextCompat.getColor(requireContext(), R.color.yellow_color)
            )
        )
        mPieChart.addPieSlice(
            PieModel(
                "Учёба",
                totalAcademics,
                ContextCompat.getColor(requireContext(), R.color.orange_color)
            )
        )

        if (totalGoal > totalExpense) {
            mPieChart.addPieSlice(
                PieModel(
                    "Left",
                    totalGoal - (totalExpense.toFloat()),
                    ContextCompat.getColor(requireContext(), R.color.gray_light)
                )
            )
        }

        mPieChart.startAnimation()

    }

    private fun initListeners() = with(binding) {
        addNew.setOnClickListener {
            onExpenseClick(null)
        }
    }

    override fun onExpenseClick(expense: Expense?) {
        val direction = ExpenseFragmentDirections.toExpenseFragment(expense)
        findNavController().navigate(direction)
    }
}