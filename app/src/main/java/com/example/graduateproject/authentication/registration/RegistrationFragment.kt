package com.example.graduateproject.authentication.registration

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

            when (authResult.exception) {
                is FirebaseAuthWeakPasswordException -> showMessage(
                    R.string.message_weak_password,
                    requireContext()
                )
                is FirebaseAuthUserCollisionException -> showMessage(
                    R.string.message_user_with_such_email_exists,
                    requireContext()
                )
                else -> showMessage(
                    R.string.message_something_went_wrong,
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
        if (password.text.toString().length < 8)
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
        val password = password.text?.trim().toString()
        val confirmPassword = confirmPassword.text?.trim().toString()

        val messageEmptyFields = getString(R.string.not_empty_fields)
        val messageDifferencePasswords = getString(R.string.message_password_is_difficult)

        when {
            email.isEmpty() -> textFieldEmail.helperText = messageEmptyFields
            password.isEmpty() -> textFieldPassword.helperText = messageEmptyFields
            confirmPassword.isEmpty() -> textFieldPasswordConfirm.helperText = messageEmptyFields
            binding.password.text.toString() != binding.confirmPassword.text.toString() ->
                textFieldPasswordConfirm.helperText = messageDifferencePasswords
            else -> {
                clearHelperText()
                viewModel.registerUser(email, password)
                SharedPreferences.savedPassword = password
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