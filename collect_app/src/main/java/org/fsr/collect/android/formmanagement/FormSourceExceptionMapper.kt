package org.fsr.collect.android.formmanagement

import android.content.Context
import org.fsr.collect.android.R
import org.fsr.collect.forms.FormSourceException
import org.fsr.collect.strings.localization.getLocalizedString

class FormSourceExceptionMapper(private val context: Context) {
    fun getMessage(exception: FormSourceException?): String {
        return when (exception) {
            is FormSourceException.Unreachable -> {
                context.getLocalizedString(
                    org.fsr.collect.strings.R.string.unreachable_error,
                    exception.serverUrl
                ) + " " + context.getLocalizedString(
                    org.fsr.collect.strings.R.string.report_to_project_lead
                )
            }
            is FormSourceException.SecurityError -> {
                context.getLocalizedString(
                    org.fsr.collect.strings.R.string.security_error,
                    exception.serverUrl
                ) + " " + context.getLocalizedString(
                    org.fsr.collect.strings.R.string.report_to_project_lead
                )
            }
            is FormSourceException.ServerError -> {
                context.getLocalizedString(
                    org.fsr.collect.strings.R.string.server_error,
                    exception.serverUrl,
                    exception.statusCode
                ) + " " + context.getLocalizedString(
                    org.fsr.collect.strings.R.string.report_to_project_lead
                )
            }
            is FormSourceException.ParseError -> {
                context.getLocalizedString(
                    org.fsr.collect.strings.R.string.invalid_response,
                    exception.serverUrl
                ) + " " + context.getLocalizedString(
                    org.fsr.collect.strings.R.string.report_to_project_lead
                )
            }
            is FormSourceException.ServerNotOpenRosaError -> {
                "This server does not correctly implement the OpenRosa formList API." + " " + context.getLocalizedString(
                    org.fsr.collect.strings.R.string.report_to_project_lead
                )
            }
            else -> {
                context.getLocalizedString(org.fsr.collect.strings.R.string.report_to_project_lead)
            }
        }
    }
}
