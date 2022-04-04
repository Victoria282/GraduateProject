package com.example.graduateproject.schedule.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import javax.inject.Inject

class ScheduleViewModel @Inject constructor(

) : ViewModel() {

    private val _studyWeek = MutableLiveData<Int>()
    val studyWeek: MutableLiveData<Int>
        get() = _studyWeek

    fun saveStudyWeek(numberOfWeek: Int) {
        _studyWeek.value = numberOfWeek
    }
}
