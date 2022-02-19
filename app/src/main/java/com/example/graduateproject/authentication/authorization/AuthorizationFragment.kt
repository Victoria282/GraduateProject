package com.example.graduateproject.authentication.authorization

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.graduateproject.R
import com.example.graduateproject.authentication.validation.Validation
import com.example.graduateproject.databinding.AuthorizationLayoutBinding
import com.example.graduateproject.di.utils.ViewModelFactory
import com.example.graduateproject.main.MainPageAccount
import com.example.graduateproject.utils.Utils.showMessage
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import javax.inject.Inject

/* TODO в будущем:
    1. api quotes для SplashScreen
    2. Вынести общее из фрагментов Auth Register Restore -> BaseFragment
    4. * Реализовать сохранение логина и пароля в shared preferences
*/

class AuthorizationFragment @Inject constructor(
    viewModelFactory: ViewModelFactory
): Fragment(R.layout.authorization_layout) {

    private lateinit var binding: AuthorizationLayoutBinding

    private val viewModel: AuthorizationViewModel by viewModels { viewModelFactory }

    private val statusAuthorizationObserver = Observer<Task<AuthResult>> { authResult ->
        authResult.addOnCompleteListener {

            hideProgressBar()

            if (authResult.isSuccessful)
                Intent(requireContext(), MainPageAccount::class.java).also {
                    it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(it)
                }
            else
                when (authResult.exception) {
                    is FirebaseAuthInvalidCredentialsException ->
                        showMessage(
                            R.string.message_invalid_auth_data,
                            requireContext()
                        )
                    else -> showMessage(
                        R.string.message_something_went_wrong,
                        requireContext()
                    )
                }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        initListeners()
        initObservers()
    }

    private fun checkUserAuthorization() {
        if (viewModel.checkUserAuthorization())
            startActivity(Intent(requireContext(), MainPageAccount::class.java))
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

        validateLoginForm(email, password)
    }

    private fun validateLoginForm(email: String, password: String) {

        val resultValidation: Int = Validation.validateInputText(email, password, null)

        when(resultValidation) {
            1 -> {
                viewModel.loginUser(email, password)
                showProgressBar()
            }
            0 -> {
                showMessage(resultValidation, requireContext())
            }
        }
    }

    private fun hideProgressBar() {
        binding.progressBarLayout.progressBar.visibility = View.GONE
    }

    private fun showProgressBar() {
        binding.progressBarLayout.progressBar.visibility = View.GONE
    }
}

