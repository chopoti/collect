package org.fsr.collect.location.satellites

import org.fsr.collect.androidshared.livedata.NonNullLiveData

interface SatelliteInfoClient {

    val satellitesUsedInLastFix: NonNullLiveData<Int>
}
