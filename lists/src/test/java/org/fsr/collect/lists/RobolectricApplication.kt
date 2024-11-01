package org.fsr.collect.lists

import android.app.Application
import org.fsr.collect.androidshared.ui.multiclicksafe.MultiClickGuard

class RobolectricApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // We don't want any clicks to be blocked
        MultiClickGuard.test = true
    }
}
