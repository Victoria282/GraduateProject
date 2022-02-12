package com.example.graduateproject.authentication.validation

import com.example.graduateproject.R
import com.example.graduateproject.utils.Utils

object Validation {

    fun validateInputText(email: String, password: String, confirmPassword: String?): Int {
        return if (
            (email.isEmpty() || password.isEmpty() && confirmPassword == null) ||
            (email.isEmpty() || password.isEmpty() || (confirmPassword != null && confirmPassword.isEmpty()))
        )
            R.string.message_input_empty_fields
        else if (!Utils.isCorrectEmail(email))
            R.string.message_email_incorrect
        else if (confirmPassword != null && password != confirmPassword)
            R.string.message_password_is_difficult
        else 1
    }
}