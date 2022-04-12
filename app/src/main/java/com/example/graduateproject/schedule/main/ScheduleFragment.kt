package com.example.graduateproject.schedule.main

import android.os.Bundle
import android.view.*
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
import javax.inject.Inject

class ScheduleFragment @Inject constructor(
    viewModelFactory: ViewModelFactory
) : Fragment(R.layout.schedule_fragment),
    LessonsAdapter.LessonClickListener,
    View.OnClickListener {
    private val viewModel: ScheduleViewModel by viewModels { viewModelFactory }
    private lateinit var binding: ScheduleFragmentBinding
    private lateinit var adapter: LessonsAdapter

    private var lessonsList: ArrayList<Lesson> = ArrayList()

    private val lessonsObserver = Observer<List<Lesson>> { list ->
        list.forEach {
            if (!lessonsList.contains(it))
                lessonsList.add(it)
            if (checkWeekPositionDay(it))
                adapter.updateLessons(it)
        }
    }

    private val weekDayObserver = Observer<Int> {
        initLessonsAdapter()
        lessonsList.forEach {
            if (checkWeekPositionDay(it)) {
                adapter.updateLessons(it)
            }
        }
    }

    private fun checkWeekPositionDay(lesson: Lesson):Boolean {
        return lesson.positionOfWeekDay == SharedPreferences.savedWeekDay && lesson.countOfWeek == SharedPreferences.savedStudyWeek
    }

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
        initDefaultDay()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
        initUi()
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

    private fun setStudyWeek(number: Int) = with(viewModel) {
        saveStudyWeek(number)
    }

    override fun onLessonClick(lesson: Lesson?) {
        val direction = ScheduleFragmentDirections.toLessonsEditor(lesson)
        findNavController().navigate(direction)
    }

    private fun initListeners() {
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

    private fun initDefaultDay() = viewModel.setWeekDay(1)

    override fun onClick(button: View?) {
        when (button?.id) {
            R.id.btn1 -> {
                viewModel.setWeekDay(1)
            }
            R.id.btn2 -> {
                viewModel.setWeekDay(2)
            }
            R.id.btn3 -> {
                viewModel.setWeekDay(3)
            }
            R.id.btn4 -> {
                viewModel.setWeekDay(4)
            }
            R.id.btn5 -> {
                viewModel.setWeekDay(5)
            }
            R.id.btn6 -> {
                viewModel.setWeekDay(6)
            }
        }
    }
}