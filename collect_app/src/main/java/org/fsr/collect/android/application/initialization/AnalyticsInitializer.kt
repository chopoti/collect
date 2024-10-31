package org.fsr.collect.android.application.initialization

import org.fsr.collect.analytics.Analytics
import org.fsr.collect.android.version.VersionInformation
import org.fsr.collect.settings.SettingsProvider
import org.fsr.collect.settings.keys.ProjectKeys

class AnalyticsInitializer(
    private val analytics: Analytics,
    private val versionInformation: VersionInformation,
    private val settingsProvider: SettingsProvider
) {

    fun initialize() {
        if (versionInformation.isBeta) {
            analytics.setAnalyticsCollectionEnabled(true)
        } else {
            val analyticsEnabled = settingsProvider.getUnprotectedSettings().getBoolean(ProjectKeys.KEY_ANALYTICS)
            analytics.setAnalyticsCollectionEnabled(analyticsEnabled)
        }

        Analytics.setInstance(analytics)
    }
}
