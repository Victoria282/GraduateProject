package com.example.graduateproject.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.RemoteViews
import com.example.graduateproject.R
import com.example.graduateproject.widget.service.WidgetService
import dagger.android.AndroidInjection
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class ScheduleWidget @Inject constructor() : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (appWidgetId in appWidgetIds)
            updateAppWidget(context, appWidgetManager, appWidgetId)
    }

    override fun onReceive(context: Context, intent: Intent) {
        AndroidInjection.inject(this, context)
        Locale.setDefault(APP_LOCALE_RU)

        if (AppWidgetManager.ACTION_APPWIDGET_UPDATE == intent.action) {
            val widgetIds = intent.extras?.getIntArray(AppWidgetManager.EXTRA_APPWIDGET_IDS)
            onUpdate(context, AppWidgetManager.getInstance(context), widgetIds!!)
        } else {
            super.onReceive(context, intent)
        }
    }

    companion object {
        private var sdf = SimpleDateFormat("EEEE", Locale("ru"))
        private val APP_LOCALE_RU = Locale("ru")

        internal fun updateAppWidget(
            context: Context,
            appWidgetManager: AppWidgetManager,
            appWidgetId: Int
        ) {
            val views = RemoteViews(context.packageName, R.layout.schedule_widget)

            val weekDay = sdf.format(System.currentTimeMillis()).uppercase()

            val serviceIntent = Intent(context, WidgetService::class.java)
            serviceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            serviceIntent.data = Uri.parse(serviceIntent.toUri(Intent.URI_INTENT_SCHEME))

            views.setTextViewText(R.id.week_day, weekDay)
            views.setRemoteAdapter(R.id.lessons_list, serviceIntent)

            appWidgetManager.updateAppWidget(appWidgetId, views)
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.lessons_list)
        }
    }
}