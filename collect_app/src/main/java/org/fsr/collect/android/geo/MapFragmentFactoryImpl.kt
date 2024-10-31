package org.fsr.collect.android.geo

import org.fsr.collect.android.application.MapboxClassInstanceCreator
import org.fsr.collect.googlemaps.GoogleMapFragment
import org.fsr.collect.maps.MapFragment
import org.fsr.collect.maps.MapFragmentFactory
import org.fsr.collect.osmdroid.OsmDroidMapFragment
import org.fsr.collect.settings.SettingsProvider
import org.fsr.collect.settings.keys.ProjectKeys.BASEMAP_SOURCE_CARTO
import org.fsr.collect.settings.keys.ProjectKeys.BASEMAP_SOURCE_MAPBOX
import org.fsr.collect.settings.keys.ProjectKeys.BASEMAP_SOURCE_OSM
import org.fsr.collect.settings.keys.ProjectKeys.BASEMAP_SOURCE_USGS
import org.fsr.collect.settings.keys.ProjectKeys.KEY_BASEMAP_SOURCE

class MapFragmentFactoryImpl(private val settingsProvider: SettingsProvider) : MapFragmentFactory {

    override fun createMapFragment(): MapFragment {
        val settings = settingsProvider.getUnprotectedSettings()

        return when {
            isBasemapOSM(settings.getString(KEY_BASEMAP_SOURCE)) -> OsmDroidMapFragment()
            settings.getString(KEY_BASEMAP_SOURCE) == BASEMAP_SOURCE_MAPBOX -> MapboxClassInstanceCreator.createMapboxMapFragment()
            else -> GoogleMapFragment()
        }
    }

    private fun isBasemapOSM(basemap: String?): Boolean {
        return basemap == BASEMAP_SOURCE_OSM ||
            basemap == BASEMAP_SOURCE_USGS ||
            basemap == BASEMAP_SOURCE_CARTO
    }
}
