package com.example.graduateproject.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.graduateproject.databinding.FragmentMainScreenBinding
import com.example.graduateproject.di.utils.ViewModelFactory
import javax.inject.Inject

class MainFragment @Inject constructor(
    viewModelFactory: ViewModelFactory
) : Fragment() {

    private lateinit var binding: FragmentMainScreenBinding
    private val viewModel: MainViewModel by viewModels { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainScreenBinding.inflate(layoutInflater, container, false)
        return binding.root
    }
}