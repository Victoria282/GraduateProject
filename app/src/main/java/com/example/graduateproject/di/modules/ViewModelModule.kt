package com.example.graduateproject.di.modules

import androidx.lifecycle.ViewModel
import com.example.graduateproject.authentication.authorization.AuthorizationViewModel
import com.example.graduateproject.authentication.registration.RegistrationViewModel
import com.example.graduateproject.authentication.restore.RestoreViewModel
import com.example.graduateproject.di.utils.ViewModelKey
import com.example.graduateproject.main.MainPageAccountViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(AuthorizationViewModel::class)
    fun bindAuthorizationViewModel(viewModel: AuthorizationViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(RegistrationViewModel::class)
    fun bindRegistrationViewModel(viewModel: RegistrationViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(RestoreViewModel::class)
    fun bindRestoreViewModel(viewModel: RestoreViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MainPageAccountViewModel::class)
    fun bindMainPageAccountViewModel(viewModel: MainPageAccountViewModel): ViewModel
}