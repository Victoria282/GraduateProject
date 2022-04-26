package com.example.graduateproject.expense.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.graduateproject.utils.Utils
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "Expense")
@Parcelize
data class Expense(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    @ColumnInfo(name = "category")
    var category: String,
    @ColumnInfo(name = "title")
    var title: String,
    @ColumnInfo(name = "amount")
    var amount: Double,
    @ColumnInfo(name = "date")
    var date: String,
    @ColumnInfo(name = "day")
    var day: Int,
    @ColumnInfo(name = "month")
    var month: Int,
    @ColumnInfo(name = "year")
    var year: Int,
    @ColumnInfo(name = "note")
    var note: String?
) : Parcelable {
    fun getIcon() = Utils.getCategoryIcon(category)
    fun getColors() = Utils.getCategoryColors(category)
}