package org.fsr.collect.settings

import org.fsr.collect.shared.settings.Settings

interface SettingsProvider {

    fun getMetaSettings(): Settings

    fun getUnprotectedSettings(projectId: String?): Settings

    fun getUnprotectedSettings(): Settings = getUnprotectedSettings(null)

    fun getProtectedSettings(projectId: String?): Settings

    fun getProtectedSettings(): Settings = getProtectedSettings(null)

    fun clearAll(projectIds: List<String>)
}
