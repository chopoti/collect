package org.fsr.collect.projects.support

import android.app.Application
import org.fsr.collect.projects.DaggerProjectsDependencyComponent
import org.fsr.collect.projects.ProjectsDependencyComponent
import org.fsr.collect.projects.ProjectsDependencyComponentProvider

class RobolectricApplication : Application(), ProjectsDependencyComponentProvider {

    override lateinit var projectsDependencyComponent: ProjectsDependencyComponent

    override fun onCreate() {
        super.onCreate()
        projectsDependencyComponent = DaggerProjectsDependencyComponent.builder().build()
    }
}
