package org.fsr.collect.android.application.initialization

import org.fsr.collect.android.projects.DeleteProjectResult
import org.fsr.collect.android.projects.ProjectDeleter
import org.fsr.collect.projects.ProjectsRepository
import org.fsr.collect.settings.SettingsProvider
import org.fsr.collect.settings.keys.ProjectKeys
import org.fsr.collect.upgrade.Upgrade

class GoogleDriveProjectsDeleter(
    private val projectsRepository: ProjectsRepository,
    private val settingsProvider: SettingsProvider,
    private val projectDeleter: ProjectDeleter
) : Upgrade {
    override fun key() = null

    override fun run() {
        projectsRepository.getAll().forEach {
            val unprotectedSettings = settingsProvider.getUnprotectedSettings(it.uuid)
            val protocol = unprotectedSettings.getString(ProjectKeys.KEY_PROTOCOL)

            if (protocol == ProjectKeys.PROTOCOL_GOOGLE_SHEETS) {
                // try to delete
                val result = projectDeleter.deleteProject(it.uuid)

                // if project cannot be deleted then convert it to ODK protocol
                if (result == DeleteProjectResult.UnsentInstances || result == DeleteProjectResult.RunningBackgroundJobs) {
                    unprotectedSettings.save(ProjectKeys.KEY_PROTOCOL, ProjectKeys.PROTOCOL_SERVER)
                    unprotectedSettings.save(ProjectKeys.KEY_SERVER_URL, "https://example.com")
                    projectsRepository.save(it.copy(isOldGoogleDriveProject = true))
                }
            }
        }
    }
}
