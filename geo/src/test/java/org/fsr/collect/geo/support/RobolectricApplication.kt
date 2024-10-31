package org.fsr.collect.geo.support

import android.app.Application
import org.fsr.collect.androidshared.ui.Animations
import org.fsr.collect.androidshared.ui.multiclicksafe.MultiClickGuard
import org.fsr.collect.geo.GeoDependencyComponent
import org.fsr.collect.geo.GeoDependencyComponentProvider

class RobolectricApplication : Application(), GeoDependencyComponentProvider {

    override lateinit var geoDependencyComponent: GeoDependencyComponent

    override fun onCreate() {
        super.onCreate()
        Animations.DISABLE_ANIMATIONS = true

        // We don't want any clicks to be blocked
        MultiClickGuard.test = true
    }
}
