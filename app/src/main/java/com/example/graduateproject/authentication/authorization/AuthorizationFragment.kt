package com.example.graduateproject.authentication.authorization

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
import com.example.graduateproject.authentication.registration.RegistrationFragment
import com.example.graduateproject.databinding.AuthorizationLayoutBinding
import com.example.graduateproject.main.MainPageAccount
import com.example.graduateproject.utils.Utils.isCorrectEmail
import com.example.graduateproject.utils.Utils.showMessage
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException

/* TODO в будущем:
    1. Вынести в отдельный класс валидацию полей в фрагменте авторизации и регистрации
    2. Восстановить пароль от аккаунта -> письмо на указанную почту для восстановления +
    AlertDialog - забыли пароль -> с кнопкой перехода на фрагмент восстановления
    3. Поворот экрана, сохранение состояния
    4. Вынести в отдельный класс ответы Firebase (Передавать объект во фрагмент объект с овтетом)
    5. SplashScreen
    6. Session Class
    7. Dagger2
*/

class AuthorizationFragment : Fragment(R.layout.authorization_layout) {

    private lateinit var binding: AuthorizationLayoutBinding
    private lateinit var viewModel: AuthorizationViewModel

    private val statusAuthorizationObserver = Observer<Task<AuthResult>> { authResult ->
        if (authResult.isSuccessful) {
            val intent = Intent(requireContext(), MainPageAccount::class.java)
            startActivity(intent)
        } else {
            when (authResult.exception) {
                is FirebaseAuthWeakPasswordException -> showMessage(
                    R.string.message_weak_password,
                    requireContext()
                )
                is FirebaseAuthInvalidCredentialsException -> showMessage(
                    R.string.message_invalid_auth_data,
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
        binding = AuthorizationLayoutBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(AuthorizationViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.title = "Авторизация"
        initListeners()
        initObservers()
    }

    private fun initObservers() = with(viewModel) {
        statusAuthorization.observe(viewLifecycleOwner, statusAuthorizationObserver)
    }

    private fun initListeners() = with(binding) {
        registerLink.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .setCustomAnimations(
                    R.anim.to_left_in,
                    R.anim.to_left_out,
                    R.anim.to_right_in,
                    R.anim.to_right_out
                )
                .replace(R.id.navHostMainActivity, RegistrationFragment())
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit()
        }
        loginButton.setOnClickListener {
            getInputData()
        }
    }

    private fun getInputData() = with(binding) {
        val email = email.text.toString()
        val password = password.text.toString()
        validateLoginForm(email, password)
    }

    private fun validateLoginForm(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty())
            showMessage(R.string.message_input_empty_fields, requireContext())
        else if (!isCorrectEmail(email))
            showMessage(R.string.message_email_incorrect, requireContext())
        else {
            viewModel.loginUser(email, password)
        }
    }
}
