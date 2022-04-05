package com.example.graduateproject.schedule.main

import android.os.Bundle
import android.view.*
import androidx.fragment.app.*
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.graduateproject.R
import com.example.graduateproject.databinding.ScheduleFragmentBinding
import com.example.graduateproject.di.utils.ViewModelFactory
import com.example.graduateproject.schedule.database.DatabaseViewModel
import com.example.graduateproject.schedule.lessons.LessonsFragment
import com.example.graduateproject.schedule.lessons.usecase.FakeLessons
import com.example.graduateproject.schedule.model.Lesson
import com.example.graduateproject.shared_preferences.SharedPreferences
import com.example.graduateproject.utils.Constants.FIRST_WEEK
import com.example.graduateproject.utils.Constants.SECOND_WEEK
import com.example.graduateproject.utils.Utils.onToday
import com.google.android.material.tabs.TabLayoutMediator
import javax.inject.Inject


class ScheduleFragment @Inject constructor(
    viewModelFactory: ViewModelFactory
) : Fragment(R.layout.schedule_fragment) {

    private lateinit var binding: ScheduleFragmentBinding
    private val viewModel: ScheduleViewModel by viewModels { viewModelFactory }
    private lateinit var viewModelDatabase: DatabaseViewModel
    private lateinit var viewPager: ViewPager
    private var lessonsList: ArrayList<Lesson> = arrayListOf()

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
        viewModelDatabase = ViewModelProvider(this)[DatabaseViewModel::class.java]
        initObservers()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
    }

    private fun initListeners() = with(binding) {
        addLesson.setOnClickListener {
            val direction = ScheduleFragmentDirections.toLessonsAdd()
            findNavController().navigate(direction)
        }
    }

    private fun initObservers() = with(viewModelDatabase) {
        lessons.observe(viewLifecycleOwner, {
            lessonsList.addAll(it)
            initPagerAdapter()
        })
    }

    private fun initPagerAdapter() {
        viewPager = ViewPager(lessonsList, requireActivity())
        binding.viewPager.adapter = viewPager
        TabLayoutMediator(binding.tabLayout, binding.viewPager, tabConfigurator).attach()
        binding.viewPager.setCurrentItem(onToday(), false)
    }

    private val tabConfigurator = TabLayoutMediator.TabConfigurationStrategy { tab, position ->
        tab.text = tabTitles[position]
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

    class ViewPager(lessons: List<Lesson>?, fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
        private val lessonsCurrentDay: Map<Int, List<Lesson>> = lessons?.takeIf { true }!!.groupBy { it.positionOfWeekDay }

        override fun getItemCount() = 6

        override fun createFragment(position: Int): Fragment {
            SharedPreferences.savedWeekDay = position
            return if(lessonsCurrentDay[position] != null && lessonsCurrentDay.isNotEmpty()) {
                val timetable: ArrayList<Lesson> = lessonsCurrentDay[position] as ArrayList<Lesson>
                val bundle = Bundle()
                bundle.putParcelableArrayList("listLessons", timetable)
                val fragment: Fragment = LessonsFragment.getInstance()
                fragment.arguments = bundle
                fragment
            } else
                LessonsFragment.getInstance()
        }
    }

    companion object {
        private val tabTitles: MutableList<String> = arrayListOf(
            "Понедельник", "Вторник", "Среда", "Четверг", "Пятница", "Суббота"
        )
    }
}