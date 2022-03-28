package com.example.graduateproject.schedule.lessons

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.graduateproject.R
import com.example.graduateproject.databinding.FragmentLessonsBinding
import com.example.graduateproject.di.utils.ViewModelFactory
import com.example.graduateproject.schedule.adapter.LessonsAdapter
import com.example.graduateproject.schedule.main.ScheduleFragmentDirections
import com.example.graduateproject.schedule.model.Lesson
import java.util.*
import javax.inject.Inject

class LessonsFragment @Inject constructor(

) : Fragment(R.layout.fragment_lessons), LessonsAdapter.LessonClickListener {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var binding: FragmentLessonsBinding
    private val viewModel: LessonsViewModel by viewModels { viewModelFactory }
    private lateinit var adapter: LessonsAdapter

    var lessons: ArrayList<Lesson>? = null

    // Добавить frgament result api (при добавлении пары), вызывать метод adapter.updateLessons

    override fun onLessonClick(lesson: Lesson?) {
        val direction = ScheduleFragmentDirections.toLessonsEditor(lesson)
        findNavController().navigate(direction)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLessonsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
    }

    private fun initUi() = with(binding) {
        if (arguments != null)
            lessons = arguments!!.getParcelableArrayList("listLessons")

        if (lessons != null) {
            if (lessons!!.isEmpty())
                binding.messageEmptyListLessons.visibility = View.VISIBLE
            else {
                initLessonsAdapter(lessons)
            }
        }
        addLesson.setOnClickListener {
            onLessonClick(null)
        }
    }

    private fun initLessonsAdapter(
        lessons: ArrayList<Lesson>?
    ) = with(binding) {
        listOfLessons.layoutManager = LinearLayoutManager(context)
        adapter = LessonsAdapter(lessons!!)
        listOfLessons.addItemDecoration(
            DividerItemDecoration(
                listOfLessons.context,
                (listOfLessons.layoutManager as LinearLayoutManager).orientation
            )
        )
        listOfLessons.adapter = adapter
        adapter.clickListener = this@LessonsFragment
    }

    companion object {
        fun getInstance() = LessonsFragment()
    }
}