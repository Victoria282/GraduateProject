package com.example.graduateproject.schedule.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.graduateproject.R
import com.example.graduateproject.databinding.ItemLessonBinding
import com.example.graduateproject.schedule.model.Lesson

class LessonsAdapter(private val lessons: ArrayList<Lesson>) :
    RecyclerView.Adapter<LessonsAdapter.DataViewHolder>() {

    var clickListener: LessonClickListener? = null

    interface LessonClickListener {
        fun onLessonClick(lesson: Lesson?)
    }

    class DataViewHolder(
        private val binding: ItemLessonBinding,
        private val listener: LessonClickListener?
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(lesson: Lesson) {
            val typeOfLesson =
                if (lesson.typeOfLesson == 0) R.string.lesson_type_lecture else R.string.lesson_type_practice

            with(binding) {
                lessonTittle.text = lesson.subject
                lessonType.setText(typeOfLesson)
                lessonTeacher.text = lesson.teacher
                lessonCabinet.text = lesson.cabinet
                itemLesson.setOnClickListener {
                    listener?.onLessonClick(lesson)
                }
                startTime.text = lesson.startTime
                endTime.text = lesson.endTime
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemLessonBinding.inflate(
            inflater,
            parent,
            false
        )
        return DataViewHolder(binding, clickListener)
    }

    override fun getItemCount(): Int = lessons.size

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.bind(lessons[position])
    }

    fun clearAll() {
        this.lessons.clear()
    }

    fun updateLessons(lessons: Lesson) {
        this.lessons.apply {
            add(lessons)
        }
        notifyDataSetChanged()
    }
}
