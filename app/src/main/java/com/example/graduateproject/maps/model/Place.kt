package com.example.graduateproject.maps.model

data class Place(
    val id: Int,
    val tittle: String,
    val description: String,
    val latitude: Double,
    val longitude: Double,
    val typeMapPlace: Int
)
