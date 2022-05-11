package com.example.graduateproject.widget.service

import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.example.graduateproject.R
import com.example.graduateproject.schedule.model.Lesson
import com.example.graduateproject.schedule.repository.ScheduleRepository
import com.example.graduateproject.shared_preferences.Storage
import com.example.graduateproject.utils.Constants.DIVIDER
import dagger.android.AndroidInjection
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class WidgetService : RemoteViewsService() {

    @Inject
    lateinit var scheduleRepository: ScheduleRepository

    override fun onGetViewFactory(p0: Intent?): RemoteViewsFactory {
        return WidgetItemFactory(applicationContext, scheduleRepository)
    }

    override fun onCreate() {
        super.onCreate()
        AndroidInjection.inject(this)
    }

    class WidgetItemFactory(
        private val context: Context,
        private val scheduleRepository: ScheduleRepository,
    ) : RemoteViewsFactory {

        private val job = SupervisorJob()
        private val coroutineScope = CoroutineScope(Dispatchers.IO + job)

        var lessonsList: ArrayList<Lesson> = arrayListOf()

        override fun onCreate() {
            coroutineScope.launch {
                scheduleRepository.getLessons().forEach {
                    if (it.weekDay.uppercase() == sdf.format(day).uppercase()
                        &&
                        Storage.studyWeek == it.week
                    ) {
                        lessonsList.add(it)
                    }
                }
            }
        }

        override fun getLoadingView(): RemoteViews? = null

        override fun getItemId(p0: Int): Long = p0.toLong()

        override fun onDataSetChanged() {
        }

        override fun hasStableIds(): Boolean = true

        override fun getViewAt(p0: Int): RemoteViews {
            val views = RemoteViews(context.packageName, R.layout.widget_list_item)

            views.apply {
                setTextViewText(R.id.lesson_start_time, lessonsList[p0].startTime)
                setTextViewText(R.id.lesson_end_time, lessonsList[p0].endTime)
                setTextViewText(R.id.lesson_tittle, lessonsList[p0].subject)
                setTextViewText(R.id.lesson_cabinet, lessonsList[p0].cabinet)
                setTextViewText(R.id.lesson_time_divider, DIVIDER)
            }
            return views
        }

        override fun getCount(): Int = lessonsList.size

        override fun getViewTypeCount(): Int = 1

        override fun onDestroy() = job.cancel()
    }

    companion object {
        private var sdf = SimpleDateFormat("EEEE", Locale("ru"))
        private val day = System.currentTimeMillis()
    }
}