package com.example.graduateproject.schedule.main

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.graduateproject.R
import com.example.graduateproject.databinding.ScheduleFragmentBinding
import com.example.graduateproject.di.utils.ViewModelFactory
import com.example.graduateproject.shared_preferences.SharedPreferences
import com.example.graduateproject.utils.Constants.FIRST_WEEK
import com.example.graduateproject.utils.Constants.SECOND_WEEK
import javax.inject.Inject

class ScheduleFragment @Inject constructor(
    viewModelFactory: ViewModelFactory
) : Fragment(R.layout.schedule_fragment) {

    private lateinit var binding: ScheduleFragmentBinding
    private val viewModel: ScheduleViewModel by viewModels { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStudyWeek(SharedPreferences.savedStudyWeek)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ScheduleFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        if (SharedPreferences.savedStudyWeek == FIRST_WEEK) {
            menu.findItem(R.id.switchWeek).title = resources.getString(R.string.first_week)
        } else if (SharedPreferences.savedStudyWeek == SECOND_WEEK) {
            menu.findItem(R.id.switchWeek).title = resources.getString(R.string.second_week)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        if (item.title == resources.getString(R.string.first_week)) {
            setStudyWeek(SECOND_WEEK)
            item.title = resources.getString(R.string.second_week)
        } else if (item.title == resources.getString(R.string.second_week)) {
            setStudyWeek(FIRST_WEEK)
            item.title = resources.getString(R.string.first_week)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setStudyWeek(number: Int) = with(viewModel) {
        saveStudyWeek(number)
        SharedPreferences.savedStudyWeek = number
    }
}