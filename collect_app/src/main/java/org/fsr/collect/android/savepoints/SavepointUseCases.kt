package org.fsr.collect.android.savepoints

import android.net.Uri
import org.fsr.collect.android.external.FormsContract
import org.fsr.collect.android.utilities.ContentUriHelper
import org.fsr.collect.forms.FormsRepository
import org.fsr.collect.forms.instances.InstancesRepository
import org.fsr.collect.forms.savepoints.Savepoint
import org.fsr.collect.forms.savepoints.SavepointsRepository
import java.io.File

object SavepointUseCases {
    fun getSavepoint(
        uri: Uri,
        uriMimeType: String,
        formsRepository: FormsRepository,
        instanceRepository: InstancesRepository,
        savepointsRepository: SavepointsRepository
    ): Savepoint? {
        return if (uriMimeType == FormsContract.CONTENT_ITEM_TYPE) {
            val selectedForm = formsRepository.get(ContentUriHelper.getIdFromUri(uri))!!

            formsRepository.getAllByFormId(selectedForm.formId)
                .filter { it.date <= selectedForm.date }
                .sortedByDescending { it.date }
                .forEach { form ->
                    val savepoint = savepointsRepository.get(form.dbId, null)
                    if (savepoint != null && File(savepoint.savepointFilePath).exists()) {
                        return savepoint
                    }
                }
            null
        } else {
            val instance = instanceRepository.get(ContentUriHelper.getIdFromUri(uri))!!
            val form = formsRepository.getLatestByFormIdAndVersion(instance.formId, instance.formVersion)!!

            val savepoint = savepointsRepository.get(form.dbId, instance.dbId)
            if (savepoint != null &&
                File(savepoint.savepointFilePath).exists() &&
                File(savepoint.savepointFilePath).lastModified() > instance.lastStatusChangeDate
            ) {
                savepoint
            } else {
                null
            }
        }
    }
}
