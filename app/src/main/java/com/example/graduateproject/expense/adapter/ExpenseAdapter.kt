package com.example.graduateproject.expense.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.graduateproject.R
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
                title.text = expense?.title
                money.text = expense?.amount.toString()
                date.text = expense?.date
                category.text = expense?.category
                expenseItem.setOnClickListener {
                    listener?.onExpenseClick(expense)
                }

                when (expense?.category) {
                    "Еда" -> {
                        categoryIcon.setImageResource(R.drawable.food)
                        categoryIcon.setColorFilter(
                            ContextCompat.getColor(
                                categoryIcon.context,
                                R.color.green_color
                            )
                        )
                        category.setTextColor(
                            ContextCompat.getColor(
                                category.context,
                                R.color.green_color
                            )
                        )
                        cardImage.setCardBackgroundColor(
                            ContextCompat.getColor(
                                cardImage.context,
                                R.color.green_light_color
                            )
                        )
                    }
                    "Покупки" -> {
                        categoryIcon.setImageResource(R.drawable.shopping)
                        categoryIcon.setColorFilter(
                            ContextCompat.getColor(
                                categoryIcon.context,
                                R.color.blue_color
                            )
                        )
                        category.setTextColor(
                            ContextCompat.getColor(
                                category.context,
                                R.color.blue_color
                            )
                        )
                        cardImage.setCardBackgroundColor(
                            ContextCompat.getColor(
                                cardImage.context,
                                R.color.blue_light_color
                            )
                        )
                    }
                    "Транспорт" -> {
                        categoryIcon.setImageResource(R.drawable.transport)
                        categoryIcon.setColorFilter(
                            ContextCompat.getColor(
                                categoryIcon.context,
                                R.color.yellow_color
                            )
                        )
                        category.setTextColor(
                            ContextCompat.getColor(
                                category.context,
                                R.color.yellow_color
                            )
                        )
                        cardImage.setCardBackgroundColor(
                            ContextCompat.getColor(
                                cardImage.context,
                                R.color.yellow_light_color
                            )
                        )
                    }
                    "Здоровье" -> {
                        categoryIcon.setImageResource(R.drawable.health)
                        categoryIcon.setColorFilter(
                            ContextCompat.getColor(
                                categoryIcon.context,
                                R.color.red_color
                            )
                        )
                        category.setTextColor(
                            ContextCompat.getColor(
                                category.context,
                                R.color.red_color
                            )
                        )
                        cardImage.setCardBackgroundColor(
                            ContextCompat.getColor(
                                cardImage.context,
                                R.color.red_light_color
                            )
                        )
                    }
                    "Другое" -> {
                        categoryIcon.setImageResource(R.drawable.other)
                        categoryIcon.setColorFilter(
                            ContextCompat.getColor(
                                categoryIcon.context,
                                R.color.violet_color
                            )
                        )
                        category.setTextColor(
                            ContextCompat.getColor(
                                category.context,
                                R.color.violet_color
                            )
                        )
                        cardImage.setCardBackgroundColor(
                            ContextCompat.getColor(
                                cardImage.context,
                                R.color.violet_light_color
                            )
                        )
                    }
                    "Учёба" -> {
                        categoryIcon.setImageResource(R.drawable.education)
                        categoryIcon.setColorFilter(
                            ContextCompat.getColor(
                                categoryIcon.context,
                                R.color.orange_color
                            )
                        )
                        category.setTextColor(
                            ContextCompat.getColor(
                                category.context,
                                R.color.orange_color
                            )
                        )
                        cardImage.setCardBackgroundColor(
                            ContextCompat.getColor(
                                cardImage.context,
                                R.color.orange_light_color
                            )
                        )
                    }
                }
            }
        }
    }

    fun updateExpenses(expenses: Expense) {
        this.expensesList.apply {
            add(expenses)
        }
        notifyDataSetChanged()
    }

    override fun getItemCount() = expensesList.size
}

