package org.fsr.collect.android.injection.config

import org.fsr.collect.android.entities.EntitiesRepositoryProvider
import org.fsr.collect.android.formmanagement.FormSourceProvider
import org.fsr.collect.android.projects.ProjectDependencyModule
import org.fsr.collect.android.storage.StoragePathProvider
import org.fsr.collect.android.utilities.ChangeLockProvider
import org.fsr.collect.android.utilities.FormsRepositoryProvider
import org.fsr.collect.android.utilities.InstancesRepositoryProvider
import org.fsr.collect.android.utilities.SavepointsRepositoryProvider
import org.fsr.collect.projects.ProjectDependencyFactory
import org.fsr.collect.settings.SettingsProvider
import javax.inject.Inject

class ProjectDependencyModuleFactory @Inject constructor(
    private val settingsProvider: SettingsProvider,
    private val formsRepositoryProvider: FormsRepositoryProvider,
    private val instancesRepositoryProvider: InstancesRepositoryProvider,
    private val storagePathProvider: StoragePathProvider,
    private val changeLockProvider: ChangeLockProvider,
    private val formSourceProvider: FormSourceProvider,
    private val savepointsRepositoryProvider: SavepointsRepositoryProvider,
    private val entitiesRepositoryProvider: EntitiesRepositoryProvider,
) : ProjectDependencyFactory<ProjectDependencyModule> {
    override fun create(projectId: String): ProjectDependencyModule {
        return ProjectDependencyModule(
            projectId,
            settingsProvider::getUnprotectedSettings,
            formsRepositoryProvider,
            instancesRepositoryProvider,
            storagePathProvider,
            changeLockProvider,
            formSourceProvider,
            savepointsRepositoryProvider,
            entitiesRepositoryProvider
        )
    }
}
