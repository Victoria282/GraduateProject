package com.example.graduateproject.maps

import android.content.Context
import com.example.graduateproject.R
import com.example.graduateproject.maps.model.Place
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.InputStream
import java.io.InputStreamReader
import javax.inject.Inject

class PlacesReader @Inject constructor(
    val context: Context
) {
    private val gson = Gson()

    private val inputStream: InputStream
        get() = context.resources.openRawResource(R.raw.places)

    fun read(): List<Place> {
        val itemType = object : TypeToken<List<Place>>() {}.type
        val reader = InputStreamReader(inputStream)
        return gson.fromJson(reader, itemType)
    }
}