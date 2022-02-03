package com.example.graduateproject.authentication.registration

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.graduateproject.R
import com.example.graduateproject.authentication.authorization.AuthorizationFragment
import com.example.graduateproject.authentication.validation.Validation
import com.example.graduateproject.databinding.RegistrationLayoutBinding
import com.example.graduateproject.main.MainPageAccount
import com.example.graduateproject.utils.Utils.showMessage
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException

class RegistrationFragment : Fragment(R.layout.registration_layout) {

    private lateinit var binding: RegistrationLayoutBinding
    private lateinit var viewModel: RegistrationViewModel

    private val statusRegistrationObserver = Observer<Task<AuthResult>> { authResult ->
        if (authResult.isSuccessful) {
            Intent(requireContext(), MainPageAccount::class.java).also {
                startActivity(it)
            }
        } else
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = RegistrationLayoutBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(RegistrationViewModel::class.java)
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
            parentFragmentManager.beginTransaction()
                .setCustomAnimations(
                    R.anim.to_left_in,
                    R.anim.to_left_out,
                    R.anim.to_right_in,
                    R.anim.to_right_out
                )
                .replace(R.id.navHostMainActivity, AuthorizationFragment())
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit()
        }
        registerButton.setOnClickListener {
            getInputData()
        }
    }

    private fun getInputData() = with(binding) {
        val email = email.text.toString()
        val password = password.text.toString()
        val confirmPassword = confirmPassword.text.toString()

        validateRegisterForm(email, password, confirmPassword)
    }

    private fun validateRegisterForm(email: String, password: String, confirmPassword: String) {
        val resultValidation: Int = Validation.validateInputText(email, password, confirmPassword)

        if (resultValidation == 1)
            viewModel.registerUser(email, password)
        else
            showMessage(resultValidation, requireContext())
    }
}