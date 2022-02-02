package com.example.graduateproject.di.component

import android.app.Application
import com.example.graduateproject.MainApplication
import com.example.graduateproject.di.modules.AppModule
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class
    ]
)
interface AppComponent {
    @Component.Builder
    interface Builder {
        // provide Application instance into DI
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

    // this is needed because MainApplication has @Inject
    fun inject(app: MainApplication)
}