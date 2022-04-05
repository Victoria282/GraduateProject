package com.example.graduateproject.di.modules

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [ViewModelModule::class])
class AppModule {
    @Provides
    @Singleton
    fun provideAppContext(app: Application): Context = app.applicationContext
}