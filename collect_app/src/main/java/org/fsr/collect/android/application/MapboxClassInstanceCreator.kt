package org.fsr.collect.android.application

import androidx.fragment.app.Fragment
import org.fsr.collect.maps.MapConfigurator
import org.fsr.collect.maps.MapFragment

object MapboxClassInstanceCreator {

    private const val MAP_FRAGMENT = "org.fsr.collect.mapbox.MapboxMapFragment"

    @JvmStatic
    fun isMapboxAvailable(): Boolean {
        return try {
            getClass(MAP_FRAGMENT)
            System.loadLibrary("mapbox-common")
            true
        } catch (e: Throwable) {
            false
        }
    }

    fun createMapboxMapFragment(): MapFragment {
        return createClassInstance(MAP_FRAGMENT)
    }

    @JvmStatic
    fun createMapBoxInitializationFragment(): Fragment {
        return createClassInstance("org.fsr.collect.mapbox.MapBoxInitializationFragment")
    }

    @JvmStatic
    fun createMapboxMapConfigurator(): MapConfigurator {
        return createClassInstance("org.fsr.collect.mapbox.MapboxMapConfigurator")
    }

    private fun <T> createClassInstance(className: String): T {
        return getClass(className).newInstance() as T
    }

    private fun getClass(className: String): Class<*> = Class.forName(className)
}
