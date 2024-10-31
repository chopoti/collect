package org.fsr.collect.android.entities

import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.runner.RunWith
import org.fsr.collect.android.database.entities.DatabaseEntitiesRepository
import org.fsr.collect.entities.storage.EntitiesRepository
import org.fsr.collect.shared.TempFiles

@RunWith(AndroidJUnit4::class)
class DatabaseEntitiesRepositoryTest : EntitiesRepositoryTest() {
    override fun buildSubject(): EntitiesRepository {
        return DatabaseEntitiesRepository(
            ApplicationProvider.getApplicationContext(),
            TempFiles.createTempDir().absolutePath
        )
    }
}
