package com.example.graduateproject

import android.app.Application
import com.example.graduateproject.di.component.AppComponent
import com.example.graduateproject.di.component.DaggerAppComponent

class MainApplication : Application() {

    lateinit var appComponent: AppComponent
    lateinit var mainApplication: MainApplication

    override fun onCreate() {
        super.onCreate()
        mainApplication = this

        appComponent = DaggerAppComponent
            .builder()
            .application(this)
            .build()

        appComponent.inject(this)
    }
}
