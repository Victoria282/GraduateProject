package com.example.graduateproject.authentication.registration

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.graduateproject.R
import com.example.graduateproject.authentication.DaggerBaseFragment
import com.example.graduateproject.databinding.RegistrationLayoutBinding
import com.example.graduateproject.di.utils.ViewModelFactory
import com.example.graduateproject.menu.MenuActivity
import com.example.graduateproject.shared_preferences.SharedPreferences
import com.example.graduateproject.utils.Utils
import com.example.graduateproject.utils.Utils.showMessage
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import javax.inject.Inject

class RegistrationFragment @Inject constructor() :
    DaggerBaseFragment(R.layout.registration_layout) {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var binding: RegistrationLayoutBinding

    private val viewModel: RegistrationViewModel by viewModels { viewModelFactory }

    private val statusRegistrationObserver = Observer<Task<AuthResult>> { authResult ->
        authResult.addOnCompleteListener {

            hideProgressBar()

            if (it.isSuccessful) enterApp()
            else when (authResult.exception) {
                is FirebaseAuthWeakPasswordException -> showMessage(
                    R.string.message_weak_password,
                    requireContext()
                )
                is FirebaseAuthUserCollisionException -> showMessage(
                    R.string.message_user_with_such_email_exists,
                    requireContext()
                )
                else -> showMessage(
                    R.string.something_went_wrong,
                    requireContext()
                )
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = RegistrationLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTittle()
        initListeners()
        initObservers()
        emailFocusListener()
        passwordFocusListener()
    }

    private fun enterApp() = Intent(requireContext(), MenuActivity::class.java).also {
        it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(it)
    }

    private fun emailFocusListener() = with(binding) {
        email.setOnFocusChangeListener { _, focused ->
            if (!focused) textFieldEmail.helperText = emailValidate()
        }
    }

    private fun emailValidate(): String? = with(binding) {
        if (!Utils.isCorrectEmail(email.text.toString()))
            return getString(R.string.message_email_incorrect)
        return null
    }

    private fun passwordFocusListener() = with(binding) {
        password.setOnFocusChangeListener { _, focused ->
            if (!focused) textFieldPassword.helperText = passwordValidate()
        }
    }

    private fun passwordValidate(): String? = with(binding) {
        if (password.text.toString().length < 6)
            return getString(R.string.message_weak_password)
        return null
    }

    private fun setTittle() {
        activity?.title = "Регистрация"
    }

    private fun initObservers() = with(viewModel) {
        statusRegistration.observe(viewLifecycleOwner, statusRegistrationObserver)
    }

    private fun initListeners() = with(binding) {
        loginBtn.setOnClickListener {
            val direction = RegistrationFragmentDirections.toAuthorization()
            findNavController().navigate(direction)
        }

        registerButton.setOnClickListener { getInputData() }
    }

    private fun getInputData() = with(binding) {
        val email = email.text?.trim().toString()
        val inputPassword = password.text?.trim().toString()
        val inputConfirmPassword = confirmPassword.text?.trim().toString()

        val messageEmptyFields = getString(R.string.not_empty_fields)
        val messageDifferentPasswords = getString(R.string.message_password_is_difficult)

        when {
            email.isEmpty() -> textFieldEmail.helperText = messageEmptyFields
            inputPassword.isEmpty() -> textFieldPassword.helperText = messageEmptyFields
            inputConfirmPassword.isEmpty() -> textFieldPasswordConfirm.helperText = messageEmptyFields
            password.text.toString() != confirmPassword.text.toString() ->
                textFieldPasswordConfirm.helperText = messageDifferentPasswords
            else -> {
                viewModel.registerUser(email, inputPassword)
                SharedPreferences.savedPassword = inputPassword
                clearHelperText()
                showProgressBar()
            }
        }
    }

    private fun clearHelperText() = with(binding) {
        textFieldEmail.helperText = ""
        textFieldPassword.helperText = ""
        textFieldPasswordConfirm.helperText = ""
    }

    private fun hideProgressBar() = with(binding) {
        progressBarLayout.progressBar.visibility = View.GONE
    }

    private fun showProgressBar() = with(binding) {
        progressBarLayout.progressBar.visibility = View.VISIBLE
    }
}