package com.example.graduateproject.maps.model

import com.example.graduateproject.utils.Utils
import java.io.Serializable

data class Place(
    val id: Int,
    val tittle: String,
    val description: String,
    val latitude: Double,
    val longitude: Double,
    val typeMapPlace: Int
) : Serializable {
    private fun getType(): PlaceType? =
        PlaceType.values().associateBy(PlaceType::id)[typeMapPlace]

    fun getIcon() = Utils.getIcon(getType())
}

enum class PlaceType(val id: Int) {
    CORPUS(0),
    DORMITORY(1),
    LIBRARY(2)
}