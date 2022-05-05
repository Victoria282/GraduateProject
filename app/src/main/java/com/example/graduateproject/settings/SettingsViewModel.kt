package com.example.graduateproject.settings

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.graduateproject.authentication.firebase.Firebase
import com.google.android.gms.tasks.Task
import javax.inject.Inject

class SettingsViewModel @Inject constructor(
    val firebase: Firebase
) : ViewModel() {
    private val _statusDeleteAccount = MutableLiveData<Task<Void>>()
    val statusDeleteAccount: MutableLiveData<Task<Void>>
        get() = _statusDeleteAccount

    private val _changePassword = MutableLiveData<Task<Void>>()
    val changePassword: MutableLiveData<Task<Void>>
        get() = _changePassword

    fun deleteAccount() = firebase.deleteAccount()?.addOnCompleteListener {
        _statusDeleteAccount.postValue(it)
    }

    fun changePassword(password: String) =
        firebase.changePassword(password)?.addOnCompleteListener {
            _changePassword.postValue(it)
        }

    fun reAuth() = firebase.reAuthenticate()
}