package com.example.graduateproject.schedule.lessonsEditor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.graduateproject.R
import com.example.graduateproject.databinding.FragmentLessonsEditorBinding
import com.example.graduateproject.di.utils.ViewModelFactory
import javax.inject.Inject

class LessonsEditorFragment @Inject constructor(
    viewModelFactory: ViewModelFactory
) : Fragment(R.layout.fragment_lessons_editor) {

    private lateinit var binding: FragmentLessonsEditorBinding
    private val viewModel: LessonsEditorViewModel by viewModels { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLessonsEditorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
    }

    private fun initUi() {
        setTittle()
    }

    private fun setTittle() {
        activity?.title = "Расписание занятий"
    }

    companion object {

    }
}