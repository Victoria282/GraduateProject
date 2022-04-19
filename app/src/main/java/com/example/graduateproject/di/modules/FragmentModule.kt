package com.example.graduateproject.di.modules

import com.example.graduateproject.authentication.authorization.AuthorizationFragment
import com.example.graduateproject.authentication.registration.RegistrationFragment
import com.example.graduateproject.authentication.restore.RestoreFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentModule {
    @ContributesAndroidInjector
    internal abstract fun contributeAuthorizationFragment(): AuthorizationFragment

    @ContributesAndroidInjector
    internal abstract fun contributeRegistrationFragment(): RegistrationFragment

    @ContributesAndroidInjector
    internal abstract fun contributeRestoreFragment(): RestoreFragment
}