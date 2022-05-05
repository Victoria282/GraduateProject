package com.example.graduateproject.rate

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.ImageView
import android.widget.Toast
import com.example.graduateproject.R
import com.example.graduateproject.authentication.firebase.Firebase
import com.example.graduateproject.databinding.RateUsDialogBinding
import com.example.graduateproject.shared_preferences.SharedPreferences
import javax.inject.Inject

class RateDialog @Inject constructor(
    context: Context,
    val firebase: Firebase
) : Dialog(context) {
    private lateinit var binding: RateUsDialogBinding
    private var appRating = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = RateUsDialogBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUi()
    }

    private fun initUi() = with(binding) {
        ratingBar.setOnRatingBarChangeListener { _, rating, _ ->
            if (rating <= 2) imageView.setImageResource(R.drawable.sad)
            else if (rating > 2 && rating <= 4) imageView.setImageResource(R.drawable.smile)
            else imageView.setImageResource(R.drawable.in_love)

            appRating = rating
            animateImage(imageView)
        }

        rateNowButton.setOnClickListener {
            val review = review.text.toString()
            sendFeedback(review)
            dismiss()
        }

        rateLaterButton.setOnClickListener {
            SharedPreferences.visitingApp = 1
            dismiss()
        }
    }

    private fun sendFeedback(review: String) = firebase.saveFeedback(appRating, review)
        .addOnSuccessListener {
            SharedPreferences.rateUs = true
            Toast.makeText(context, R.string.sending_success, Toast.LENGTH_SHORT).show()
        }
        .addOnFailureListener {
            Toast.makeText(context, R.string.something_went_wrong, Toast.LENGTH_SHORT).show()
        }

    private fun animateImage(imageView: ImageView) {
        val scaleAnnotation = ScaleAnimation(
            0f,
            1f,
            0f,
            1f,
            Animation.RELATIVE_TO_SELF,
            0.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f
        )
        with(scaleAnnotation) {
            fillAfter = true
            duration = 200
            imageView.startAnimation(this)
        }
    }
}