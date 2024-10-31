package org.fsr.collect.android.application.initialization

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.startup.AppInitializer
import net.danlew.android.joda.JodaTimeInitializer
import org.fsr.collect.analytics.Analytics
import org.fsr.collect.android.BuildConfig
import org.fsr.collect.android.application.Collect
import org.fsr.collect.android.application.initialization.upgrade.UpgradeInitializer
import org.fsr.collect.android.entities.EntitiesRepositoryProvider
import org.fsr.collect.android.projects.ProjectsDataService
import org.fsr.collect.metadata.PropertyManager
import org.fsr.collect.projects.ProjectsRepository
import org.fsr.collect.settings.SettingsProvider
import timber.log.Timber
import java.util.Locale

class ApplicationInitializer(
    private val context: Application,
    private val propertyManager: PropertyManager,
    private val analytics: Analytics,
    private val upgradeInitializer: UpgradeInitializer,
    private val analyticsInitializer: AnalyticsInitializer,
    private val mapsInitializer: MapsInitializer,
    private val projectsRepository: ProjectsRepository,
    private val settingsProvider: SettingsProvider,
    private val entitiesRepositoryProvider: EntitiesRepositoryProvider,
    private val projectsDataService: ProjectsDataService
) {
    fun initialize() {
        initializeLocale()
        runInitializers()
        initializeFrameworks()
    }

    private fun runInitializers() {
        upgradeInitializer.initialize()
        analyticsInitializer.initialize()
        UserPropertiesInitializer(
            analytics,
            projectsRepository,
            settingsProvider,
            context
        ).initialize()
        mapsInitializer.initialize()
        JavaRosaInitializer(propertyManager, projectsDataService, entitiesRepositoryProvider, settingsProvider).initialize()
        SystemThemeMismatchFixInitializer(context).initialize()
    }

    private fun initializeFrameworks() {
        initializeLogging()
        AppInitializer.getInstance(context).initializeComponent(JodaTimeInitializer::class.java)
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
    }

    private fun initializeLocale() {
        Collect.defaultSysLanguage = Locale.getDefault().language
    }

    private fun initializeLogging() {
        if (BuildConfig.BUILD_TYPE == "odkCollectRelease") {
            Timber.plant(CrashReportingTree(analytics))
        } else {
            Timber.plant(Timber.DebugTree())
        }
    }
}
