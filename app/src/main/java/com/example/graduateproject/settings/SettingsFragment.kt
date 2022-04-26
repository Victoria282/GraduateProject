package com.example.graduateproject.settings

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
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
import javax.inject.Inject

class SettingsFragment @Inject constructor(
    viewModelFactory: ViewModelFactory
) : Fragment(R.layout.settings_fragment_layout) {
    private lateinit var binding: SettingsFragmentLayoutBinding
    private val viewModel: SettingsViewModel by viewModels { viewModelFactory }

    private val statusDeleteAccountObserver = Observer<Task<Void>> { task ->
        task.addOnCompleteListener {
            hideProgressBar()
            if (it.isSuccessful) {
                val direction = SettingsFragmentDirections.toAuthenticationGraph()
                findNavController().navigate(direction)
            } else if (it.exception is FirebaseAuthRecentLoginRequiredException) {
                viewModel.reAuth()
                try {
                    viewModel.deleteAccount()
                    val direction = SettingsFragmentDirections.toAuthenticationGraph()
                    findNavController().navigate(direction)
                } catch (e: FirebaseAuthRecentLoginRequiredException) {
                    Utils.showMessage(R.string.message_something_went_wrong, requireContext())
                }
            }
        }
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
        switchExams.setOnCheckedChangeListener { _, isChecked ->
            SharedPreferences.saveStudyMode = isChecked
        }
        switchNotification.setOnCheckedChangeListener { _, isChecked ->
            SharedPreferences.savePermissionNotification = isChecked
        }
    }

    private fun initObservers() {
        viewModel.statusDeleteAccount.observe(viewLifecycleOwner, statusDeleteAccountObserver)
    }

    private fun loadSettings() {
        with(binding) {
            switchWeek.isChecked = SharedPreferences.saveSwitchWeek
            switchExams.isChecked = SharedPreferences.saveStudyMode
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