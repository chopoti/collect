package org.fsr.collect.android.utilities

import android.content.Context
import org.fsr.collect.android.R
import org.fsr.collect.android.upload.FormUploadException
import org.fsr.collect.errors.ErrorItem
import org.fsr.collect.forms.instances.Instance
import org.fsr.collect.strings.localization.getLocalizedString

object FormsUploadResultInterpreter {
    fun getFailures(result: Map<Instance, FormUploadException?>, context: Context) = result.filter {
        it.value != null
    }.map {
        ErrorItem(
            it.key.displayName,
            context.getLocalizedString(org.fsr.collect.strings.R.string.form_details, it.key.formId ?: "", it.key.formVersion ?: ""),
            it.value?.message ?: ""
        )
    }

    fun getNumberOfFailures(result: Map<Instance, FormUploadException?>) = result.count {
        it.value != null
    }

    fun allFormsUploadedSuccessfully(result: Map<Instance, FormUploadException?>) = result.values.all {
        it == null
    }
}
