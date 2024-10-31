package org.fsr.collect.android.formmanagement.download

import android.content.Context
import org.fsr.collect.android.formmanagement.FormSourceExceptionMapper
import org.fsr.collect.strings.localization.getLocalizedString

class FormDownloadExceptionMapper(private val context: Context) {
    fun getMessage(exception: FormDownloadException?): String {
        return when (exception) {
            is FormDownloadException.FormWithNoHash -> {
                context.getLocalizedString(
                    org.fsr.collect.strings.R.string.form_with_no_hash_error
                ) + " " + context.getLocalizedString(
                    org.fsr.collect.strings.R.string.report_to_project_lead
                )
            }
            is FormDownloadException.FormParsingError -> {
                context.getLocalizedString(
                    org.fsr.collect.strings.R.string.form_parsing_error
                ) + " " + context.getLocalizedString(
                    org.fsr.collect.strings.R.string.report_to_project_lead
                )
            }
            is FormDownloadException.DiskError -> {
                context.getLocalizedString(
                    org.fsr.collect.strings.R.string.form_save_disk_error
                ) + " " + context.getLocalizedString(
                    org.fsr.collect.strings.R.string.report_to_project_lead
                )
            }
            is FormDownloadException.InvalidSubmission -> {
                context.getLocalizedString(
                    org.fsr.collect.strings.R.string.form_with_invalid_submission_error
                ) + " " + context.getLocalizedString(
                    org.fsr.collect.strings.R.string.report_to_project_lead
                )
            }
            is FormDownloadException.FormSourceError -> {
                FormSourceExceptionMapper(context).getMessage(exception.exception)
            }
            else -> {
                context.getLocalizedString(org.fsr.collect.strings.R.string.report_to_project_lead)
            }
        }
    }
}
