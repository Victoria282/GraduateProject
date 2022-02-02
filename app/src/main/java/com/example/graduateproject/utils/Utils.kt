package com.example.graduateproject.utils

import android.app.AlertDialog
import android.content.Context
import android.util.Patterns
import com.example.graduateproject.R

object Utils {

    fun isCorrectEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun showMessage(messageId: Int, context: Context): AlertDialog = AlertDialog.Builder(context)
        .setMessage(messageId)
        .setIcon(R.drawable.ic_alert)
        .setNeutralButton(R.string.message_ok, null)
        .setCancelable(true)
        .setTitle(R.string.alert_dialog_message)
        .show()
}