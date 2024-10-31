package org.fsr.collect.android.utilities

import android.content.Context
import org.fsr.collect.android.application.Collect
import org.fsr.collect.android.database.instances.DatabaseInstancesRepository
import org.fsr.collect.android.injection.DaggerUtils
import org.fsr.collect.android.storage.StoragePathProvider
import org.fsr.collect.android.storage.StoragePaths
import org.fsr.collect.forms.instances.InstancesRepository
import org.fsr.collect.projects.ProjectDependencyFactory

class InstancesRepositoryProvider @JvmOverloads constructor(
    private val context: Context,
    private val storagePathFactory: ProjectDependencyFactory<StoragePaths> = StoragePathProvider()
) : ProjectDependencyFactory<InstancesRepository> {

    override fun create(projectId: String): InstancesRepository {
        val storagePaths = storagePathFactory.create(projectId)
        return DatabaseInstancesRepository(
            context,
            storagePaths.metaDir,
            storagePaths.instancesDir,
            System::currentTimeMillis
        )
    }

    @Deprecated("Creating dependency without specified project is dangerous")
    fun create(): InstancesRepository {
        val currentProject =
            DaggerUtils.getComponent(Collect.getInstance()).currentProjectProvider()
                .getCurrentProject()
        return create(currentProject.uuid)
    }
}
