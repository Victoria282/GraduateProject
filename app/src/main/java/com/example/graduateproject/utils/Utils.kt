package com.example.graduateproject.utils

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Bitmap
import android.graphics.Canvas
import android.util.Patterns
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.content.res.ResourcesCompat
import com.example.graduateproject.R
import com.example.graduateproject.maps.model.PlaceType
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import java.text.SimpleDateFormat
import java.util.*

object Utils {

    fun isCorrectEmail(email: String): Boolean = Patterns.EMAIL_ADDRESS.matcher(email).matches()

    fun showMessage(
        messageId: Int,
        context: Context
    ): AlertDialog = AlertDialog.Builder(context)
        .setMessage(messageId)
        .setIcon(R.drawable.ic_alert)
        .setNeutralButton(R.string.message_ok, null)
        .setCancelable(true)
        .setTitle(R.string.alert_dialog_message)
        .show()

    fun showMessageWithPositiveButton(
        messageId: Int,
        context: Context,
        listenerMessageId: Int,
        listener: DialogInterface.OnClickListener
    ): AlertDialog = AlertDialog.Builder(context)
        .setMessage(messageId)
        .setIcon(R.drawable.ic_alert)
        .setCancelable(true)
        .setTitle(R.string.alert_dialog_message)
        .setPositiveButton(listenerMessageId, listener)
        .show()

    fun Context.hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(
            Activity.INPUT_METHOD_SERVICE
        ) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun vectorToBitmap(
        context: Context,
        id: Int
    ): BitmapDescriptor {
        val vectorDrawable = ResourcesCompat.getDrawable(context.resources, id, null)
            ?: return BitmapDescriptorFactory.defaultMarker()
        val bitmap = Bitmap.createBitmap(
            60,
            60,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        vectorDrawable.setBounds(0, 0, canvas.width, canvas.height)
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    fun getIcon(place: PlaceType?): Int {
        when (place?.name) {
            "CORPUS" -> return R.drawable.map_marker_blue
            "DORMITORY" -> return R.drawable.map_marker_red
            "LIBRARY" -> return R.drawable.map_marker_green
        }
        return R.drawable.map_marker_green
    }

    fun getDayOfWeek(): String {
        val appLocale = Locale("ru")
        Locale.setDefault(appLocale)
        return SimpleDateFormat("EEEE", appLocale).format(System.currentTimeMillis())
    }
}