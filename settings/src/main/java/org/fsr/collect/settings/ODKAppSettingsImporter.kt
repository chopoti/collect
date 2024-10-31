package org.fsr.collect.settings

import org.json.JSONObject
import org.fsr.collect.projects.Project
import org.fsr.collect.projects.ProjectsRepository
import org.fsr.collect.settings.importing.ProjectDetailsCreatorImpl
import org.fsr.collect.settings.importing.SettingsChangeHandler
import org.fsr.collect.settings.importing.SettingsImporter
import org.fsr.collect.settings.importing.SettingsImportingResult
import org.fsr.collect.settings.validation.JsonSchemaSettingsValidator

class ODKAppSettingsImporter(
    projectsRepository: ProjectsRepository,
    settingsProvider: SettingsProvider,
    generalDefaults: Map<String, Any>,
    adminDefaults: Map<String, Any>,
    projectColors: List<String>,
    settingsChangedHandler: SettingsChangeHandler,
    private val deviceUnsupportedSettings: JSONObject
) {

    private val settingsImporter = SettingsImporter(
        settingsProvider,
        ODKAppSettingsMigrator(settingsProvider.getMetaSettings()),
        JsonSchemaSettingsValidator { javaClass.getResourceAsStream("/client-settings.schema.json")!! },
        generalDefaults,
        adminDefaults,
        settingsChangedHandler,
        projectsRepository,
        ProjectDetailsCreatorImpl(projectColors, generalDefaults)
    )

    fun fromJSON(json: String, project: Project.Saved): SettingsImportingResult {
        return try {
            settingsImporter.fromJSON(json, project, deviceUnsupportedSettings)
        } catch (e: Throwable) {
            SettingsImportingResult.INVALID_SETTINGS
        }
    }
}
