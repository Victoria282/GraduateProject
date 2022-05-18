package com.example.graduateproject.authentication.authorization

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
import com.example.graduateproject.databinding.AuthorizationLayoutBinding
import com.example.graduateproject.di.utils.ViewModelFactory
import com.example.graduateproject.menu.MenuActivity
import com.example.graduateproject.utils.Utils
import com.example.graduateproject.utils.Utils.showMessage
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import javax.inject.Inject

class AuthorizationFragment @Inject constructor(

) : DaggerBaseFragment(R.layout.authorization_layout) {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var binding: AuthorizationLayoutBinding

    private val viewModel: AuthorizationViewModel by viewModels { viewModelFactory }

    private val statusAuthorizationObserver = Observer<Task<AuthResult>> { authResult ->
        authResult.addOnCompleteListener {

            hideProgressBar()

            if (authResult.isSuccessful) enterApp()
            else when (authResult.exception) {
                is FirebaseAuthInvalidCredentialsException ->
                    showMessage(
                        R.string.message_invalid_auth_data,
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
        binding = AuthorizationLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkUserAuthorization()
        setTittle()
        emailFocusListener()
        initListeners()
        initObservers()
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

    private fun checkUserAuthorization() {
        if (viewModel.checkUserAuthorization()) enterApp()
    }

    private fun enterApp() = Intent(requireContext(), MenuActivity::class.java).also {
        it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(it)
    }

    private fun setTittle() {
        activity?.title = "Авторизация"
    }

    private fun initObservers() = with(viewModel) {
        statusAuthorization.observe(viewLifecycleOwner, statusAuthorizationObserver)
    }

    private fun initListeners() = with(binding) {
        registerLink.setOnClickListener {
            val direction = AuthorizationFragmentDirections.toRegistration()
            findNavController().navigate(direction)
        }

        loginButton.setOnClickListener { getInputData() }

        textViewRestorePassword.setOnClickListener {
            val direction = AuthorizationFragmentDirections.toRestore()
            findNavController().navigate(direction)
        }
    }

    private fun getInputData() = with(binding) {
        val email = email.text.toString()
        val password = password.text.toString()
        checkInputFields(email, password)
    }

    private fun checkInputFields(email: String, password: String) = with(binding) {
        val message = getString(R.string.not_empty_fields)
        when {
            email.isEmpty() -> textFieldEmail.helperText = message
            password.isEmpty() -> textFieldPassword.helperText = message
            else -> {
                viewModel.loginUser(email, password)
                clearHelperText()
                showProgressBar()
            }
        }
    }

    private fun clearHelperText() = with(binding) {
        textFieldEmail.helperText = ""
        textFieldPassword.helperText = ""
    }

    private fun hideProgressBar() = with(binding) {
        progressBarLayout.progressBar.visibility = View.GONE
    }

    private fun showProgressBar() = with(binding) {
        progressBarLayout.progressBar.visibility = View.GONE
    }
}

