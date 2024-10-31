package org.fsr.collect.audiorecorder.recording

import android.app.Application
import org.fsr.collect.androidshared.data.getState
import org.fsr.collect.audiorecorder.recording.internal.ForegroundServiceAudioRecorder
import org.fsr.collect.audiorecorder.recording.internal.RecordingRepository

open class AudioRecorderFactory(private val application: Application) {

    open fun create(): AudioRecorder {
        return ForegroundServiceAudioRecorder(application, RecordingRepository(application.getState()))
    }
}
