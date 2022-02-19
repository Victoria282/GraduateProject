package com.example.graduateproject.main

import androidx.lifecycle.ViewModel
import com.example.graduateproject.authentication.firebase.Firebase
import javax.inject.Inject

class MainPageAccountViewModel @Inject constructor(
    private val firebase: Firebase
): ViewModel() {

    fun logOutUser() {
        firebase.logoutUser()
    }
}