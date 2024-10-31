package org.fsr.collect.android.utilities

import org.fsr.collect.forms.FormsRepository
import org.fsr.collect.forms.instances.Instance
import java.util.Locale

object InstanceAutoDeleteChecker {

    /**
     * Returns whether instances of the form specified should be auto-deleted after successful
     * update.
     *
     * If the form explicitly sets the auto-delete property, then it overrides the preference.
     */
    @JvmStatic
    fun shouldInstanceBeDeleted(
        formsRepository: FormsRepository,
        isAutoDeleteEnabledInProjectSettings: Boolean,
        instance: Instance
    ): Boolean {
        formsRepository.getLatestByFormIdAndVersion(instance.formId, instance.formVersion)?.let { form ->
            return if (isAutoDeleteEnabledInProjectSettings) {
                form.autoDelete == null || form.autoDelete.trim().lowercase(Locale.US) != "false"
            } else {
                form.autoDelete != null && form.autoDelete.trim().lowercase(Locale.US) == "true"
            }
        }

        return false
    }
}
