package org.fsr.collect.android.application.initialization

import org.fsr.collect.android.projects.ProjectDependencyModule
import org.fsr.collect.projects.ProjectDependencyFactory
import org.fsr.collect.projects.ProjectsRepository
import org.fsr.collect.upgrade.Upgrade
import java.io.File

class CachedFormsCleaner(
    private val projectsRepository: ProjectsRepository,
    private val projectDependencyModuleFactory: ProjectDependencyFactory<ProjectDependencyModule>
) : Upgrade {
    override fun key() = null

    override fun run() {
        projectsRepository.getAll().forEach { project ->
            val projectDependencyModule = projectDependencyModuleFactory.create(project.uuid)
            File(projectDependencyModule.cacheDir)
                .listFiles { file -> file.name.endsWith(".formdef") }
                ?.forEach { file -> file.delete() }
        }
    }
}
