package org.fsr.collect.android.formmanagement

import org.fsr.collect.android.formmanagement.download.FormDownloadException
import org.fsr.collect.android.formmanagement.download.FormDownloader
import org.fsr.collect.android.utilities.FileUtils
import org.fsr.collect.async.OngoingWorkListener
import org.fsr.collect.entities.LocalEntityUseCases
import org.fsr.collect.entities.storage.EntitiesRepository
import org.fsr.collect.forms.Form
import org.fsr.collect.forms.FormSource
import org.fsr.collect.forms.FormSourceException
import org.fsr.collect.forms.FormsRepository
import org.fsr.collect.forms.MediaFile
import org.fsr.collect.shared.locks.ChangeLock
import org.fsr.collect.shared.strings.Md5.getMd5Hash
import java.io.File
import java.io.IOException

object ServerFormUseCases {

    fun downloadForms(
        forms: List<ServerFormDetails>,
        changeLock: ChangeLock,
        formDownloader: FormDownloader,
        progressReporter: ((Int, Int) -> Unit)? = null,
        isCancelled: (() -> Boolean)? = null,
    ): Map<ServerFormDetails, FormDownloadException?> {
        val results = mutableMapOf<ServerFormDetails, FormDownloadException?>()
        changeLock.withLock { acquiredLock: Boolean ->
            if (acquiredLock) {
                for (index in forms.indices) {
                    val form = forms[index]

                    try {
                        formDownloader.downloadForm(
                            form,
                            object : FormDownloader.ProgressReporter {
                                override fun onDownloadingMediaFile(count: Int) {
                                    progressReporter?.invoke(index, count)
                                }
                            },
                            { isCancelled?.invoke() ?: false }
                        )

                        results[form] = null
                    } catch (e: FormDownloadException.DownloadingInterrupted) {
                        break
                    } catch (e: FormDownloadException) {
                        results[form] = e
                    }
                }
            }
        }

        return results
    }

    @JvmStatic
    fun copySavedFileFromPreviousFormVersionIfExists(formsRepository: FormsRepository, formId: String, mediaDirPath: String) {
        val lastSavedFile: File? = formsRepository
            .getAllByFormId(formId)
            .maxByOrNull { form -> form.date }
            ?.let {
                File(it.formMediaPath, FileUtils.LAST_SAVED_FILENAME)
            }

        if (lastSavedFile != null && lastSavedFile.exists()) {
            File(mediaDirPath).mkdir()
            FileUtils.copyFile(lastSavedFile, File(mediaDirPath, FileUtils.LAST_SAVED_FILENAME))
        }
    }

    @JvmStatic
    @Throws(IOException::class, FormSourceException::class, InterruptedException::class)
    fun downloadMediaFiles(
        formToDownload: ServerFormDetails,
        formSource: FormSource,
        formsRepository: FormsRepository,
        tempMediaPath: String,
        tempDir: File,
        entitiesRepository: EntitiesRepository,
        stateListener: OngoingWorkListener
    ): Boolean {
        var atLeastOneNewMediaFileDetected = false
        val tempMediaDir = File(tempMediaPath).also { it.mkdir() }

        formToDownload.manifest!!.mediaFiles.forEachIndexed { i, mediaFile ->
            stateListener.progressUpdate(i + 1)

            val tempMediaFile = File(tempMediaDir, mediaFile.filename)

            val existingFile = searchForExistingMediaFile(formsRepository, formToDownload, mediaFile)
            existingFile.also {
                if (it != null) {
                    if (it.getMd5Hash().contentEquals(mediaFile.hash)) {
                        FileUtils.copyFile(it, tempMediaFile)
                    } else {
                        val existingFileHash = it.getMd5Hash()
                        val file = formSource.fetchMediaFile(mediaFile.downloadUrl)
                        FileUtils.interuptablyWriteFile(file, tempMediaFile, tempDir, stateListener)

                        if (!tempMediaFile.getMd5Hash().contentEquals(existingFileHash)) {
                            atLeastOneNewMediaFileDetected = true
                        }
                    }
                } else {
                    val file = formSource.fetchMediaFile(mediaFile.downloadUrl)
                    FileUtils.interuptablyWriteFile(file, tempMediaFile, tempDir, stateListener)
                    atLeastOneNewMediaFileDetected = true
                }
            }

            if (mediaFile.isEntityList) {
                val dataset = mediaFile.filename.substringBefore(".csv")
                LocalEntityUseCases.updateLocalEntitiesFromServer(dataset, tempMediaFile, entitiesRepository)
            }
        }

        return atLeastOneNewMediaFileDetected
    }

    private fun searchForExistingMediaFile(
        formsRepository: FormsRepository,
        formToDownload: ServerFormDetails,
        mediaFile: MediaFile
    ): File? {
        val allFormVersions = formsRepository.getAllByFormId(formToDownload.formId)
        return allFormVersions.sortedByDescending {
            it.date
        }.map { form: Form ->
            File(form.formMediaPath, mediaFile.filename)
        }.firstOrNull { file: File ->
            file.exists()
        }
    }
}
