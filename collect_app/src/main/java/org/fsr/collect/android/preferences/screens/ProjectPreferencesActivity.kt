/*
 * Copyright 2018 Nafundi
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.fsr.collect.android.preferences.screens

import android.os.Bundle
import org.fsr.collect.android.R
import org.fsr.collect.android.activities.ActivityUtils
import org.fsr.collect.android.fragments.dialogs.MovingBackwardsDialog.MovingBackwardsDialogListener
import org.fsr.collect.android.fragments.dialogs.ResetSettingsResultDialog.ResetSettingsResultDialogListener
import org.fsr.collect.android.injection.DaggerUtils
import org.fsr.collect.android.mainmenu.MainMenuActivity
import org.fsr.collect.metadata.PropertyManager
import org.fsr.collect.strings.localization.LocalizedActivity
import javax.inject.Inject

class ProjectPreferencesActivity :
    LocalizedActivity(),
    ResetSettingsResultDialogListener,
    MovingBackwardsDialogListener {

    private var isInstanceStateSaved = false

    @Inject
    lateinit var propertyManager: PropertyManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preferences_layout)
        DaggerUtils.getComponent(this).inject(this)
    }

    override fun onPause() {
        super.onPause()
        propertyManager.reload()
    }

    override fun onPostResume() {
        super.onPostResume()
        isInstanceStateSaved = false
    }

    override fun onSaveInstanceState(outState: Bundle) {
        isInstanceStateSaved = true
        super.onSaveInstanceState(outState)
    }

    override fun onDialogClosed() {
        ActivityUtils.startActivityAndCloseAllOthers(this, MainMenuActivity::class.java)
    }

    override fun preventOtherWaysOfEditingForm() {
        val fragment = supportFragmentManager.findFragmentById(R.id.preferences_fragment_container) as FormEntryAccessPreferencesFragment
        fragment.preventOtherWaysOfEditingForm()
    }

    fun isInstanceStateSaved() = isInstanceStateSaved
}
