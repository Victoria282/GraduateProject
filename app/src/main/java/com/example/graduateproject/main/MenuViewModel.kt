package com.example.graduateproject.main

import androidx.lifecycle.ViewModel
import com.example.graduateproject.authentication.firebase.Firebase
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject

class MenuViewModel @Inject constructor(
    private val firebase: Firebase
) : ViewModel() {

    fun getUserInfo(): FirebaseUser? {
        return firebase.getCurrentUser()
    }
}