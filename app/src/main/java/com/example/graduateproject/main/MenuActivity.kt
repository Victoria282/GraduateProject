package com.example.graduateproject.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.ui.*
import com.example.graduateproject.R
import com.example.graduateproject.authentication.MainActivity
import com.example.graduateproject.databinding.ActivityMenuBinding
import com.example.graduateproject.di.utils.FragmentFactory
import com.example.graduateproject.di.utils.ViewModelFactory
import com.example.graduateproject.mainScreen.MainViewModel
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

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_menu)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}