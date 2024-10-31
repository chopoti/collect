package org.fsr.collect.android.preferences.dialogs

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.widget.CompoundButton
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.fsr.collect.android.R
import org.fsr.collect.android.databinding.PasswordDialogLayoutBinding
import org.fsr.collect.android.injection.DaggerUtils
import org.fsr.collect.android.preferences.ProjectPreferencesViewModel
import org.fsr.collect.android.utilities.SoftKeyboardController
import org.fsr.collect.androidshared.ui.ToastUtils
import org.fsr.collect.settings.SettingsProvider
import org.fsr.collect.settings.keys.ProtectedProjectKeys
import javax.inject.Inject

class ChangeAdminPasswordDialog : DialogFragment() {
    @Inject
    lateinit var factory: ProjectPreferencesViewModel.Factory

    @Inject
    lateinit var settingsProvider: SettingsProvider

    @Inject
    lateinit var softKeyboardController: SoftKeyboardController

    lateinit var binding: PasswordDialogLayoutBinding

    lateinit var projectPreferencesViewModel: ProjectPreferencesViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        DaggerUtils.getComponent(context).inject(this)
        projectPreferencesViewModel = ViewModelProvider(requireActivity(), factory)[ProjectPreferencesViewModel::class.java]
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = PasswordDialogLayoutBinding.inflate(LayoutInflater.from(context))

        binding.pwdField.post {
            softKeyboardController.showSoftKeyboard(binding.pwdField)
        }
        binding.checkBox2.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            if (isChecked) {
                binding.pwdField.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
            } else {
                binding.pwdField.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
        }

        return MaterialAlertDialogBuilder(requireContext())
            .setView(binding.root)
            .setTitle(org.fsr.collect.strings.R.string.change_admin_password)
            .setView(binding.root)
            .setPositiveButton(getString(org.fsr.collect.strings.R.string.ok)) { _: DialogInterface?, _: Int ->
                val password = binding.pwdField.text.toString()

                settingsProvider.getProtectedSettings().save(ProtectedProjectKeys.KEY_ADMIN_PW, password)

                if (password.isEmpty()) {
                    projectPreferencesViewModel.setStateNotProtected()
                    ToastUtils.showShortToast(
                        requireContext(),
                        org.fsr.collect.strings.R.string.admin_password_disabled
                    )
                } else {
                    projectPreferencesViewModel.setStateUnlocked()
                    ToastUtils.showShortToast(
                        requireContext(),
                        org.fsr.collect.strings.R.string.admin_password_changed
                    )
                }
                dismiss()
            }
            .setNegativeButton(getString(org.fsr.collect.strings.R.string.cancel)) { _: DialogInterface?, _: Int -> dismiss() }
            .setCancelable(false)
            .create()
    }
}
