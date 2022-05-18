package com.example.graduateproject.expense.pichartBuilder

import android.content.Context
import androidx.core.content.ContextCompat
import com.example.graduateproject.R
import com.example.graduateproject.utils.Constants.FOOD
import com.example.graduateproject.utils.Constants.HEALTH
import com.example.graduateproject.utils.Constants.OTHER
import com.example.graduateproject.utils.Constants.REMAINS
import com.example.graduateproject.utils.Constants.SHOPPING
import com.example.graduateproject.utils.Constants.STUDY
import com.example.graduateproject.utils.Constants.TRANSPORT
import org.eazegraph.lib.charts.PieChart
import org.eazegraph.lib.models.PieModel

class PiChartBuilder(
    var context: Context,
    var pieChart: PieChart
) {

    fun createFood(totalFood: Float): PiChartBuilder {
        pieChart.addPieSlice(
            PieModel(
                FOOD,
                totalFood,
                ContextCompat.getColor(context, R.color.green_color)
            )
        )
        return this
    }

    fun crateShopping(totalShopping: Float): PiChartBuilder {
        pieChart.addPieSlice(
            PieModel(
                SHOPPING,
                totalShopping,
                ContextCompat.getColor(context, R.color.blue_color)
            )
        )
        return this
    }

    fun createHealth(totalHealth: Float): PiChartBuilder {
        pieChart.addPieSlice(
            PieModel(
                HEALTH,
                totalHealth,
                ContextCompat.getColor(context, R.color.red_color)
            )
        )
        return this
    }

    fun createOther(totalOthers: Float): PiChartBuilder {
        pieChart.addPieSlice(
            PieModel(
                OTHER,
                totalOthers,
                ContextCompat.getColor(context, R.color.violet_color)
            )
        )
        return this
    }

    fun createTransport(totalTransport: Float): PiChartBuilder {
        pieChart.addPieSlice(
            PieModel(
                TRANSPORT,
                totalTransport,
                ContextCompat.getColor(context, R.color.yellow_color)
            )
        )
        return this
    }

    fun createStudy(totalAcademics: Float): PiChartBuilder {
        pieChart.addPieSlice(
            PieModel(
                STUDY,
                totalAcademics,
                ContextCompat.getColor(context, R.color.orange_color)
            )
        )
        return this
    }

    fun createRemains(monthExpense: Float, totalExpense: Float): PiChartBuilder {
        pieChart.addPieSlice(
            PieModel(
                REMAINS,
                monthExpense - (totalExpense),
                ContextCompat.getColor(context, R.color.gray_light)
            )
        )
        return this
    }

    fun build(): PieChart = pieChart
}