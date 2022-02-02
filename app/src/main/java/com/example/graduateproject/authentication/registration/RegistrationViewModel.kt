package com.example.graduateproject.authentication.registration

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.graduateproject.authentication.firebase.Firebase
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import javax.inject.Inject

class RegistrationViewModel : ViewModel() {

    private val firebase = Firebase()

    private val _statusRegistration = MutableLiveData<Task<AuthResult>>()
    val statusRegistration: MutableLiveData<Task<AuthResult>>
        get() = _statusRegistration

    fun registerUser(email: String, password: String) {
        firebase.registerUser(email, password).addOnCompleteListener {
            _statusRegistration.postValue(it)
        }
    }
}