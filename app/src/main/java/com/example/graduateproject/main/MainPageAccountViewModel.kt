package com.example.graduateproject.main

import androidx.lifecycle.ViewModel
import com.example.graduateproject.authentication.firebase.Firebase

class MainPageAccountViewModel : ViewModel() {

    private val firebase = Firebase()

    fun logOutUser() { firebase.logoutUser() }
}