package com.example.graduateproject.di.modules

import androidx.fragment.app.Fragment
import com.example.graduateproject.authentication.authorization.AuthorizationFragment
import com.example.graduateproject.authentication.registration.RegistrationFragment
import com.example.graduateproject.authentication.restore.RestoreFragment
import com.example.graduateproject.di.utils.FragmentKey
import com.example.graduateproject.expense.ExpenseFragment
import com.example.graduateproject.expense.adding.AddingExpenseFragment
import com.example.graduateproject.main.MainFragment
import com.example.graduateproject.maps.MapsFragment
import com.example.graduateproject.notes.NotesFragment
import com.example.graduateproject.notes.create.NoteCreateFragment
import com.example.graduateproject.schedule.editor.LessonsEditorFragment
import com.example.graduateproject.schedule.main.ScheduleFragment
import com.example.graduateproject.settings.SettingsFragment
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface PresenterModule {
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
    @FragmentKey(MainFragment::class)
    fun bindMainScreenFragment(fragment: MainFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(NotesFragment::class)
    fun bindNotesFragment(fragment: NotesFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(NoteCreateFragment::class)
    fun bindNoteCreateFragment(fragment: NoteCreateFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(MapsFragment::class)
    fun bindMapsFragment(fragment: MapsFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(SettingsFragment::class)
    fun bindSettingsFragment(fragment: SettingsFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(ExpenseFragment::class)
    fun bindExpenseFragment(fragment: ExpenseFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(AddingExpenseFragment::class)
    fun bindAddExpenseFragment(fragment: AddingExpenseFragment): Fragment
}