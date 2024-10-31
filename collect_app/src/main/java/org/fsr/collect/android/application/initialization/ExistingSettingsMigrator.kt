package org.fsr.collect.android.application.initialization

import org.fsr.collect.projects.ProjectsRepository
import org.fsr.collect.settings.ODKAppSettingsMigrator
import org.fsr.collect.settings.SettingsProvider
import org.fsr.collect.upgrade.Upgrade

class ExistingSettingsMigrator(
    private val projectsRepository: ProjectsRepository,
    private val settingsProvider: SettingsProvider,
    private val settingsMigrator: ODKAppSettingsMigrator
) : Upgrade {

    override fun key(): String? {
        return null
    }

    override fun run() {
        projectsRepository.getAll().forEach {
            settingsMigrator.migrate(
                settingsProvider.getUnprotectedSettings(it.uuid),
                settingsProvider.getProtectedSettings(it.uuid)
            )
        }
    }
}
