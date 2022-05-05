package com.example.graduateproject.schedule.main

import android.os.Bundle
import android.view.*
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.graduateproject.R
import com.example.graduateproject.databinding.ScheduleFragmentBinding
import com.example.graduateproject.di.utils.ViewModelFactory
import com.example.graduateproject.schedule.adapter.LessonsAdapter
import com.example.graduateproject.schedule.model.Lesson
import com.example.graduateproject.shared_preferences.SharedPreferences
import com.example.graduateproject.utils.Utils
import javax.inject.Inject

class ScheduleFragment @Inject constructor(
    viewModelFactory: ViewModelFactory
) : Fragment(R.layout.schedule_fragment),
    LessonsAdapter.LessonClickListener,
    View.OnClickListener {
    private val viewModel: ScheduleViewModel by viewModels { viewModelFactory }
    private lateinit var binding: ScheduleFragmentBinding
    private lateinit var adapter: LessonsAdapter
    private var counter: Int = 0
    private var lessonsList: ArrayList<Lesson> = ArrayList()

    private val lessonsObserver = Observer<List<Lesson>> { list ->
        list.forEach {
            if (!lessonsList.contains(it)) lessonsList.add(it)
            if (checkWeekPositionDay(it)) adapter.updateLessons(it)
        }
    }

    private val weekDayObserver = Observer<String> {
        initLessonsAdapter()
        lessonsList.forEach {
            if (checkWeekPositionDay(it)) adapter.updateLessons(it)
        }
        if(lessonsList.isNotEmpty()) checkLessonsExists()
    }

    private fun checkWeekPositionDay(lesson: Lesson): Boolean {
        val flag = lesson.positionOfWeekDay == SharedPreferences.savedWeekDay && lesson.week == SharedPreferences.saveSwitchWeek
        if(flag) counter++
        return flag
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
        initDefaultDay()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (!SharedPreferences.scheduleOnBoarding) showOnBoarding()
        initObservers()
        initUi()
    }
    
    private fun checkLessonsExists() {
        val visibilityFlag = if(counter == 0) View.VISIBLE else View.GONE

        binding.weekend.visibility = visibilityFlag
        binding.iconWeekend.visibility = visibilityFlag
        counter = 0
    }

    private fun initUi() = with(binding) {
        initListeners()
        addLesson.setOnClickListener {
            onLessonClick(null)
        }
    }

    private fun initObservers() = with(viewModel) {
        weekDay.observe(viewLifecycleOwner, weekDayObserver)
        lessons.observe(viewLifecycleOwner, lessonsObserver)
    }

    private fun initLessonsAdapter() = with(binding) {
        listOfLessons.layoutManager = LinearLayoutManager(context)
        adapter = LessonsAdapter(arrayListOf())
        listOfLessons.addItemDecoration(
            DividerItemDecoration(
                listOfLessons.context,
                (listOfLessons.layoutManager as LinearLayoutManager).orientation
            )
        )
        listOfLessons.adapter = adapter
        adapter.clickListener = this@ScheduleFragment
    }

    override fun onLessonClick(lesson: Lesson?) {
        val direction = ScheduleFragmentDirections.toLessonsEditor(lesson)
        findNavController().navigate(direction)
    }

    private fun initListeners()  {
        binding.btn1.setOnClickListener(this)
        binding.btn2.setOnClickListener(this)
        binding.btn3.setOnClickListener(this)
        binding.btn4.setOnClickListener(this)
        binding.btn5.setOnClickListener(this)
        binding.btn6.setOnClickListener(this)
    }

    override fun onResume() {
        super.onResume()
        initDefaultDay()
    }

    private fun initDefaultDay() = viewModel.setWeekDay("Понедельник")

    override fun onClick(button: View?) {
        viewModel.setWeekDay((button as Button).text.toString())
    }

    private fun showOnBoarding() {
        Utils.showOnBoarding(
            requireActivity(),
            binding.addLesson,
            R.string.welcome_subtitle_message_schedule,
            requireContext()
        ) {
            SharedPreferences.scheduleOnBoarding = true
        }
    }
}