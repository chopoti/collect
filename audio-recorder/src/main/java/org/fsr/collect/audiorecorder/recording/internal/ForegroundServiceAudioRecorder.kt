package org.fsr.collect.audiorecorder.recording.internal

import android.app.Application
import android.content.Intent
import androidx.lifecycle.LiveData
import org.fsr.collect.androidshared.data.Consumable
import org.fsr.collect.audiorecorder.recorder.Output
import org.fsr.collect.audiorecorder.recording.AudioRecorder
import org.fsr.collect.audiorecorder.recording.AudioRecorderService
import org.fsr.collect.audiorecorder.recording.AudioRecorderService.Companion.ACTION_CLEAN_UP
import org.fsr.collect.audiorecorder.recording.AudioRecorderService.Companion.ACTION_PAUSE
import org.fsr.collect.audiorecorder.recording.AudioRecorderService.Companion.ACTION_RESUME
import org.fsr.collect.audiorecorder.recording.AudioRecorderService.Companion.ACTION_START
import org.fsr.collect.audiorecorder.recording.AudioRecorderService.Companion.ACTION_STOP
import org.fsr.collect.audiorecorder.recording.AudioRecorderService.Companion.EXTRA_OUTPUT
import org.fsr.collect.audiorecorder.recording.AudioRecorderService.Companion.EXTRA_SESSION_ID
import org.fsr.collect.audiorecorder.recording.RecordingSession
import java.io.Serializable

internal class ForegroundServiceAudioRecorder internal constructor(private val application: Application, private val recordingRepository: RecordingRepository) : AudioRecorder() {

    override fun isRecording(): Boolean {
        val currentSession = recordingRepository.currentSession.value
        return currentSession != null && currentSession.file == null
    }

    override fun getCurrentSession(): LiveData<RecordingSession?> {
        return recordingRepository.currentSession
    }

    override fun failedToStart(): LiveData<Consumable<Exception?>> {
        return recordingRepository.failedToStart
    }

    override fun start(sessionId: Serializable, output: Output) {
        application.startService(
            Intent(application, AudioRecorderService::class.java).apply {
                action = ACTION_START
                putExtra(EXTRA_SESSION_ID, sessionId)
                putExtra(EXTRA_OUTPUT, output)
            }
        )
    }

    override fun pause() {
        application.startService(
            Intent(application, AudioRecorderService::class.java).apply { action = ACTION_PAUSE }
        )
    }

    override fun resume() {
        application.startService(
            Intent(application, AudioRecorderService::class.java).apply { action = ACTION_RESUME }
        )
    }

    override fun stop() {
        application.startService(
            Intent(application, AudioRecorderService::class.java).apply { action = ACTION_STOP }
        )
    }

    override fun cleanUp() {
        application.startService(
            Intent(application, AudioRecorderService::class.java).apply { action = ACTION_CLEAN_UP }
        )
    }
}
