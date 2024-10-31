package org.fsr.collect.android.injection.config

import org.fsr.collect.android.geo.MapConfiguratorProvider
import org.fsr.collect.location.LocationClient
import org.fsr.collect.maps.MapConfigurator
import org.fsr.collect.maps.layers.ReferenceLayerRepository
import org.fsr.collect.osmdroid.OsmDroidDependencyModule
import org.fsr.collect.settings.SettingsProvider
import org.fsr.collect.settings.keys.ProjectKeys

class CollectOsmDroidDependencyModule(
    private val appDependencyComponent: AppDependencyComponent
) : OsmDroidDependencyModule() {
    override fun providesReferenceLayerRepository(): ReferenceLayerRepository {
        return appDependencyComponent.referenceLayerRepository()
    }

    override fun providesLocationClient(): LocationClient {
        return appDependencyComponent.locationClient()
    }

    override fun providesMapConfigurator(): MapConfigurator {
        return MapConfiguratorProvider.getConfigurator(
            appDependencyComponent.settingsProvider().getUnprotectedSettings().getString(ProjectKeys.KEY_BASEMAP_SOURCE)
        )
    }

    override fun providesSettingsProvider(): SettingsProvider {
        return appDependencyComponent.settingsProvider()
    }
}
