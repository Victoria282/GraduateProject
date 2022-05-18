package com.example.graduateproject.authentication

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.constraintlayout.motion.widget.MotionLayout
import com.example.graduateproject.databinding.SplashScreenLayoutBinding
import dagger.android.support.DaggerAppCompatActivity

@SuppressLint("CustomSplashScreen")
class SplashScreen : DaggerAppCompatActivity() {

    private lateinit var binding: SplashScreenLayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SplashScreenLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSplashScreen()
    }

    private fun setSplashScreen() = binding.motionLayout.addTransitionListener(
        object : MotionLayout.TransitionListener {
            override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {}

            override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) {}

            override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {
                startActivity(Intent(this@SplashScreen, MainActivity::class.java))
                finish()
            }

            override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {}
        })
}