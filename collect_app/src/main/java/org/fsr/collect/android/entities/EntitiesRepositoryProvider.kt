package org.fsr.collect.android.entities

import android.content.Context
import org.fsr.collect.android.database.entities.DatabaseEntitiesRepository
import org.fsr.collect.android.storage.StoragePaths
import org.fsr.collect.entities.storage.EntitiesRepository
import org.fsr.collect.projects.ProjectDependencyFactory

class EntitiesRepositoryProvider(
    private val context: Context,
    private val storagePathFactory: ProjectDependencyFactory<StoragePaths>
) :
    ProjectDependencyFactory<EntitiesRepository> {

    override fun create(projectId: String): EntitiesRepository {
        return DatabaseEntitiesRepository(context, storagePathFactory.create(projectId).metaDir)
    }
}
