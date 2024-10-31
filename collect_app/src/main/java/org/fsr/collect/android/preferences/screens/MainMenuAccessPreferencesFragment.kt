package org.fsr.collect.android.preferences.screens

import android.os.Bundle
import androidx.preference.Preference
import org.fsr.collect.android.R
import org.fsr.collect.android.preferences.utilities.PreferencesUtils
import org.fsr.collect.settings.enums.FormUpdateMode
import org.fsr.collect.settings.enums.StringIdEnumUtils.getFormUpdateMode
import org.fsr.collect.settings.keys.ProtectedProjectKeys

class MainMenuAccessPreferencesFragment : BaseAdminPreferencesFragment() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        super.onCreatePreferences(savedInstanceState, rootKey)
        setPreferencesFromResource(R.xml.main_menu_access_preferences, rootKey)

        findPreference<Preference>(ProtectedProjectKeys.KEY_EDIT_SAVED)!!.isEnabled =
            settingsProvider.getProtectedSettings().getBoolean(ProtectedProjectKeys.ALLOW_OTHER_WAYS_OF_EDITING_FORM)

        val formUpdateMode = settingsProvider.getUnprotectedSettings().getFormUpdateMode(requireContext())
        if (formUpdateMode == FormUpdateMode.MATCH_EXACTLY) {
            PreferencesUtils.displayDisabled(findPreference(ProtectedProjectKeys.KEY_GET_BLANK), false)
        }
    }
}