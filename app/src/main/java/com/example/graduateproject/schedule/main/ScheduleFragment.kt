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
import com.example.graduateproject.shared_preferences.Storage
import com.example.graduateproject.utils.Constants.MONDAY
import com.example.graduateproject.utils.Utils
import com.google.android.material.bottomsheet.BottomSheetDialog
import javax.inject.Inject

class ScheduleFragment @Inject constructor(
    viewModelFactory: ViewModelFactory
) : Fragment(R.layout.schedule_fragment),
    LessonsAdapter.LessonClickListener,
    View.OnClickListener {
    private val viewModel: ScheduleViewModel by viewModels { viewModelFactory }
    private lateinit var binding: ScheduleFragmentBinding
    private lateinit var lessonsAdapter: LessonsAdapter
    private var counter: Int = 0
    private var lessonsList: ArrayList<Lesson> = ArrayList()

    private val lessonsObserver = Observer<List<Lesson>?> { list ->
        list.forEach {
            if (!lessonsList.contains(it)) {
                lessonsList.add(it)
                if (accordanceLesson(it))
                    lessonsAdapter.updateLessons(it)
            }
        }
    }

    private val weekDayObserver = Observer<String> {
        initLessonsAdapter()
        lessonsList.forEach {
            if (accordanceLesson(it))
                lessonsAdapter.updateLessons(it)
        }
        if (lessonsList.isNotEmpty()) checkLessonsExists()
    }

    private fun accordanceLesson(lesson: Lesson): Boolean {
        val flag = lesson.weekDay == Storage.weekDay && lesson.week == Storage.studyWeek
        if (flag) counter++
        return flag
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.delete, menu);
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        if (item.itemId == R.id.deleteSchedule) showBottomNavigation()
        return super.onOptionsItemSelected(item)
    }

    private fun showBottomNavigation() {
        val dialog = BottomSheetDialog(requireContext())

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.bottom_sheet_delete_schedule)

        val delete = dialog.findViewById<Button>(R.id.delete)
        delete?.setOnClickListener {
            viewModel.deleteSchedule()
            lessonsAdapter.clearLessons()
            lessonsList.clear()
            dialog.dismiss()
        }
        dialog.show()
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
        checkWeekDay(savedInstanceState)
        return binding.root
    }

    private fun checkWeekDay(savedInstanceState: Bundle?) {
        if (savedInstanceState == null)
            setWeekDay(MONDAY)
        else
            setWeekDay(Storage.weekDay!!)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getLessons()
        if (!Storage.scheduleOnBoarding) showOnBoarding()
        initObservers()
        initListeners()
    }

    private fun checkLessonsExists() = with(binding) {
        val visibilityFlag = if (counter == 0) View.VISIBLE else View.GONE

        weekend.visibility = visibilityFlag
        iconWeekend.visibility = visibilityFlag
        counter = 0
    }

    private fun initObservers() = with(viewModel) {
        weekDay.observe(viewLifecycleOwner, weekDayObserver)
        lessons.observe(viewLifecycleOwner, lessonsObserver)
    }

    private fun initLessonsAdapter() = with(binding.listOfLessons) {
        layoutManager = LinearLayoutManager(context)
        lessonsAdapter = LessonsAdapter(arrayListOf())
        addItemDecoration(
            DividerItemDecoration(
                context,
                (layoutManager as LinearLayoutManager).orientation
            )
        )
        adapter = lessonsAdapter
        lessonsAdapter.clickListener = this@ScheduleFragment
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

        binding.addLesson.setOnClickListener {
            onLessonClick(null)
        }
    }

    override fun onResume() {
        super.onResume()
        setWeekDay(Storage.weekDay!!)
    }

    override fun onClick(button: View?) = setWeekDay((button as Button).text.toString())

    private fun setWeekDay(weekday: String) = viewModel.setWeekDay(weekday)

    private fun showOnBoarding() = Utils.showOnBoarding(
        requireActivity(),
        binding.addLesson,
        R.string.welcome_subtitle_message_schedule,
        requireContext()
    ) {
        Storage.scheduleOnBoarding = true
    }
}