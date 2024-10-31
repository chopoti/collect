package org.fsr.collect.android.notifications.builders

import android.app.Application
import android.app.Notification
import androidx.core.app.NotificationCompat
import org.fsr.collect.android.notifications.NotificationManagerNotifier
import org.fsr.collect.android.notifications.NotificationUtils
import org.fsr.collect.strings.localization.getLocalizedString

object FormUpdatesAvailableNotificationBuilder {

    @JvmStatic
    fun build(application: Application, projectName: String, notificationId: Int): Notification {
        val contentIntent = NotificationUtils.createOpenAppContentIntent(
            application,
            notificationId
        )

        return NotificationCompat.Builder(
            application,
            NotificationManagerNotifier.COLLECT_NOTIFICATION_CHANNEL
        ).apply {
            setContentIntent(contentIntent)
            setContentTitle(application.getLocalizedString(org.fsr.collect.strings.R.string.form_updates_available))
            setContentText(null)
            setSubText(projectName)
            setSmallIcon(org.fsr.collect.icons.R.drawable.ic_notification_small)
            setAutoCancel(true)
        }.build()
    }
}
