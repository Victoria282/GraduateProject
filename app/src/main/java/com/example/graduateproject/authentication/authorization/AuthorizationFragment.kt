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
import com.example.graduateproject.authentication.validation.Validation
import com.example.graduateproject.databinding.AuthorizationLayoutBinding
import com.example.graduateproject.main.MainPageAccount
import com.example.graduateproject.utils.Utils.showMessage
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException

/* TODO в будущем:
    1. Восстановить пароль от аккаунта -> письмо на указанную почту для восстановления +
           AlertDialog - забыли пароль -> с кнопкой перехода на фрагмент восстановления
    2. Поворот экрана, сохранение состояния
    3. Вынести в отдельный класс ответы Firebase (Передавать объект во фрагмент объект с овтетом)
    4. api quotes для SplashScreen
    5. Session Class
    6. Dagger2
*/

class AuthorizationFragment : Fragment(R.layout.authorization_layout) {

    private lateinit var binding: AuthorizationLayoutBinding
    private lateinit var viewModel: AuthorizationViewModel

    private val statusAuthorizationObserver = Observer<Task<AuthResult>> { authResult ->
        if (authResult.isSuccessful) {
            Intent(requireContext(), MainPageAccount::class.java).also {
                startActivity(it)
            }
        } else
            when (authResult.exception) {
                is FirebaseAuthInvalidCredentialsException -> showMessage(
                    R.string.message_invalid_auth_data,
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
        binding = AuthorizationLayoutBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(AuthorizationViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTittle()
        initListeners()
        initObservers()
    }

    private fun setTittle() {
        activity?.title = "Авторизация"
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
        val resultValidation: Int = Validation.validateInputText(email, password, null)

        if (resultValidation == 1)
            viewModel.loginUser(email, password)
        else
            showMessage(resultValidation, requireContext())
    }
}
