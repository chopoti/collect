package org.fsr.collect.android.projects

import org.fsr.collect.android.storage.StoragePaths
import org.fsr.collect.android.utilities.ChangeLocks
import org.fsr.collect.entities.storage.EntitiesRepository
import org.fsr.collect.forms.FormSource
import org.fsr.collect.forms.FormsRepository
import org.fsr.collect.forms.instances.InstancesRepository
import org.fsr.collect.forms.savepoints.SavepointsRepository
import org.fsr.collect.projects.ProjectDependencyFactory
import org.fsr.collect.shared.settings.Settings

/**
 * Provides all the basic/building block dependencies needed when performing logic inside a
 * project.
 */
data class ProjectDependencyModule(
    val projectId: String,
    private val settingsFactory: ProjectDependencyFactory<Settings>,
    private val formsRepositoryFactory: ProjectDependencyFactory<FormsRepository>,
    private val instancesRepositoryProvider: ProjectDependencyFactory<InstancesRepository>,
    private val storagePathsFactory: ProjectDependencyFactory<StoragePaths>,
    private val changeLockFactory: ProjectDependencyFactory<ChangeLocks>,
    private val formSourceFactory: ProjectDependencyFactory<FormSource>,
    private val savepointsRepositoryFactory: ProjectDependencyFactory<SavepointsRepository>,
    private val entitiesRepositoryFactory: ProjectDependencyFactory<EntitiesRepository>
) {
    val generalSettings by lazy { settingsFactory.create(projectId) }
    val formsRepository by lazy { formsRepositoryFactory.create(projectId) }
    val instancesRepository by lazy { instancesRepositoryProvider.create(projectId) }
    val formSource by lazy { formSourceFactory.create(projectId) }
    val formsLock by lazy { changeLockFactory.create(projectId).formsLock }
    val instancesLock by lazy { changeLockFactory.create(projectId).instancesLock }
    val formsDir by lazy { storagePathsFactory.create(projectId).formsDir }
    val cacheDir by lazy { storagePathsFactory.create(projectId).cacheDir }
    val entitiesRepository by lazy { entitiesRepositoryFactory.create(projectId) }
    val savepointsRepository by lazy { savepointsRepositoryFactory.create(projectId) }
    val rootDir by lazy { storagePathsFactory.create(projectId).rootDir }
    val instancesDir by lazy { storagePathsFactory.create(projectId).instancesDir }
}
