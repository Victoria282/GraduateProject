package com.example.graduateproject.authentication.authorization

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.graduateproject.authentication.firebase.Firebase
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult

class AuthorizationViewModel : ViewModel() {

    private val firebase = Firebase()

    private val _statusAuthorization = MutableLiveData<Task<AuthResult>>()
    val statusAuthorization: MutableLiveData<Task<AuthResult>>
        get() = _statusAuthorization

    fun loginUser(email: String, password: String) {
        firebase.loginUser(email, password).addOnCompleteListener {
            _statusAuthorization.postValue(it)
        }
    }

    fun checkUserAuthorization(): Boolean = firebase.getCurrentUser() != null
}