package com.example.graduateproject.di.modules

import androidx.fragment.app.Fragment
import com.example.graduateproject.authentication.authorization.AuthorizationFragment
import com.example.graduateproject.authentication.registration.RegistrationFragment
import com.example.graduateproject.authentication.restore.RestoreFragment
import com.example.graduateproject.di.utils.FragmentKey
import com.example.graduateproject.main.MainFragment
import com.example.graduateproject.schedule.lessons.LessonsFragment
import com.example.graduateproject.schedule.lessonsEditor.LessonsEditorFragment
import com.example.graduateproject.schedule.main.ScheduleFragment
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface FragmentModule {
    @Binds
    @IntoMap
    @FragmentKey(AuthorizationFragment::class)
    fun bindAuthorizationFragment(fragment: AuthorizationFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(RegistrationFragment::class)
    fun bindRegistrationFragment(fragment: RegistrationFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(RestoreFragment::class)
    fun bindRestoreFragment(fragment: RestoreFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(ScheduleFragment::class)
    fun bindScheduleFragment(fragment: ScheduleFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(LessonsEditorFragment::class)
    fun bindLessonsEditorFragment(fragment: LessonsEditorFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(LessonsFragment::class)
    fun bindLessonsFragment(fragment: LessonsFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(MainFragment::class)
    fun bindMainScreenFragment(fragment: MainFragment): Fragment
}