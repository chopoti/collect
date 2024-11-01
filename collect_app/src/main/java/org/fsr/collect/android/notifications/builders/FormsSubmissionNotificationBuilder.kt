package org.fsr.collect.android.notifications.builders

import android.app.Application
import android.app.Notification
import androidx.core.app.NotificationCompat
import org.fsr.collect.android.R
import org.fsr.collect.android.notifications.NotificationManagerNotifier
import org.fsr.collect.android.notifications.NotificationUtils
import org.fsr.collect.android.upload.FormUploadException
import org.fsr.collect.android.utilities.FormsUploadResultInterpreter
import org.fsr.collect.forms.instances.Instance
import org.fsr.collect.strings.localization.getLocalizedString

object FormsSubmissionNotificationBuilder {

    fun build(
        application: Application,
        result: Map<Instance, FormUploadException?>,
        projectName: String,
        notificationId: Int
    ): Notification {
        val allFormsUploadedSuccessfully = FormsUploadResultInterpreter.allFormsUploadedSuccessfully(result)

        return NotificationCompat.Builder(
            application,
            NotificationManagerNotifier.COLLECT_NOTIFICATION_CHANNEL
        ).apply {
            setContentIntent(
                NotificationUtils.createOpenAppContentIntent(
                    application,
                    notificationId
                )
            )
            setContentTitle(getTitle(application, allFormsUploadedSuccessfully))
            setContentText(getMessage(application, allFormsUploadedSuccessfully, result))
            setSubText(projectName)
            setSmallIcon(org.fsr.collect.icons.R.drawable.ic_notification_small)
            setAutoCancel(true)

            if (!allFormsUploadedSuccessfully) {
                val errorItems = FormsUploadResultInterpreter.getFailures(result, application)

                addAction(
                    R.drawable.ic_outline_info_small,
                    application.getLocalizedString(org.fsr.collect.strings.R.string.show_details),
                    NotificationUtils.createOpenErrorsActionIntent(application, errorItems, notificationId)
                )
            }
        }.build()
    }

    private fun getTitle(application: Application, allFormsUploadedSuccessfully: Boolean): String {
        return if (allFormsUploadedSuccessfully) {
            application.getLocalizedString(org.fsr.collect.strings.R.string.forms_upload_succeeded)
        } else {
            application.getLocalizedString(org.fsr.collect.strings.R.string.forms_upload_failed)
        }
    }

    private fun getMessage(application: Application, allFormsUploadedSuccessfully: Boolean, result: Map<Instance, FormUploadException?>): String {
        return if (allFormsUploadedSuccessfully) {
            application.getLocalizedString(org.fsr.collect.strings.R.string.all_uploads_succeeded)
        } else {
            application.getLocalizedString(
                org.fsr.collect.strings.R.string.some_uploads_failed,
                FormsUploadResultInterpreter.getNumberOfFailures(result),
                result.size
            )
        }
    }
}
