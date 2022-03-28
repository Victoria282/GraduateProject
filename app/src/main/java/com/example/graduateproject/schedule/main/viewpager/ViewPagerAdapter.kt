package com.example.graduateproject.schedule.main.viewpager

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.graduateproject.schedule.lessons.LessonsFragment
import com.example.graduateproject.schedule.lessons.usecase.FakeLessons
import com.example.graduateproject.schedule.model.Lesson

class ViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    private var titles: MutableList<String> = ArrayList()
    var currentWeek = 1
    private var countPages = 0
    // здесь добавить юзкейс, получать из БД по индексу расписание на день
    constructor (
        fragment: Fragment,
        tabTitles: MutableList<String>,
        currentWeek: Int
    ) : this(fragment) {
        titles = tabTitles
        this.countPages = tabTitles.size
        this.currentWeek = currentWeek
    }

    override fun getItemCount(): Int {
        return countPages
    }

    override fun createFragment(position: Int): Fragment {
        val bundle = Bundle()
        val data: ArrayList<Lesson> = if (currentWeek == 1) {
            FakeLessons.fakeData1week
        } else {
            FakeLessons.fakeData2week
        }
        bundle.putParcelableArrayList("listLessons", data)
        val fragment: Fragment = LessonsFragment.getInstance()
        fragment.arguments = bundle
        return fragment
    }
}