package com.example.graduateproject.expense

import android.os.Bundle
import android.view.*
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.graduateproject.R
import com.example.graduateproject.databinding.FragmentExpenseLayoutBinding
import com.example.graduateproject.di.utils.ViewModelFactory
import com.example.graduateproject.expense.adapter.ExpenseAdapter
import com.example.graduateproject.expense.model.Expense
import com.example.graduateproject.shared_preferences.SharedPreferences
import com.example.graduateproject.utils.Utils
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.bottom_sheet_layout.*
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

    private var totalFood = 0.0f
    private var totalShopping = 0.0f
    private var totalTransport = 0.0f
    private var totalHealth = 0.0f
    private var totalOthers = 0.0f
    private var totalAcademics = 0.0f

    private var monthlyExpensesObserver = Observer<List<Expense>> { listExpenses ->
        resetResult()

        if (listExpenses.isNotEmpty()) {
            binding.recentlyExpenses.visibility = View.VISIBLE

            listExpenses.forEach { expense ->
                if (!expenseAdapter.expensesList.contains(expense))
                    expenseAdapter.updateExpenses(expense)

                totalExpense += expense.amount

                when (expense.category) {
                    "Еда" -> totalFood += expense.amount.toFloat()
                    "Покупки" -> totalShopping += expense.amount.toFloat()
                    "Транспорт" -> totalTransport += expense.amount.toFloat()
                    "Здоровье" -> totalHealth += expense.amount.toFloat()
                    "Другое" -> totalOthers += expense.amount.toFloat()
                    "Учёба" -> totalAcademics += expense.amount.toFloat()
                }
            }
            showCurrentExpenses()
            checkMonthBudget()
            showPiChart()
        }
    }

    private fun resetResult() {
        binding.expenseAnalytics.clearChart()

        totalExpense = 0.0

        totalFood = 0.0f
        totalShopping = 0.0f
        totalTransport = 0.0f
        totalHealth = 0.0f
        totalOthers = 0.0f
        totalAcademics = 0.0f
    }

    private fun showCurrentExpenses() = with(binding) {
        expense.text = getString(R.string.expense_month_current, totalExpense.toString())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentExpenseLayoutBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu.findItem(R.id.switchWeek).isVisible = false
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.edit, menu);
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        if (item.itemId == R.id.editExpense) showBottomNavigation()
        return super.onOptionsItemSelected(item)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (!SharedPreferences.expenseOnBoarding) showOnBoarding()
        initUi()
        initAdapter()
        initListeners()
        initDate()
        initObservers()
    }

    private fun initUi() = with(binding) {
        budget.text =
            if (SharedPreferences.saveMonthBudget == "") "0 ₽" else SharedPreferences.saveMonthBudget
    }

    private fun initDate() {
        val day = dayFormat.format(Calendar.getInstance().time)
        val currentMonth = monthFormat.format(Calendar.getInstance().time)
        val currentYear = yearFormat.format(Calendar.getInstance().time)

        binding.date.text = getString(
            R.string.expense_full_date,
            day,
            dateFormat.format(Calendar.getInstance().time),
            currentYear
        )
        viewModel.getMonthlyTransaction(currentMonth.toInt(), currentYear.toInt())
    }

    private fun showBottomNavigation() {
        val dialog = BottomSheetDialog(requireContext())

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.bottom_sheet_expense)

        val update = dialog.findViewById<Button>(R.id.update)
        val moneyEditor = dialog.findViewById<TextInputEditText>(R.id.edit_money)

        moneyEditor?.setText(SharedPreferences.saveMonthBudget)

        update?.setOnClickListener {
            val monthBudget = moneyEditor?.text.toString()
            if (monthBudget == "") Utils.showMessage(
                R.string.message_input_empty_fields,
                requireContext()
            )
            else {
                SharedPreferences.saveMonthBudget = monthBudget
                binding.expenseAnalytics.clearChart()
                changeMonthBudgetTittle(monthBudget.toFloat())
                checkMonthBudget()
                showPiChart()
                dialog.dismiss()
            }
        }
        dialog.show()
    }

    private fun initAdapter() {
        expenseAdapter = ExpenseAdapter(requireContext())
        expensesRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = expenseAdapter
        }
        expenseAdapter.clickListener = this@ExpenseFragment
    }

    private fun initObservers() = with(viewModel) {
        monthlyExpenses.observe(viewLifecycleOwner, monthlyExpensesObserver)
    }

    private fun changeMonthBudgetTittle(monthBudget: Float) = with(binding) {
        budget.text = getString(R.string.expense_month_budget, monthBudget.toString())
    }

    private fun checkMonthBudget() = with(binding) {
        if (totalExpense > SharedPreferences.saveMonthBudget!!.toFloat()) {
            indicator.setImageResource(R.drawable.ic_negative_balance)
            expense.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.error_color
                )
            )
        } else {
            indicator.setImageResource(R.drawable.ic_positive_balance)
            expense.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.main_color_icon
                )
            )
        }
    }

    private fun showPiChart() = with(binding.expenseAnalytics) {
        addPieSlice(
            PieModel(
                "Еда",
                totalFood,
                ContextCompat.getColor(requireContext(), R.color.green_color)
            )
        )
        addPieSlice(
            PieModel(
                "Покупки",
                totalShopping,
                ContextCompat.getColor(requireContext(), R.color.blue_color)
            )
        )
        addPieSlice(
            PieModel(
                "Здоровье",
                totalHealth,
                ContextCompat.getColor(requireContext(), R.color.red_color)
            )
        )
        addPieSlice(
            PieModel(
                "Другое",
                totalOthers,
                ContextCompat.getColor(requireContext(), R.color.violet_color)
            )
        )
        addPieSlice(
            PieModel(
                "Транспорт",
                totalTransport,
                ContextCompat.getColor(requireContext(), R.color.yellow_color)
            )
        )
        addPieSlice(
            PieModel(
                "Учёба",
                totalAcademics,
                ContextCompat.getColor(requireContext(), R.color.orange_color)
            )
        )

        val monthExpense =
            if (SharedPreferences.saveMonthBudget == "") 0f else SharedPreferences.saveMonthBudget!!.toFloat()

        if (monthExpense >= totalExpense)
            addPieSlice(
                PieModel(
                    "Старт",
                    monthExpense - (totalExpense.toFloat()),
                    ContextCompat.getColor(requireContext(), R.color.gray_light)
                )
            )
        this.startAnimation()
    }

    private fun initListeners() = with(binding) {
        addNew.setOnClickListener {
            if (budget.text.toString() == "0 ₽")
                Utils.showMessage(
                    R.string.welcome_subtitle_message_expenses_add_budget,
                    requireContext()
                )
            else
                onExpenseClick(null)
        }
    }

    override fun onExpenseClick(expense: Expense?) {
        val direction = ExpenseFragmentDirections.toExpenseFragment(expense)
        findNavController().navigate(direction)
    }

    private fun showOnBoarding() = Utils.showOnBoarding(
        requireActivity(),
        binding.addNew,
        R.string.welcome_subtitle_message_expenses,
        requireContext()
    ) {
        SharedPreferences.expenseOnBoarding = true
    }

    companion object {
        private val APP_LOCALE_RU = Locale("ru")

        val dayFormat = SimpleDateFormat("dd", APP_LOCALE_RU)
        var monthFormat = SimpleDateFormat("MM", APP_LOCALE_RU)
        val yearFormat = SimpleDateFormat("yyyy", APP_LOCALE_RU)
        val dateFormat = SimpleDateFormat("MMMM", APP_LOCALE_RU)
    }
}