package org.fsr.collect.android.application.initialization.upgrade

import android.content.Context
import org.fsr.collect.android.BuildConfig
import org.fsr.collect.android.application.initialization.CachedFormsCleaner
import org.fsr.collect.android.application.initialization.ExistingProjectMigrator
import org.fsr.collect.android.application.initialization.ExistingSettingsMigrator
import org.fsr.collect.android.application.initialization.GoogleDriveProjectsDeleter
import org.fsr.collect.android.application.initialization.SavepointsImporter
import org.fsr.collect.android.application.initialization.ScheduledWorkUpgrade
import org.fsr.collect.settings.SettingsProvider
import org.fsr.collect.settings.keys.MetaKeys
import org.fsr.collect.upgrade.AppUpgrader

class UpgradeInitializer(
    private val context: Context,
    private val settingsProvider: SettingsProvider,
    private val existingProjectMigrator: ExistingProjectMigrator,
    private val existingSettingsMigrator: ExistingSettingsMigrator,
    private val scheduledWorkUpgrade: ScheduledWorkUpgrade,
    private val googleDriveProjectsDeleter: GoogleDriveProjectsDeleter,
    private val savepointsImporter: SavepointsImporter,
    private val cachedFormsCleaner: CachedFormsCleaner
) {

    fun initialize() {
        AppUpgrader(
            MetaKeys.LAST_LAUNCHED,
            settingsProvider.getMetaSettings(),
            BuildConfig.VERSION_CODE,
            BeforeProjectsInstallDetector(context),
            listOf(
                existingProjectMigrator,
                existingSettingsMigrator,
                scheduledWorkUpgrade,
                googleDriveProjectsDeleter,
                savepointsImporter,
                cachedFormsCleaner
            )
        ).upgradeIfNeeded()
    }
}