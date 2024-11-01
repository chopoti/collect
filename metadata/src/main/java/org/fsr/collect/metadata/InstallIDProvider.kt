package org.fsr.collect.metadata

import org.fsr.collect.shared.settings.Settings
import org.fsr.collect.shared.strings.RandomString

interface InstallIDProvider {
    val installID: String
}

class SettingsInstallIDProvider(
    private val metaPreferences: Settings,
    private val preferencesKey: String
) : InstallIDProvider {

    override val installID: String
        get() {
            return if (metaPreferences.contains(preferencesKey)) {
                metaPreferences.getString(preferencesKey) ?: generateAndStoreInstallID()
            } else {
                generateAndStoreInstallID()
            }
        }

    private fun generateAndStoreInstallID(): String {
        val installID = "collect:" + RandomString.randomString(16)
        metaPreferences.save(preferencesKey, installID)
        return installID
    }
}
