package org.fsr.collect.android.widgets.utilities

import android.app.Activity
import android.content.Intent
import android.widget.Toast
import org.javarosa.form.api.FormEntryPrompt
import org.fsr.collect.android.R
import org.fsr.collect.android.utilities.ApplicationConstants
import org.fsr.collect.androidshared.system.IntentLauncher

class GetContentAudioFileRequester(
    private val activity: Activity,
    private val intentLauncher: IntentLauncher,
    private val waitingForDataRegistry: WaitingForDataRegistry
) : AudioFileRequester {

    override fun requestFile(prompt: FormEntryPrompt) {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "audio/*"
        waitingForDataRegistry.waitForData(prompt.index)
        intentLauncher.launchForResult(
            activity,
            intent,
            ApplicationConstants.RequestCodes.AUDIO_CHOOSER
        ) {
            Toast.makeText(
                activity,
                activity.getString(
                    org.fsr.collect.strings.R.string.activity_not_found,
                    activity.getString(org.fsr.collect.strings.R.string.choose_sound)
                ),
                Toast.LENGTH_SHORT
            ).show()
            waitingForDataRegistry.cancelWaitingForData()
        }
    }
}
