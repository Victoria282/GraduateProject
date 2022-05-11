package com.example.graduateproject.di.modules

import com.example.graduateproject.widget.ScheduleWidget
import com.example.graduateproject.widget.service.WidgetService
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class WidgetModule {
    @ContributesAndroidInjector
    internal abstract fun contributeDWidget(): ScheduleWidget

    @ContributesAndroidInjector
    internal abstract fun contributeWidgetService(): WidgetService
}