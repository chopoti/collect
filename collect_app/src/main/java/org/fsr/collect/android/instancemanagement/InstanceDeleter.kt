package org.fsr.collect.android.instancemanagement

import org.fsr.collect.forms.FormsRepository
import org.fsr.collect.forms.instances.Instance
import org.fsr.collect.forms.instances.InstancesRepository

class InstanceDeleter(
    private val instancesRepository: InstancesRepository,
    private val formsRepository: FormsRepository
) {
    fun delete(ids: Array<Long>) {
        ids.forEach {
            delete(it)
        }
    }

    fun delete(id: Long?) {
        instancesRepository[id]?.let { instance ->
            if (instance.status == Instance.STATUS_SUBMITTED) {
                instancesRepository.deleteWithLogging(id)
            } else {
                instancesRepository.delete(id)
            }
            val form =
                formsRepository.getLatestByFormIdAndVersion(instance.formId, instance.formVersion)
            if (form != null && form.isDeleted) {
                val otherInstances = instancesRepository.getAllNotDeletedByFormIdAndVersion(
                    form.formId,
                    form.version
                )
                if (otherInstances.isEmpty()) {
                    formsRepository.delete(form.dbId)
                }
            }
        }
    }
}
