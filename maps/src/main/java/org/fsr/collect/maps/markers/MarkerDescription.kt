package org.fsr.collect.maps.markers

import org.fsr.collect.maps.MapFragment
import org.fsr.collect.maps.MapPoint

data class MarkerDescription(
    val point: MapPoint,
    val isDraggable: Boolean,
    @get:MapFragment.IconAnchor @param:MapFragment.IconAnchor
    val iconAnchor: String,
    val iconDescription: MarkerIconDescription
)
