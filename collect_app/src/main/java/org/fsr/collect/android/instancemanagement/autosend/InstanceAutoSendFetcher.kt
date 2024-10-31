package org.fsr.collect.android.instancemanagement.autosend

import org.fsr.collect.forms.Form
import org.fsr.collect.forms.FormsRepository
import org.fsr.collect.forms.instances.Instance
import org.fsr.collect.forms.instances.InstancesRepository

object InstanceAutoSendFetcher {

    fun getInstancesToAutoSend(
        instancesRepository: InstancesRepository,
        formsRepository: FormsRepository,
        forcedOnly: Boolean = false
    ): List<Instance> {
        val allFinalizedForms = instancesRepository.getAllByStatus(
            Instance.STATUS_COMPLETE,
            Instance.STATUS_SUBMISSION_FAILED
        )

        val filter: (Form) -> Boolean = if (forcedOnly) {
            { form -> form.getAutoSendMode() == FormAutoSendMode.FORCED }
        } else {
            { form -> form.getAutoSendMode() == FormAutoSendMode.NEUTRAL }
        }

        return allFinalizedForms.filter {
            formsRepository.getLatestByFormIdAndVersion(it.formId, it.formVersion)
                ?.let { form -> filter(form) } ?: false
        }
    }
}
