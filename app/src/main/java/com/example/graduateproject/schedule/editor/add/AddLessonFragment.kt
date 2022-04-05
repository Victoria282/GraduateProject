package com.example.graduateproject.schedule.editor.add

import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.graduateproject.R
import com.example.graduateproject.databinding.AddLessonFragmentBinding
import com.example.graduateproject.schedule.database.DatabaseViewModel
import com.example.graduateproject.schedule.model.Lesson
import com.example.graduateproject.shared_preferences.SharedPreferences
import com.example.graduateproject.utils.Utils
import com.google.android.material.textview.MaterialTextView
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class AddLessonFragment @Inject constructor(

) : Fragment(R.layout.add_lesson_fragment) {

    private lateinit var binding: AddLessonFragmentBinding

    private lateinit var viewModel: DatabaseViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = AddLessonFragmentBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[DatabaseViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
    }

    private fun initListeners() = with(binding) {
        btnSave.setOnClickListener { getInputLesson() }

        startTime.setOnClickListener { createTimePicker(startTime) }

        endTime.setOnClickListener { createTimePicker(endTime) }
    }

    private fun getInputLesson() = with(binding) {
        val lessonName = lessonName.text?.trim()
        val teacherName = teacherName.text?.trim()
        val cabinet = cabinet.text?.trim()

        val radioButton: View =
            typeOfLessonLayout.findViewById(typeOfLessonLayout.checkedRadioButtonId)

        val typeOfLesson: Int = typeOfLessonLayout.indexOfChild(radioButton)
        val numOfLesson: Int =
            if (lessonNumber.text?.trim().toString().isEmpty()) 0 else lessonNumber.text?.trim()
                .toString().toInt()

        val startTime = startTime.text?.trim()
        val endTime = endTime.text?.trim()

        validateInputFields(
            lessonName,
            teacherName,
            cabinet,
            typeOfLesson,
            numOfLesson,
            startTime,
            endTime
        )
    }

    private fun validateInputFields(
        lessonName: CharSequence?,
        teacherName: CharSequence?,
        cabinet: CharSequence?,
        typeOfLesson: Int,
        numOfLesson: Int,
        startTime: CharSequence?,
        endTime: CharSequence?
    ) {
        if (lessonName == "" || teacherName == "" || cabinet == "" || startTime == "" || endTime == "") {
            Utils.showMessage(R.string.message_input_empty_fields, requireContext())
        } else {
            val formedLesson = Lesson(
                id = 0,
                positionOfWeekDay = SharedPreferences.savedWeekDay,
                numberOfLesson = numOfLesson,
                subject = lessonName.toString(),
                teacher = teacherName.toString(),
                typeOfLesson = typeOfLesson,
                cabinet = cabinet.toString(),
                countOfWeek = SharedPreferences.savedStudyWeek,
                startTime = startTime.toString(),
                endTime = endTime.toString()
            )
            viewModel.insertLesson(formedLesson)
            backToLessons()
        }
    }

    private fun backToLessons() {
        val directions = AddLessonFragmentDirections.toSchedule()
        findNavController().navigate(directions)
    }

    private fun createTimePicker(textView: MaterialTextView) {
        val cal = Calendar.getInstance()
        val timeSetListener = TimePickerDialog.OnTimeSetListener { _, hour, minute ->
            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)
            textView.text = SimpleDateFormat("HH:mm").format(cal.time)
        }
        TimePickerDialog(
            requireContext(),
            timeSetListener,
            cal.get(Calendar.HOUR_OF_DAY),
            cal.get(Calendar.MINUTE),
            true
        ).show()
    }
}