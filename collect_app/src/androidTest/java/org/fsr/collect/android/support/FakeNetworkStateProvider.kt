package org.fsr.collect.android.support

import org.fsr.collect.async.Scheduler
import org.fsr.collect.async.network.NetworkStateProvider

class FakeNetworkStateProvider : NetworkStateProvider {

    private var type: Scheduler.NetworkType? = Scheduler.NetworkType.WIFI

    fun goOnline(networkType: Scheduler.NetworkType) {
        type = networkType
    }

    fun goOffline() {
        type = null
    }

    override val currentNetwork: Scheduler.NetworkType?
        get() = type
}
