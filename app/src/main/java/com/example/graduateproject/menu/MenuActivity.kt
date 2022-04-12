package com.example.graduateproject.menu

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.widget.TextView
import androidx.activity.viewModels
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.graduateproject.R
import com.example.graduateproject.authentication.MainActivity
import com.example.graduateproject.databinding.ActivityMenuBinding
import com.example.graduateproject.di.utils.FragmentFactory
import com.example.graduateproject.di.utils.ViewModelFactory
import com.example.graduateproject.shared_preferences.SharedPreferences
import com.example.graduateproject.utils.Constants.FIRST_WEEK
import com.example.graduateproject.utils.Constants.SECOND_WEEK
import com.google.android.material.navigation.NavigationView
import dagger.android.AndroidInjection
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class MenuActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var fragmentFactory: FragmentFactory

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

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

        navView.menu.findItem(R.id.log_out).setOnMenuItemClickListener {
            logOut()
            true
        }

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home,
                R.id.nav_schedule,
                R.id.notesFragment,
                R.id.mapFragment
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
        if (SharedPreferences.savedStudyWeek == FIRST_WEEK) {
            menu.findItem(R.id.switchWeek).title = resources.getString(R.string.first_week)
        } else if (SharedPreferences.savedStudyWeek == SECOND_WEEK) {
            menu.findItem(R.id.switchWeek).title = resources.getString(R.string.second_week)
        }
    }

    private fun logOut() {
        viewModel.logOut()
        Intent(this, MainActivity::class.java).also {
            it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(it)
        }
        finish()
    }
}