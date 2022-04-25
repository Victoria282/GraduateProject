package com.example.graduateproject.di.modules

import androidx.lifecycle.ViewModel
import com.example.graduateproject.authentication.authorization.AuthorizationViewModel
import com.example.graduateproject.authentication.registration.RegistrationViewModel
import com.example.graduateproject.authentication.restore.RestoreViewModel
import com.example.graduateproject.di.utils.ViewModelKey
import com.example.graduateproject.expense.ExpenseViewModel
import com.example.graduateproject.expense.add.AddExpenseViewModel
import com.example.graduateproject.main.MainViewModel
import com.example.graduateproject.menu.MenuViewModel
import com.example.graduateproject.notes.create.NoteCreateViewModel
import com.example.graduateproject.schedule.editor.LessonsEditorViewModel
import com.example.graduateproject.schedule.main.ScheduleViewModel
import com.example.graduateproject.settings.SettingsViewModel
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
    @ViewModelKey(MainViewModel::class)
    fun bindMainViewModel(viewModel: MainViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LessonsEditorViewModel::class)
    fun bindLessonsEditorViewModel(viewModel: LessonsEditorViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MenuViewModel::class)
    fun bindMenuViewModel(viewModel: MenuViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ScheduleViewModel::class)
    fun bindScheduleViewModel(viewModel: ScheduleViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(NoteCreateViewModel::class)
    fun bindNoteCreateViewModel(viewModel: NoteCreateViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SettingsViewModel::class)
    fun bindSettingsViewModel(viewModel: SettingsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ExpenseViewModel::class)
    fun bindExpenseViewModel(viewModel: ExpenseViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AddExpenseViewModel::class)
    fun bindAddExpenseViewModel(viewModel: AddExpenseViewModel): ViewModel
}