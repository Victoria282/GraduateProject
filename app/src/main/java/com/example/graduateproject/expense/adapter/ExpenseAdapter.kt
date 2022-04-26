package com.example.graduateproject.expense.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.graduateproject.databinding.ExpenseItemLayoutBinding
import com.example.graduateproject.expense.model.Expense

class ExpenseAdapter(val context: Context) :
    RecyclerView.Adapter<ExpenseAdapter.DataViewHolder>() {
    var expensesList: ArrayList<Expense> = ArrayList()
    var clickListener: ExpenseClickListener? = null

    interface ExpenseClickListener {
        fun onExpenseClick(expense: Expense?)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DataViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ExpenseItemLayoutBinding.inflate(inflater, parent, false)
        return DataViewHolder(binding, clickListener)
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.bind(expensesList[position])
    }

    class DataViewHolder(
        private val binding: ExpenseItemLayoutBinding,
        private val listener: ExpenseClickListener?
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(expense: Expense) {
            with(binding) {
                title.text = expense.title
                money.text = expense.amount.toString()
                date.text = expense.date
                category.text = expense.category
                expenseItem.setOnClickListener {
                    listener?.onExpenseClick(expense)
                }
                categoryIcon.setImageResource(expense.getIcon())
                categoryIcon.setColorFilter(
                    ContextCompat.getColor(
                        categoryIcon.context,
                        expense.getColors().first
                    )
                )
                category.setTextColor(
                    ContextCompat.getColor(
                        category.context,
                        expense.getColors().first
                    )
                )
                cardImage.setCardBackgroundColor(
                    ContextCompat.getColor(
                        cardImage.context,
                        expense.getColors().second
                    )
                )
            }
        }
    }

    fun updateExpenses(expenses: Expense) {
        expensesList.add(expenses)
        notifyDataSetChanged()
    }

    override fun getItemCount() = expensesList.size
}

