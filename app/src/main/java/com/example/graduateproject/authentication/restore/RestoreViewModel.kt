package com.example.graduateproject.authentication.restore

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.graduateproject.authentication.firebase.Firebase
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import javax.inject.Inject

class RestoreViewModel @Inject constructor(
    private val firebase: Firebase
): ViewModel() {

    private val _statusRestorePassword = MutableLiveData<Task<Void>>()
    val statusRestorePassword: MutableLiveData<Task<Void>>
        get() = _statusRestorePassword

    fun restoreUserPassword(email: String) {
        firebase.restoreAccount(email).addOnCompleteListener {
            _statusRestorePassword.postValue(it)
        }
    }
}