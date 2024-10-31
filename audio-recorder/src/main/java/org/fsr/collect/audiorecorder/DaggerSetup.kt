package org.fsr.collect.audiorecorder

import android.app.Application
import android.media.MediaRecorder
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.Dispatchers
import org.fsr.collect.async.CoroutineScheduler
import org.fsr.collect.async.Scheduler
import org.fsr.collect.audiorecorder.mediarecorder.AACRecordingResource
import org.fsr.collect.audiorecorder.mediarecorder.AMRRecordingResource
import org.fsr.collect.audiorecorder.recorder.Output
import org.fsr.collect.audiorecorder.recorder.Recorder
import org.fsr.collect.audiorecorder.recorder.RecordingResourceRecorder
import org.fsr.collect.audiorecorder.recording.AudioRecorderService
import java.io.File
import javax.inject.Singleton

/**
 * This module follows the Android docs's multi-module example for Dagger. Any application that
 * depends on this module should implement the provider interface and return a constructed
 * component. This gives the application the opportunity to override dependencies if it wants to.
 *
 * @see [Using Dagger in multi-module apps](https://developer.android.com/training/dependency-injection/dagger-multi-module)
 */

interface AudioRecorderDependencyComponentProvider {
    val audioRecorderDependencyComponent: AudioRecorderDependencyComponent
}

@Component(modules = [AudioRecorderDependencyModule::class])
@Singleton
interface AudioRecorderDependencyComponent {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        fun dependencyModule(audioRecorderDependencyModule: AudioRecorderDependencyModule): Builder

        fun build(): AudioRecorderDependencyComponent
    }

    fun inject(activity: AudioRecorderService)
}

@Module
open class AudioRecorderDependencyModule {

    @Provides
    open fun providesCacheDir(application: Application): File {
        val externalFilesDir = application.getExternalFilesDir(null)
        return File(externalFilesDir, "recordings").also { it.mkdirs() }
    }

    @Provides
    open fun providesRecorder(cacheDir: File): Recorder {
        return RecordingResourceRecorder(cacheDir) { output ->
            when (output) {
                Output.AMR -> {
                    AMRRecordingResource(MediaRecorder(), android.os.Build.VERSION.SDK_INT)
                }

                Output.AAC -> {
                    AACRecordingResource(MediaRecorder(), android.os.Build.VERSION.SDK_INT, 64)
                }

                Output.AAC_LOW -> {
                    AACRecordingResource(MediaRecorder(), android.os.Build.VERSION.SDK_INT, 24)
                }
            }
        }
    }

    @Provides
    open fun providesScheduler(application: Application): Scheduler {
        return CoroutineScheduler(Dispatchers.Main, Dispatchers.IO)
    }
}
