package org.fsr.collect.android.utilities

import android.app.Application
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hamcrest.Matchers.startsWith
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.mock
import org.fsr.collect.android.storage.StoragePaths
import org.fsr.collect.formstest.FormUtils.buildForm
import org.fsr.collect.shared.TempFiles.createTempDir

@RunWith(AndroidJUnit4::class)
class FormsRepositoryProviderTest {

    private val metaDir = createTempDir()
    private val formsDir = createTempDir()
    private val cacheDir = createTempDir()

    @Test
    fun `returned repository uses project directory when passed`() {
        val context = ApplicationProvider.getApplicationContext<Application>()

        val projectId = "projectId"

        val formsRepositoryProvider = FormsRepositoryProvider(
            context,
            {
                StoragePaths(
                    "",
                    formsDir.absolutePath,
                    "",
                    cacheDir.absolutePath,
                    metaDir.absolutePath,
                    "",
                    ""
                )
            },
            mock()
        )
        val repository = formsRepositoryProvider.create(projectId)

        val form = repository.save(buildForm("id", "version", formsDir.absolutePath).build())
        assertThat(form.formFilePath, startsWith(formsDir.absolutePath))
        assertThat(form.jrCacheFilePath, startsWith(cacheDir.absolutePath))
    }
}
