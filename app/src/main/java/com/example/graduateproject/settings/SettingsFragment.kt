package com.example.graduateproject.settings

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.graduateproject.R
import com.example.graduateproject.databinding.SettingsFragmentLayoutBinding
import com.example.graduateproject.di.utils.ViewModelFactory
import com.example.graduateproject.shared_preferences.SharedPreferences
import com.example.graduateproject.utils.Utils
import com.google.android.gms.tasks.Task
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException
import kotlinx.android.synthetic.main.bottom_sheet_settings.*
import kotlinx.android.synthetic.main.dialog_change_password.view.*
import javax.inject.Inject

class SettingsFragment @Inject constructor(
    viewModelFactory: ViewModelFactory
) : Fragment(R.layout.settings_fragment_layout) {
    private lateinit var binding: SettingsFragmentLayoutBinding
    private val viewModel: SettingsViewModel by viewModels { viewModelFactory }

    private val changePasswordObserver = Observer<Task<Void>> { task ->
        task.addOnCompleteListener {
            hideProgressBar()
            if (it.isSuccessful) {
                Utils.showMessage(R.string.password_is_changed_success, requireContext())
            } else if (it.exception is FirebaseAuthRecentLoginRequiredException) {
                viewModel.reAuth()
                try {
                    viewModel.changePassword(SharedPreferences.savedPassword!!)
                } catch (e: FirebaseAuthRecentLoginRequiredException) {
                    Utils.showMessage(R.string.message_something_went_wrong, requireContext())
                }
            }
        }
    }

    private val statusDeleteAccountObserver = Observer<Task<Void>> { task ->
        task.addOnCompleteListener {
            hideProgressBar()
            if (it.isSuccessful) {
                toAuthenticationScreen()
            } else if (it.exception is FirebaseAuthRecentLoginRequiredException) {
                viewModel.reAuth()
                try {
                    viewModel.deleteAccount()
                    toAuthenticationScreen()
                } catch (e: FirebaseAuthRecentLoginRequiredException) {
                    Utils.showMessage(R.string.message_something_went_wrong, requireContext())
                }
            }
        }
    }

    private fun toAuthenticationScreen() {
        val direction = SettingsFragmentDirections.toAuthenticationGraph()
        findNavController().navigate(direction)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = SettingsFragmentLayoutBinding.inflate(inflater, container, false)
        loadSettings()
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        val item = menu.findItem(R.id.switchWeek)
        item.isVisible = false
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initSwitch()
        initObservers()
    }

    private fun initSwitch() = with(binding) {
        bottomSheetMenu.setOnClickListener {
            setDialog()
        }
        switchWeek.setOnCheckedChangeListener { _, isChecked ->
            SharedPreferences.saveSwitchWeek = isChecked
        }
        switchNotification.setOnCheckedChangeListener { _, isChecked ->
            SharedPreferences.savePermissionNotification = isChecked
        }
    }

    private fun initObservers() {
        viewModel.statusDeleteAccount.observe(viewLifecycleOwner, statusDeleteAccountObserver)
        viewModel.changePassword.observe(viewLifecycleOwner, changePasswordObserver)
    }

    private fun loadSettings() {
        with(binding) {
            switchWeek.isChecked = SharedPreferences.saveSwitchWeek
            switchNotification.isChecked = SharedPreferences.savePermissionNotification
        }
    }

    private fun setDialog() = with(binding) {
        val dialog = BottomSheetDialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.bottom_sheet_settings)

        val deleteAccountButton = dialog.delete_account
        val changePassword = dialog.change_password

        deleteAccountButton.setOnClickListener {
            dialog.dismiss()
            showProgressBar()
            viewModel.deleteAccount()
        }
        changePassword.setOnClickListener {
            val dialogView = LayoutInflater
                .from(requireContext())
                .inflate(R.layout.dialog_change_password, null)

            val mBuilder = AlertDialog.Builder(requireContext())
                .setView(dialogView)
                .setTitle(R.string.change_password_button)
                .show()

            val messageEmptyFields = getString(R.string.not_empty_fields)
            val messageDifferencePasswords = getString(R.string.message_password_is_difficult)

            dialogView.changeButton.setOnClickListener {
                val password = dialogView.password.text?.trim().toString()
                val confirmPassword = dialogView.confirmPassword.text?.trim().toString()

                when {
                    password.isEmpty() -> dialogView.textFieldPassword.helperText =
                        messageEmptyFields
                    confirmPassword.isEmpty() -> dialogView.textFieldPasswordConfirm.helperText =
                        messageEmptyFields
                    dialogView.password.text.toString() != dialogView.confirmPassword.text.toString() ->
                        dialogView.textFieldPasswordConfirm.helperText = messageDifferencePasswords

                    else -> {
                        dialogView.textFieldPassword.helperText = ""
                        dialogView.textFieldPasswordConfirm.helperText = ""

                        mBuilder.dismiss()
                        SharedPreferences.savedPassword = password
                        showProgressBar()
                        viewModel.changePassword(password)
                    }
                }
            }
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun hideProgressBar() {
        binding.progressBarLayout.progressBar.visibility = View.GONE
    }

    private fun showProgressBar() {
        binding.progressBarLayout.progressBar.visibility = View.VISIBLE
    }
}