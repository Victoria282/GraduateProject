package com.example.graduateproject

import android.app.Application
import com.example.graduateproject.database.AppDatabase
import com.example.graduateproject.di.component.AppComponent
import com.example.graduateproject.di.component.DaggerAppComponent
import com.example.graduateproject.shared_preferences.SharedPreferences
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

class MainApplication : Application(), HasAndroidInjector {

    @Inject
    lateinit var hasAndroidInjector: DispatchingAndroidInjector<Any>

    private val appComponent: AppComponent by lazy {
        DaggerAppComponent
            .builder()
            .setApplication(this)
            .build()
    }

    override fun onCreate() {
        super.onCreate()
        AppDatabase.invoke(applicationContext)
        appComponent.inject(this)
        SharedPreferences.init(applicationContext)
    }

    override fun androidInjector(): AndroidInjector<Any> = hasAndroidInjector
}