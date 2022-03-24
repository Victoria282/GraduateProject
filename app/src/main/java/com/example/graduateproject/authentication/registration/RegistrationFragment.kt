package com.example.graduateproject.authentication.registration

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.graduateproject.R
import com.example.graduateproject.authentication.authorization.AuthorizationFragment
import com.example.graduateproject.authentication.authorization.AuthorizationFragmentDirections
import com.example.graduateproject.authentication.validation.Validation
import com.example.graduateproject.databinding.RegistrationLayoutBinding
import com.example.graduateproject.di.utils.ViewModelFactory
import com.example.graduateproject.main.MenuActivity
import com.example.graduateproject.utils.Utils.showMessage
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import javax.inject.Inject

class RegistrationFragment @Inject constructor(
    viewModelFactory: ViewModelFactory
): Fragment(R.layout.registration_layout) {

    private lateinit var binding: RegistrationLayoutBinding

    private val viewModel: RegistrationViewModel by viewModels { viewModelFactory }

    private val statusRegistrationObserver = Observer<Task<AuthResult>> { authResult ->
        authResult.addOnCompleteListener {

            hideProgressBar()

            if (authResult.isSuccessful)
                Intent(requireContext(), MenuActivity::class.java).also {
                    it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(it)
                }
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
        val email = email.text.toString()
        val password = password.text.toString()
        val confirmPassword = confirmPassword.text.toString()

        validateRegisterForm(email, password, confirmPassword)
    }

    private fun validateRegisterForm(email: String, password: String, confirmPassword: String) {

        val resultValidation: Int = Validation.validateInputText(email, password, confirmPassword)

        when(resultValidation) {
            1 -> {
                showProgressBar()
                viewModel.registerUser(email, password)
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
        binding.progressBarLayout.progressBar.visibility = View.VISIBLE
    }
}