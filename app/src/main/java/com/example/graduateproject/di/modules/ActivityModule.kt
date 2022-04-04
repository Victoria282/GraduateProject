package com.example.graduateproject.di.modules

import com.example.graduateproject.authentication.MainActivity
import com.example.graduateproject.authentication.SplashScreen
import com.example.graduateproject.menu.MenuActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface ActivityModule {
    @ContributesAndroidInjector
    fun contributeMainActivity(): MainActivity

    @ContributesAndroidInjector
    fun contributeSplashScreen(): SplashScreen

    @ContributesAndroidInjector
    fun contributeMenuActivity(): MenuActivity
}