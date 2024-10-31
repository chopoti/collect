package org.fsr.collect.android.utilities

import android.content.Context
import org.fsr.collect.android.formmanagement.ServerFormDetails
import org.fsr.collect.android.formmanagement.download.FormDownloadException
import org.fsr.collect.android.formmanagement.download.FormDownloadExceptionMapper
import org.fsr.collect.errors.ErrorItem
import org.fsr.collect.strings.localization.getLocalizedString

object FormsDownloadResultInterpreter {
    fun getFailures(result: Map<ServerFormDetails, FormDownloadException?>, context: Context) = result.filter {
        it.value != null
    }.map {
        ErrorItem(
            it.key.formName ?: "",
            context.getLocalizedString(org.fsr.collect.strings.R.string.form_details, it.key.formId ?: "", it.key.formVersion ?: ""),
            FormDownloadExceptionMapper(context).getMessage(it.value)
        )
    }

    fun getNumberOfFailures(result: Map<ServerFormDetails, FormDownloadException?>) = result.count {
        it.value != null
    }

    fun allFormsDownloadedSuccessfully(result: Map<ServerFormDetails, FormDownloadException?>) = result.values.all {
        it == null
    }
}
