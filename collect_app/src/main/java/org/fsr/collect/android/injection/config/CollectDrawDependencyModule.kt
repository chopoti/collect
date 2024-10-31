package org.fsr.collect.android.injection.config

import org.fsr.collect.async.Scheduler
import org.fsr.collect.draw.DrawDependencyModule
import org.fsr.collect.settings.SettingsProvider

class CollectDrawDependencyModule(
    private val applicationComponent: AppDependencyComponent
) : DrawDependencyModule() {
    override fun providesScheduler(): Scheduler {
        return applicationComponent.scheduler()
    }

    override fun providesSettingsProvider(): SettingsProvider {
        return applicationComponent.settingsProvider()
    }

    override fun providesImagePath(): String {
        return applicationComponent.storagePathProvider().getTmpImageFilePath()
    }
}
