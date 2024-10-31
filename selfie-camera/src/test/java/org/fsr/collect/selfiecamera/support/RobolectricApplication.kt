package org.fsr.collect.selfiecamera.support

import android.app.Application
import org.fsr.collect.selfiecamera.SelfieCameraDependencyComponent
import org.fsr.collect.selfiecamera.SelfieCameraDependencyComponentProvider

class RobolectricApplication : Application(), SelfieCameraDependencyComponentProvider {

    override lateinit var selfieCameraDependencyComponent: SelfieCameraDependencyComponent
}
