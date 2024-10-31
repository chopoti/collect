package org.fsr.collect.android.injection.config

import org.fsr.collect.projects.ProjectsDependencyModule
import org.fsr.collect.projects.ProjectsRepository

class CollectProjectsDependencyModule(
    private val appDependencyComponent: AppDependencyComponent
) : ProjectsDependencyModule() {
    override fun providesProjectsRepository(): ProjectsRepository {
        return appDependencyComponent.projectsRepository()
    }
}
