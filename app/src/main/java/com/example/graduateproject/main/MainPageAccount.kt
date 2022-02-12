package com.example.graduateproject.main

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.graduateproject.R
import com.example.graduateproject.authentication.MainActivity
import com.example.graduateproject.databinding.MainPageAccountLayoutBinding

class MainPageAccount : AppCompatActivity() {

    private lateinit var viewModel: MainPageAccountViewModel
    private lateinit var binding: MainPageAccountLayoutBinding
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainPageAccountLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this).get(MainPageAccountViewModel::class.java)
        initUI()
    }

    override fun onStart() {
        super.onStart()
        initListeners()
    }

    private fun initUI() {
        toggle = ActionBarDrawerToggle(this, binding.drawerLayout, R.string.open, R.string.close)
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.navigationView.setNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.nav_home -> {}
                R.id.settings -> {}
                R.id.log_out -> logOutUser()
                R.id.nav_schedule -> {}
                R.id.nav_about_developer -> {}
            }
            true
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
        // TODO Показать сообщение (Вы уверены, что хотите выйти? 'да' / 'отмена')
        viewModel.logOutUser()
        Intent(this, MainActivity::class.java).also {
            startActivity(it)
        }
    }
}