package org.fsr.collect.android.projects

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.core.widget.doOnTextChanged
import org.fsr.collect.analytics.Analytics
import org.fsr.collect.android.activities.ActivityUtils
import org.fsr.collect.android.analytics.AnalyticsEvents
import org.fsr.collect.android.configure.qr.AppConfigurationGenerator
import org.fsr.collect.android.databinding.ManualProjectCreatorDialogLayoutBinding
import org.fsr.collect.android.injection.DaggerUtils
import org.fsr.collect.android.mainmenu.MainMenuActivity
import org.fsr.collect.android.projects.DuplicateProjectConfirmationKeys.MATCHING_PROJECT
import org.fsr.collect.android.projects.DuplicateProjectConfirmationKeys.SETTINGS_JSON
import org.fsr.collect.android.utilities.SoftKeyboardController
import org.fsr.collect.androidshared.ui.DialogFragmentUtils
import org.fsr.collect.androidshared.ui.ToastUtils
import org.fsr.collect.androidshared.utils.Validator
import org.fsr.collect.material.MaterialFullScreenDialogFragment
import org.fsr.collect.projects.ProjectsRepository
import org.fsr.collect.settings.SettingsProvider
import javax.inject.Inject

class ManualProjectCreatorDialog :
    MaterialFullScreenDialogFragment(),
    DuplicateProjectConfirmationDialog.DuplicateProjectConfirmationListener {

    @Inject
    lateinit var projectCreator: ProjectCreator

    @Inject
    lateinit var appConfigurationGenerator: AppConfigurationGenerator

    @Inject
    lateinit var softKeyboardController: SoftKeyboardController

    @Inject
    lateinit var projectsDataService: ProjectsDataService

    @Inject
    lateinit var projectsRepository: ProjectsRepository

    @Inject
    lateinit var settingsProvider: SettingsProvider

    lateinit var settingsConnectionMatcher: SettingsConnectionMatcher

    private lateinit var binding: ManualProjectCreatorDialogLayoutBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        DaggerUtils.getComponent(context).inject(this)
        settingsConnectionMatcher = SettingsConnectionMatcher(projectsRepository, settingsProvider)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ManualProjectCreatorDialogLayoutBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpToolbar()

        binding.urlInputText.doOnTextChanged { text, _, _, _ ->
            binding.addButton.isEnabled = !text.isNullOrBlank()
        }

        binding.urlInputText.post {
            softKeyboardController.showSoftKeyboard(binding.urlInputText)
        }

        binding.cancelButton.setOnClickListener {
            dismiss()
        }

        binding.addButton.setOnClickListener {
            handleAddingNewProject()
        }
    }

    override fun onCloseClicked() {
    }

    override fun onBackPressed() {
        dismiss()
    }

    override fun getToolbar(): Toolbar {
        return binding.toolbarLayout.toolbar
    }

    private fun setUpToolbar() {
        toolbar.setTitle(org.fsr.collect.strings.R.string.add_project)
    }

    private fun handleAddingNewProject() {
        if (!Validator.isUrlValid(binding.urlInputText.text?.trim().toString())) {
            ToastUtils.showShortToast(requireContext(), org.fsr.collect.strings.R.string.url_error)
        } else {
            val settingsJson = appConfigurationGenerator.getAppConfigurationAsJsonWithServerDetails(
                binding.urlInputText.text?.trim().toString(),
                binding.usernameInputText.text?.trim().toString(),
                binding.passwordInputText.text?.trim().toString()
            )

            settingsConnectionMatcher.getProjectWithMatchingConnection(settingsJson)?.let { uuid ->
                val confirmationArgs = Bundle()
                confirmationArgs.putString(SETTINGS_JSON, settingsJson)
                confirmationArgs.putString(MATCHING_PROJECT, uuid)
                DialogFragmentUtils.showIfNotShowing(
                    DuplicateProjectConfirmationDialog::class.java,
                    confirmationArgs,
                    childFragmentManager
                )
            } ?: run {
                createProject(settingsJson)
                Analytics.log(AnalyticsEvents.MANUAL_CREATE_PROJECT)
            }
        }
    }

    override fun createProject(settingsJson: String) {
        projectCreator.createNewProject(settingsJson)
        ActivityUtils.startActivityAndCloseAllOthers(activity, MainMenuActivity::class.java)
        ToastUtils.showLongToast(
            requireContext(),
            getString(org.fsr.collect.strings.R.string.switched_project, projectsDataService.getCurrentProject().name)
        )
    }

    override fun switchToProject(uuid: String) {
        projectsDataService.setCurrentProject(uuid)
        ActivityUtils.startActivityAndCloseAllOthers(activity, MainMenuActivity::class.java)
        ToastUtils.showLongToast(
            requireContext(),
            getString(
                org.fsr.collect.strings.R.string.switched_project,
                projectsDataService.getCurrentProject().name
            )
        )
    }
}
