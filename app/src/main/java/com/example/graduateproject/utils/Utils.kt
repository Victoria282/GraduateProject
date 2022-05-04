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
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.example.graduateproject.R
import com.example.graduateproject.maps.model.PlaceType
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt

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
        return when (place?.name) {
            "CORPUS" -> R.drawable.map_marker_blue
            "DORMITORY" -> R.drawable.map_marker_red
            "LIBRARY" -> R.drawable.map_marker_green
            else -> R.drawable.map_marker_green
        }
    }

    fun getCategoryIcon(category: String): Int {
        return when (category) {
            "Еда" -> R.drawable.food
            "Покупки" -> R.drawable.shopping
            "Транспорт" -> R.drawable.transport
            "Здоровье" -> R.drawable.health
            "Другое" -> R.drawable.other
            "Учёба" -> R.drawable.education
            else -> R.drawable.other
        }
    }

    fun getCategoryColors(category: String): Pair<Int, Int> {
        return when (category) {
            "Еда" -> Pair(R.color.green_color, R.color.green_light_color)
            "Покупки" -> Pair(R.color.blue_color, R.color.blue_light_color)
            "Транспорт" -> Pair(R.color.yellow_color, R.color.yellow_light_color)
            "Здоровье" -> Pair(R.color.red_color, R.color.red_light_color)
            "Другое" -> Pair(R.color.violet_color, R.color.violet_light_color)
            "Учёба" -> Pair(R.color.orange_color, R.color.orange_light_color)
            else -> Pair(R.color.green_color, R.color.green_light_color)
        }
    }

    fun showOnBoarding(
        activity: Activity,
        view: View,
        message: Int,
        context: Context,
        action: () -> Unit
    ) {
        MaterialTapTargetPrompt.Builder(activity)
            .setTarget(view)
            .setPrimaryText(R.string.welcome_message_expenses)
            .setFocalRadius(80.0f)
            .setSecondaryText(message)
            .setBackButtonDismissEnabled(true)
            .setBackgroundColour(ContextCompat.getColor(context, R.color.main_color_icon))
            .setPromptStateChangeListener { _, state ->
                if (state == MaterialTapTargetPrompt.STATE_FOCAL_PRESSED || state == MaterialTapTargetPrompt.STATE_NON_FOCAL_PRESSED)
                    action()
            }
            .show()
    }
}