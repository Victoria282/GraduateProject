package com.example.graduateproject.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetManager.ACTION_APPWIDGET_UPDATE
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
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        appWidgetIds.forEach {
            updateAppWidget(context, appWidgetManager, it)
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        AndroidInjection.inject(this, context)
        Locale.setDefault(APP_LOCALE_RU)
        super.onReceive(context, intent)

        if (ACTION_APPWIDGET_UPDATE == intent.action) {
            val widgetIds = intent.extras?.getIntArray(AppWidgetManager.EXTRA_APPWIDGET_IDS)
            onUpdate(context, AppWidgetManager.getInstance(context), widgetIds!!)
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
            views.setOnClickPendingIntent(
                R.id.updateWidget,
                getPendingSelfIntent(context, ACTION_APPWIDGET_UPDATE)
            )
            views.setRemoteAdapter(R.id.lessons_list, serviceIntent)

            appWidgetManager.updateAppWidget(appWidgetId, views)
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.lessons_list)
        }

        private fun getPendingSelfIntent(context: Context?, action: String?): PendingIntent? {
            val intent = Intent(context, ScheduleWidget::class.java)
            intent.action = action
            return PendingIntent.getBroadcast(context, 0, intent, 0)
        }
    }
}