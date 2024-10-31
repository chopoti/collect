package org.fsr.collect.geo

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides
import org.fsr.collect.async.Scheduler
import org.fsr.collect.geo.geopoint.GeoPointActivity
import org.fsr.collect.geo.geopoint.GeoPointDialogFragment
import org.fsr.collect.geo.geopoint.GeoPointMapActivity
import org.fsr.collect.geo.geopoint.GeoPointViewModelFactory
import org.fsr.collect.geo.geopoint.LocationTrackerGeoPointViewModel
import org.fsr.collect.geo.geopoly.GeoPolyActivity
import org.fsr.collect.geo.selection.SelectionMapFragment
import org.fsr.collect.location.LocationClient
import org.fsr.collect.location.satellites.SatelliteInfoClient
import org.fsr.collect.location.tracker.LocationTracker
import org.fsr.collect.maps.MapFragmentFactory
import org.fsr.collect.maps.layers.ReferenceLayerRepository
import org.fsr.collect.permissions.PermissionsChecker
import org.fsr.collect.settings.SettingsProvider
import org.fsr.collect.webpage.ExternalWebPageHelper
import javax.inject.Singleton

interface GeoDependencyComponentProvider {
    val geoDependencyComponent: GeoDependencyComponent
}

@Component(modules = [GeoDependencyModule::class])
@Singleton
interface GeoDependencyComponent {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        fun geoDependencyModule(geoDependencyModule: GeoDependencyModule): Builder

        fun build(): GeoDependencyComponent
    }

    fun inject(geoPointMapActivity: GeoPointMapActivity)
    fun inject(geoPolyActivity: GeoPolyActivity)
    fun inject(geoPointDialogFragment: GeoPointDialogFragment)
    fun inject(geoPointActivity: GeoPointActivity)
    fun inject(selectionMapFragment: SelectionMapFragment)

    val scheduler: Scheduler
    val locationTracker: LocationTracker
    val satelliteInfoClient: SatelliteInfoClient
}

@Module
open class GeoDependencyModule {

    @Provides
    open fun context(application: Application): Context {
        return application
    }

    @Provides
    open fun providesMapFragmentFactory(): MapFragmentFactory {
        throw UnsupportedOperationException("This should be overridden by dependent application")
    }

    @Provides
    open fun providesLocationTracker(application: Application): LocationTracker {
        throw UnsupportedOperationException("This should be overridden by dependent application")
    }

    @Provides
    open fun providesLocationClient(): LocationClient {
        throw UnsupportedOperationException("This should be overridden by dependent application")
    }

    @Provides
    open fun providesScheduler(): Scheduler {
        throw UnsupportedOperationException("This should be overridden by dependent application")
    }

    @Provides
    open fun providesSatelliteInfoClient(context: Context): SatelliteInfoClient {
        throw UnsupportedOperationException("This should be overridden by dependent application")
    }

    @Provides
    open fun providesPermissionChecker(context: Context): PermissionsChecker {
        throw UnsupportedOperationException("This should be overridden by dependent application")
    }

    @Provides
    internal open fun providesGeoPointViewModelFactory(application: Application): GeoPointViewModelFactory {
        return object : GeoPointViewModelFactory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val componentProvider = application as GeoDependencyComponentProvider
                val component = componentProvider.geoDependencyComponent
                return LocationTrackerGeoPointViewModel(
                    component.locationTracker,
                    component.satelliteInfoClient,
                    System::currentTimeMillis,
                    component.scheduler
                ) as T
            }
        }
    }

    @Provides
    open fun providesReferenceLayerRepository(): ReferenceLayerRepository {
        throw UnsupportedOperationException("This should be overridden by dependent application")
    }

    @Provides
    open fun providesSettingsProvider(): SettingsProvider {
        throw UnsupportedOperationException("This should be overridden by dependent application")
    }

    @Provides
    open fun providesExternalWebPageHelper(): ExternalWebPageHelper {
        throw UnsupportedOperationException("This should be overridden by dependent application")
    }
}
