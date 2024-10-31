package org.fsr.collect.location

import android.app.Application
import org.fsr.collect.androidshared.data.AppState
import org.fsr.collect.androidshared.data.StateStore

class RobolectricApplication : Application(), StateStore {

    private val appState = AppState()

    override fun getState(): AppState {
        return appState
    }
}
