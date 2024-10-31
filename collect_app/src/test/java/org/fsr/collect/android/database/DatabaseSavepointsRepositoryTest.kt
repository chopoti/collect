package org.fsr.collect.android.database

import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.runner.RunWith
import org.fsr.collect.android.database.savepoints.DatabaseSavepointsRepository
import org.fsr.collect.forms.savepoints.SavepointsRepository
import org.fsr.collect.formstest.SavepointsRepositoryTest
import org.fsr.collect.shared.TempFiles

@RunWith(AndroidJUnit4::class)
class DatabaseSavepointsRepositoryTest : SavepointsRepositoryTest() {
    override fun buildSubject(cacheDirPath: String, instancesDirPath: String): SavepointsRepository {
        return DatabaseSavepointsRepository(
            ApplicationProvider.getApplicationContext(),
            TempFiles.createTempDir().absolutePath,
            cacheDirPath,
            instancesDirPath
        )
    }
}
