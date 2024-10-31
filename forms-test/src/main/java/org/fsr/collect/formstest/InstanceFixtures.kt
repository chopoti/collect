package org.fsr.collect.formstest

import org.fsr.collect.forms.Form
import org.fsr.collect.forms.instances.Instance
import org.fsr.collect.shared.TempFiles

object InstanceFixtures {

    fun instance(
        status: String = Instance.STATUS_INCOMPLETE,
        lastStatusChangeDate: Long = 0,
        displayName: String? = null,
        dbId: Long? = null,
        form: Form? = null,
        deletedDate: Long? = null
    ): Instance {
        val instancesDir = TempFiles.createTempDir()
        return InstanceUtils.buildInstance("formId", "version", instancesDir.absolutePath)
            .status(status)
            .lastStatusChangeDate(lastStatusChangeDate)
            .displayName(displayName)
            .dbId(dbId).also {
                if (form != null) {
                    it.formId(form.formId)
                    it.formVersion(form.version)
                }
            }
            .deletedDate(deletedDate)
            .canDeleteBeforeSend(true)
            .build()
    }
}
