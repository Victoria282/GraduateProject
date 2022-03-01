package com.example.graduateproject.authentication

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.example.graduateproject.databinding.SplashScreenLayoutBinding
import com.example.graduateproject.di.utils.FragmentFactory
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

@SuppressLint("CustomSplashScreen")
class SplashScreen : DaggerAppCompatActivity() {

    private lateinit var binding: SplashScreenLayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SplashScreenLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSplashScreen()
    }

    private fun setSplashScreen() {
        Handler().postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, 3000)
    }
}