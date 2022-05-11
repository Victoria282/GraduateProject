package com.example.graduateproject.schedule.editor

import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.graduateproject.R
import com.example.graduateproject.databinding.FragmentLessonsEditorBinding
import com.example.graduateproject.di.utils.ViewModelFactory
import com.example.graduateproject.schedule.model.Lesson
import com.example.graduateproject.shared_preferences.Storage
import com.example.graduateproject.utils.Utils
import com.google.android.material.textview.MaterialTextView
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class LessonsEditorFragment @Inject constructor(
    viewModelFactory: ViewModelFactory
) : Fragment(R.layout.fragment_lessons_editor) {

    private lateinit var binding: FragmentLessonsEditorBinding
    private val viewModel: LessonsEditorViewModel by viewModels { viewModelFactory }
    private val args by navArgs<LessonsEditorFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLessonsEditorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
        args.lesson?.let { setLessonInfo(it) }
    }

    private fun setLessonInfo(lesson: Lesson) {
        val typeOfLesson = lesson.lessonType

        with(binding) {
            lessonName.setText(lesson.subject)
            teacherName.setText(lesson.teacher)
            cabinet.setText(lesson.cabinet)
            startTime.text = lesson.startTime
            endTime.text = lesson.endTime
            lessonNumber.setText(lesson.lesson.toString())

            if (typeOfLesson == 0) lecture.isChecked =
                true else practice.isChecked = true
        }
    }

    private fun initListeners() = with(binding) {
        btnSave.setOnClickListener { getInputLesson() }

        btnDelete.setOnClickListener { deleteLesson() }

        startTime.setOnClickListener { createTimePicker(startTime) }

        endTime.setOnClickListener { createTimePicker(endTime) }
    }

    private fun deleteLesson() {
        args.lesson?.let { viewModel.deleteLesson(it) }
        backToLessons()
    }

    private fun getInputLesson() = with(binding) {
        val lesson = lessonName.text?.trim().toString()
        val teacher = teacherName.text?.trim().toString()
        val cabinet = cabinet.text?.trim().toString()

        val buttonId: View = typeOfLesson.findViewById(typeOfLesson.checkedRadioButtonId)
        val lessonType = typeOfLesson.indexOfChild(buttonId)
        val lessonPosition = if (lessonNumber.text?.trim().toString().isEmpty()) 0
        else lessonNumber.text?.trim().toString().toInt()

        val startTime = startTime.text?.trim().toString()
        val endTime = endTime.text?.trim().toString()

        validateInputFields(
            lesson,
            teacher,
            cabinet,
            lessonType,
            lessonPosition,
            startTime,
            endTime
        )
    }

    private fun validateInputFields(
        lessonName: String?,
        teacherName: String?,
        cabinet: String?,
        typeOfLesson: Int,
        numOfLesson: Int,
        startTime: String?,
        endTime: String?
    ) {
        if (lessonName.isNullOrEmpty() || teacherName.isNullOrEmpty() || cabinet.isNullOrEmpty() || startTime.isNullOrEmpty() || endTime.isNullOrEmpty()) {
            Utils.showMessage(R.string.message_input_empty_fields, requireContext())
        } else {
            val id = if (args.lesson?.id != null) args.lesson!!.id else 0
            val formedLesson = Lesson(
                id = id,
                weekDay = Storage.weekDay!!,
                lesson = numOfLesson,
                subject = lessonName,
                teacher = teacherName,
                lessonType = typeOfLesson,
                cabinet = cabinet,
                week = Storage.studyWeek,
                startTime = startTime,
                endTime = endTime
            )
            if (args.lesson == null)
                viewModel.insertLesson(formedLesson)
            else
                viewModel.updateLesson(formedLesson)
            backToLessons()
        }
    }

    private fun backToLessons() {
        val directions = LessonsEditorFragmentDirections.toSchedule()
        findNavController().navigate(directions)
    }

    private fun createTimePicker(timeTextView: MaterialTextView) {
        val cal = Calendar.getInstance()

        val timeSetListener = TimePickerDialog.OnTimeSetListener { _, hour, minute ->
            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)
            timeTextView.text = sdf.format(cal.time)
        }
        TimePickerDialog(
            requireContext(),
            timeSetListener,
            cal.get(Calendar.HOUR_OF_DAY),
            cal.get(Calendar.MINUTE),
            true
        ).show()
    }

    companion object {
        val sdf = SimpleDateFormat("HH:mm")
    }
}