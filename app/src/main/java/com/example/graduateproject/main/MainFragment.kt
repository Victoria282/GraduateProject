package com.example.graduateproject.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.graduateproject.R
import com.example.graduateproject.authentication.firebase.Firebase
import com.example.graduateproject.databinding.FragmentMainScreenBinding
import com.example.graduateproject.di.utils.ViewModelFactory
import com.example.graduateproject.rate.RateDialog
import com.example.graduateproject.shared_preferences.SharedPreferences
import javax.inject.Inject

class MainFragment @Inject constructor(
    viewModelFactory: ViewModelFactory,
    val firebase: Firebase
) : Fragment(R.layout.fragment_main_screen) {

    lateinit var rateDialog: RateDialog
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
        rateDialog = RateDialog(requireContext(), firebase)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkReviewAppExisting()
    }

    private fun checkReviewAppExisting() {
        if (SharedPreferences.visitingApp == 3 && !SharedPreferences.rateUs) {
            rateDialog.setCancelable(true)
            rateDialog.show()
        } else if (SharedPreferences.visitingApp < 3) {
            SharedPreferences.visitingApp++
        }
    }
}