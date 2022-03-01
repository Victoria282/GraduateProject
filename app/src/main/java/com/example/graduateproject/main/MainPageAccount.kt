package com.example.graduateproject.main

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import com.example.graduateproject.R
import com.example.graduateproject.authentication.MainActivity
import com.example.graduateproject.databinding.MainPageAccountLayoutBinding
import com.example.graduateproject.di.utils.ViewModelFactory
import javax.inject.Inject

class MainPageAccount : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel: MainPageAccountViewModel by viewModels { viewModelFactory }

    private lateinit var binding: MainPageAccountLayoutBinding

    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainPageAccountLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()
        initListeners()
        initUI()
    }

    private fun initUI() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        toggle = ActionBarDrawerToggle(
            this, binding.drawerLayout, R.string.open, R.string.close
        ).also { it.syncState() }

        with(binding) {

            drawerLayout.addDrawerListener(toggle)

            navigationView.setNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.nav_home -> {
                    }
                    R.id.settings -> {
                    }
                    R.id.log_out -> logOutUser()
                    R.id.nav_schedule -> {
                    }
                    R.id.nav_about_developer -> {
                    }
                }
                true
            }
        }
    }

    private fun initListeners() = with(binding) {

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item))
            return true
        return super.onOptionsItemSelected(item)
    }

    private fun logOutUser() {
        viewModel.logOutUser()
        Intent(this, MainActivity::class.java).also {
            startActivity(it)
        }
    }
}