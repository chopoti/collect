package org.fsr.collect.android.formlists.blankformlist

import android.net.Uri
import org.fsr.collect.android.external.FormsContract
import org.fsr.collect.forms.Form
import org.fsr.collect.forms.instances.InstancesRepository

data class BlankFormListItem(
    val databaseId: Long,
    val formId: String,
    val formName: String,
    val formVersion: String,
    val geometryPath: String,
    val dateOfCreation: Long,
    val dateOfLastUsage: Long,
    val dateOfLastDetectedAttachmentsUpdate: Long?,
    val contentUri: Uri
)

fun Form.toBlankFormListItem(projectId: String, instancesRepository: InstancesRepository) = BlankFormListItem(
    databaseId = this.dbId,
    formId = this.formId,
    formName = this.displayName,
    formVersion = this.version ?: "",
    geometryPath = this.geometryXpath ?: "",
    dateOfCreation = this.date,
    dateOfLastUsage = instancesRepository
        .getAllByFormId(this.formId)
        .filter { it.formVersion == this.version }
        .maxByOrNull { it.lastStatusChangeDate }?.lastStatusChangeDate ?: 0L,
    dateOfLastDetectedAttachmentsUpdate = this.lastDetectedAttachmentsUpdateDate,
    contentUri = FormsContract.getUri(projectId, this.dbId)
)
