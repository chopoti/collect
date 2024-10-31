package org.fsr.collect.android.projects

import org.fsr.collect.projects.Project
import org.fsr.collect.projects.ProjectsRepository
import org.fsr.collect.settings.ODKAppSettingsImporter
import org.fsr.collect.settings.SettingsProvider
import org.fsr.collect.settings.importing.SettingsImportingResult

class ProjectCreator(
    private val projectsRepository: ProjectsRepository,
    private val projectsDataService: ProjectsDataService,
    private val settingsImporter: ODKAppSettingsImporter,
    private val settingsProvider: SettingsProvider
) {

    fun createNewProject(settingsJson: String): SettingsImportingResult {
        val savedProject = projectsRepository.save(Project.New("", "", ""))
        val settingsImportingResult = settingsImporter.fromJSON(settingsJson, savedProject)

        return if (settingsImportingResult == SettingsImportingResult.SUCCESS) {
            projectsDataService.setCurrentProject(savedProject.uuid)
            settingsImportingResult
        } else {
            settingsProvider.getUnprotectedSettings(savedProject.uuid).clear()
            settingsProvider.getProtectedSettings(savedProject.uuid).clear()
            projectsRepository.delete(savedProject.uuid)
            settingsImportingResult
        }
    }
}
