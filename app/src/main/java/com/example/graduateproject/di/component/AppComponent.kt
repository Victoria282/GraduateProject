package com.example.graduateproject.di.component

import android.app.Application
import com.example.graduateproject.MainApplication
import com.example.graduateproject.di.modules.*
import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        ActivityModule::class,
        AppModule::class,
        PresenterModule::class,
        ViewModelModule::class,
        DatabaseModule::class,
        RepositoryModule::class,
        FragmentModule::class
    ]
)
@Singleton
interface AppComponent {
    @Component.Builder
    interface Builder {

        @BindsInstance
        fun setApplication(application: Application): Builder

        fun build(): AppComponent
    }

    fun inject(mainApplication: MainApplication)
}