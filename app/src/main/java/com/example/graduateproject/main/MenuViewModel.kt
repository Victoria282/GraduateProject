package com.example.graduateproject.main

import androidx.lifecycle.ViewModel
import com.example.graduateproject.authentication.firebase.Firebase
import javax.inject.Inject

class MenuViewModel @Inject constructor(
    private val firebase: Firebase
): ViewModel() {

}