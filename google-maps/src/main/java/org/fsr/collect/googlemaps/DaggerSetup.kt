package org.fsr.collect.googlemaps

import dagger.Component
import dagger.Module
import dagger.Provides
import org.fsr.collect.location.LocationClient
import org.fsr.collect.maps.layers.ReferenceLayerRepository
import org.fsr.collect.settings.SettingsProvider
import javax.inject.Singleton

interface GoogleMapsDependencyComponentProvider {
    val googleMapsDependencyComponent: GoogleMapsDependencyComponent
}

@Component(modules = [GoogleMapsDependencyModule::class])
@Singleton
interface GoogleMapsDependencyComponent {
    fun inject(osmDroidMapFragment: GoogleMapFragment)
}

@Module
open class GoogleMapsDependencyModule {

    @Provides
    open fun providesReferenceLayerRepository(): ReferenceLayerRepository {
        throw UnsupportedOperationException("This should be overridden by dependent application")
    }

    @Provides
    open fun providesLocationClient(): LocationClient {
        throw UnsupportedOperationException("This should be overridden by dependent application")
    }

    @Provides
    open fun providesSettingsProvider(): SettingsProvider {
        throw UnsupportedOperationException("This should be overridden by dependent application")
    }
}
