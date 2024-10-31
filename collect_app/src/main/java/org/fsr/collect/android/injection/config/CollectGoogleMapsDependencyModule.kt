package org.fsr.collect.android.injection.config

import org.fsr.collect.googlemaps.GoogleMapsDependencyModule
import org.fsr.collect.location.LocationClient
import org.fsr.collect.maps.layers.ReferenceLayerRepository
import org.fsr.collect.settings.SettingsProvider

class CollectGoogleMapsDependencyModule(
    private val appDependencyComponent: AppDependencyComponent
) : GoogleMapsDependencyModule() {
    override fun providesReferenceLayerRepository(): ReferenceLayerRepository {
        return appDependencyComponent.referenceLayerRepository()
    }

    override fun providesLocationClient(): LocationClient {
        return appDependencyComponent.locationClient()
    }

    override fun providesSettingsProvider(): SettingsProvider {
        return appDependencyComponent.settingsProvider()
    }
}
