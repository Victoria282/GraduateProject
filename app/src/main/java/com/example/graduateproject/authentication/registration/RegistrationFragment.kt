package com.example.graduateproject.authentication.registration

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.graduateproject.R
import com.example.graduateproject.databinding.RegistrationLayoutBinding
import com.example.graduateproject.di.utils.ViewModelFactory
import com.example.graduateproject.menu.MenuActivity
import com.example.graduateproject.utils.Utils
import com.example.graduateproject.utils.Utils.showMessage
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import javax.inject.Inject

class RegistrationFragment @Inject constructor(
    viewModelFactory: ViewModelFactory
) : Fragment(R.layout.registration_layout) {

    private lateinit var binding: RegistrationLayoutBinding

    private val viewModel: RegistrationViewModel by viewModels { viewModelFactory }

    private val statusRegistrationObserver = Observer<Task<AuthResult>> { authResult ->
        authResult.addOnCompleteListener {

            hideProgressBar()

            if (authResult.isSuccessful) enterApp()
            else
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

    private fun enterApp() =
        Intent(requireContext(), MenuActivity::class.java).also {
            it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(it)
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
        val messageDifficultPasswords = getString(R.string.message_password_is_difficult)

        when {
            email.isEmpty() -> textFieldEmail.helperText = messageEmptyFields
            password.isEmpty() -> textFieldPassword.helperText = messageEmptyFields
            confirmPassword.isEmpty() -> textFieldPasswordConfirm.helperText = messageEmptyFields
            binding.password.text.toString() != binding.confirmPassword.text.toString() ->
                textFieldPasswordConfirm.helperText = messageDifficultPasswords
            else -> {
                clearHelperText()
                viewModel.registerUser(email, password)
                showProgressBar()
            }
        }
    }

    private fun RegistrationLayoutBinding.clearHelperText() {
        textFieldEmail.helperText = ""
        textFieldPassword.helperText = ""
        textFieldPasswordConfirm.helperText = ""
    }

    private fun hideProgressBar() {
        binding.progressBarLayout.progressBar.visibility = View.GONE
    }

    private fun showProgressBar() {
        binding.progressBarLayout.progressBar.visibility = View.VISIBLE
    }
}