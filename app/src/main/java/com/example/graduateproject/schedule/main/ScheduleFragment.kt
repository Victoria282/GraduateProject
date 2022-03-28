package com.example.graduateproject.schedule.main

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.graduateproject.R
import com.example.graduateproject.databinding.ScheduleFragmentBinding
import com.example.graduateproject.di.utils.ViewModelFactory
import com.example.graduateproject.schedule.main.viewpager.ViewPagerAdapter
import com.example.graduateproject.shared_preferences.SharedPreferences
import com.google.android.material.tabs.TabLayoutMediator
import javax.inject.Inject

class ScheduleFragment @Inject constructor(
    viewModelFactory: ViewModelFactory
) : Fragment(R.layout.schedule_fragment) {

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    private lateinit var binding: ScheduleFragmentBinding
    private val viewModel: ScheduleViewModel by viewModels { viewModelFactory }
    private lateinit var viewPagerAdapter: ViewPagerAdapter

    private val studyWeek = Observer<Int> { week ->
        viewPagerAdapter = ViewPagerAdapter(this, tabTitles, week)
        binding.viewpager.adapter = viewPagerAdapter
        TabLayoutMediator(binding.tabLayout, binding.viewpager, tabConfigurator).attach()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkStudyWeek()
        initObservers()
        initUi()
    }

    private fun checkStudyWeek() {
        if (sharedPreferences.savedStudyWeek == FIRST_WEEK) {
            setStudyWeek(FIRST_WEEK)
        } else if (sharedPreferences.savedStudyWeek == SECOND_WEEK) {
            setStudyWeek(SECOND_WEEK)
        }
    }

    private fun initObservers() {
        viewModel.studyWeek.observe(viewLifecycleOwner, studyWeek)
    }

    private fun initUi() {
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        if (sharedPreferences.savedStudyWeek == FIRST_WEEK)
            menu.findItem(R.id.switchWeek).title = resources.getString(R.string.first_week)
        else if (sharedPreferences.savedStudyWeek == SECOND_WEEK)
            menu.findItem(R.id.switchWeek).title = resources.getString(R.string.second_week)
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

    private val tabConfigurator = TabLayoutMediator.TabConfigurationStrategy { tab, position ->
        tab.text = tabTitles[position]
    }

    private fun setStudyWeek(number: Int) = with(viewModel) {
        setStudyWeek(number)
    }

    override fun onPause() {
        super.onPause()
        sharedPreferences.savedStudyWeek = viewModel.studyWeek.value
    }

    companion object {
        private const val FIRST_WEEK = 1
        private const val SECOND_WEEK = 2

        private val tabTitles: MutableList<String> = arrayListOf(
            "Понедельник", "Вторник", "Среда", "Четверг", "Пятница", "Суббота"
        )
    }
}