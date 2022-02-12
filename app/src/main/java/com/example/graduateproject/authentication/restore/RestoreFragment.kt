package com.example.graduateproject.authentication.restore

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.graduateproject.R
import com.example.graduateproject.authentication.MainActivity
import com.example.graduateproject.databinding.RestoreLayoutBinding
import com.example.graduateproject.utils.Utils
import com.example.graduateproject.utils.Utils.hideKeyboard
import com.example.graduateproject.utils.Utils.showMessage
import com.example.graduateproject.utils.Utils.showMessageWithPositiveButton
import com.google.android.gms.tasks.Task

class RestoreFragment : Fragment(R.layout.restore_layout) {

    private lateinit var binding: RestoreLayoutBinding
    private lateinit var viewModel: RestoreViewModel

    private val statusRestorePasswordObservers = Observer<Task<Void>> { task ->
        task.addOnCompleteListener {

            hideProgressBar()
            binding.email.setText("")

            if (task.isSuccessful) {
                showMessageWithPositiveButton(R.string.password_is_reset, requireContext(), R.string.message_back_to_main_page) { _, _ ->
                    Intent(requireContext(), MainActivity::class.java).also {
                        startActivity(it)
                    }
                }
            } else {
                showMessage(R.string.message_something_went_wrong, requireContext())
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = RestoreLayoutBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(RestoreViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTittle()
        initListeners()
        initObservers()
    }

    private fun setTittle() { activity?.title = "Восстановление пароля" }

    private fun initObservers() {
        viewModel.statusRestorePassword.observe(viewLifecycleOwner, statusRestorePasswordObservers)
    }

    private fun initListeners() = with(binding) {
        buttonRestorePassword.setOnClickListener {
            showProgressBar()

            val email = email.text.toString()

            if (email.trim().isEmpty() || !Utils.isCorrectEmail(email)) {
                showMessage(R.string.message_email_incorrect, requireContext())
                binding.progressBar.visibility = View.GONE
            }
            else {
                view?.let { activity?.hideKeyboard(it) }
                viewModel.restoreUserPassword(email)
            }
        }
    }
    private fun hideProgressBar() {
        binding.progressBar.visibility = View.GONE
    }

    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
    }
}