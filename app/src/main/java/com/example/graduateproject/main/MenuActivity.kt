package com.example.graduateproject.main

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.activity.viewModels
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.graduateproject.R
import com.example.graduateproject.databinding.ActivityMenuBinding
import com.example.graduateproject.di.utils.FragmentFactory
import com.example.graduateproject.di.utils.ViewModelFactory
import com.example.graduateproject.shared_preferences.SharedPreferences
import com.google.android.material.navigation.NavigationView
import dagger.android.AndroidInjection
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class MenuActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var fragmentFactory: FragmentFactory

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMenuBinding
    private val viewModel: MenuViewModel by viewModels { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportFragmentManager.fragmentFactory = fragmentFactory
        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AndroidInjection.inject(this)

        initNavigationView()
        initNavigationViewHeader()
    }

    private fun initNavigationView() {
        setSupportActionBar(binding.appBarMenu.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_menu)

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home,
                R.id.nav_schedule
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    private fun initNavigationViewHeader() {
        val header = binding.navView.getHeaderView(0)
        val email = header.findViewById<TextView>(R.id.userEmail)
        email.text = viewModel.getUserInfo()?.email
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_menu)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)

        checkStudyWeek(menu)
        return true
    }

    private fun checkStudyWeek(menu: Menu) {
        if (sharedPreferences.savedStudyWeek == FIRST_WEEK) {
            menu.findItem(R.id.switchWeek).title = resources.getString(R.string.first_week)
        } else if (sharedPreferences.savedStudyWeek == SECOND_WEEK) {
            menu.findItem(R.id.switchWeek).title = resources.getString(R.string.second_week)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }

    companion object {
        private const val FIRST_WEEK = 1
        private const val SECOND_WEEK = 2
    }
}