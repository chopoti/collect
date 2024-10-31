package org.fsr.collect.android.preferences.screens

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.preference.Preference
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.fsr.collect.analytics.Analytics
import org.fsr.collect.android.R
import org.fsr.collect.android.activities.ActivityUtils
import org.fsr.collect.android.activities.FirstLaunchActivity
import org.fsr.collect.android.analytics.AnalyticsEvents
import org.fsr.collect.android.configure.qr.QRCodeTabsActivity
import org.fsr.collect.android.injection.DaggerUtils
import org.fsr.collect.android.mainmenu.MainMenuActivity
import org.fsr.collect.android.preferences.dialogs.ResetDialogPreference
import org.fsr.collect.android.preferences.dialogs.ResetDialogPreferenceFragmentCompat
import org.fsr.collect.android.projects.DeleteProjectResult
import org.fsr.collect.android.projects.ProjectDeleter
import org.fsr.collect.androidshared.ui.ToastUtils
import org.fsr.collect.androidshared.ui.multiclicksafe.MultiClickGuard
import javax.inject.Inject

class ProjectManagementPreferencesFragment :
    BaseAdminPreferencesFragment(),
    Preference.OnPreferenceClickListener {

    @Inject
    lateinit var projectDeleter: ProjectDeleter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        DaggerUtils.getComponent(context).inject(this)
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        super.onCreatePreferences(savedInstanceState, rootKey)
        setPreferencesFromResource(R.xml.project_management_preferences, rootKey)

        findPreference<Preference>(IMPORT_SETTINGS_KEY)!!.onPreferenceClickListener = this
        findPreference<Preference>(DELETE_PROJECT_KEY)!!.onPreferenceClickListener = this
    }

    override fun onDisplayPreferenceDialog(preference: Preference) {
        if (MultiClickGuard.allowClick(javaClass.name)) {
            var resetDialogPreference: ResetDialogPreference? = null
            if (preference is ResetDialogPreference) {
                resetDialogPreference = preference
            }
            if (resetDialogPreference != null) {
                val dialogFragment = ResetDialogPreferenceFragmentCompat.newInstance(preference.key)
                dialogFragment.setTargetFragment(this, 0)
                dialogFragment.show(parentFragmentManager, null)
            } else {
                super.onDisplayPreferenceDialog(preference)
            }
        }
    }

    override fun onPreferenceClick(preference: Preference): Boolean {
        if (MultiClickGuard.allowClick(javaClass.name)) {
            when (preference.key) {
                IMPORT_SETTINGS_KEY -> {
                    val pref = Intent(activity, QRCodeTabsActivity::class.java)
                    startActivity(pref)
                }
                DELETE_PROJECT_KEY -> {
                    MaterialAlertDialogBuilder(requireActivity())
                        .setTitle(org.fsr.collect.strings.R.string.delete_project)
                        .setMessage(org.fsr.collect.strings.R.string.delete_project_confirm_message)
                        .setNegativeButton(org.fsr.collect.strings.R.string.delete_project_no) { _: DialogInterface?, _: Int -> }
                        .setPositiveButton(org.fsr.collect.strings.R.string.delete_project_yes) { _: DialogInterface?, _: Int -> deleteProject() }
                        .show()
                }
            }
            return true
        }
        return false
    }

    private fun deleteProject() {
        Analytics.log(AnalyticsEvents.DELETE_PROJECT)

        when (val deleteProjectResult = projectDeleter.deleteProject()) {
            is DeleteProjectResult.UnsentInstances -> {
                MaterialAlertDialogBuilder(requireActivity())
                    .setTitle(org.fsr.collect.strings.R.string.cannot_delete_project_title)
                    .setMessage(org.fsr.collect.strings.R.string.cannot_delete_project_message_one)
                    .setPositiveButton(org.fsr.collect.strings.R.string.ok, null)
                    .show()
            }
            is DeleteProjectResult.RunningBackgroundJobs -> {
                MaterialAlertDialogBuilder(requireActivity())
                    .setTitle(org.fsr.collect.strings.R.string.cannot_delete_project_title)
                    .setMessage(org.fsr.collect.strings.R.string.cannot_delete_project_message_two)
                    .setPositiveButton(org.fsr.collect.strings.R.string.ok, null)
                    .show()
            }
            is DeleteProjectResult.DeletedSuccessfullyCurrentProject -> {
                val newCurrentProject = deleteProjectResult.newCurrentProject
                ActivityUtils.startActivityAndCloseAllOthers(
                    requireActivity(),
                    MainMenuActivity::class.java
                )
                ToastUtils.showLongToast(
                    requireContext(),
                    getString(
                        org.fsr.collect.strings.R.string.switched_project,
                        newCurrentProject.name
                    )
                )
            }
            is DeleteProjectResult.DeletedSuccessfullyLastProject -> {
                ActivityUtils.startActivityAndCloseAllOthers(
                    requireActivity(),
                    FirstLaunchActivity::class.java
                )
            }
            is DeleteProjectResult.DeletedSuccessfullyInactiveProject -> {
                // not possible here
            }
        }
    }

    companion object {
        const val IMPORT_SETTINGS_KEY = "import_settings"
        const val DELETE_PROJECT_KEY = "delete_project"
    }
}