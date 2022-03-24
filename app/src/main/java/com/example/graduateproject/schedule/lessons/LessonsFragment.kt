package com.example.graduateproject.schedule.lessons

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.graduateproject.R
import com.example.graduateproject.databinding.FragmentLessonsBinding
import com.example.graduateproject.di.utils.ViewModelFactory
import javax.inject.Inject

class LessonsFragment @Inject constructor(
    viewModelFactory: ViewModelFactory
) : Fragment(R.layout.fragment_lessons) {

    private lateinit var binding: FragmentLessonsBinding
    private val viewModel: LessonsViewModel by viewModels { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLessonsBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
    }
}