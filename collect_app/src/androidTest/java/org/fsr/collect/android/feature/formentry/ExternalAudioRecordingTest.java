package org.fsr.collect.android.feature.formentry;

import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static org.fsr.collect.android.utilities.FileUtils.copyFileFromResources;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.RuleChain;
import org.junit.runner.RunWith;
import org.fsr.collect.android.support.pages.MainMenuPage;
import org.fsr.collect.android.support.rules.CollectTestRule;
import org.fsr.collect.android.support.rules.RunnableRule;
import org.fsr.collect.android.support.rules.TestRuleChain;
import org.fsr.collect.androidtest.RecordedIntentsRule;

import java.io.File;
import java.io.IOException;

@RunWith(AndroidJUnit4.class)
public class ExternalAudioRecordingTest {

    public final CollectTestRule rule = new CollectTestRule();

    @Rule
    public final RuleChain chain = TestRuleChain.chain()
            .around(new RecordedIntentsRule())
            .around(new RunnableRule(() -> {
                // Return audio file when RECORD_SOUND_ACTION intent is sent

                try {
                    File stubRecording = File.createTempFile("test", ".m4a");
                    stubRecording.deleteOnExit();
                    copyFileFromResources("media/test.m4a", stubRecording.getAbsolutePath());

                    Intent intent = new Intent();
                    intent.setData(Uri.fromFile(stubRecording));
                    Instrumentation.ActivityResult result = new Instrumentation.ActivityResult(Activity.RESULT_OK, intent);
                    intending(hasAction(MediaStore.Audio.Media.RECORD_SOUND_ACTION)).respondWith(result);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }))
            .around(rule);

    @Test
    public void onAudioQuestion_whenAudioQualityIsExternal_usesExternalRecorder() throws Exception {
        new MainMenuPage()
                .copyForm("external-audio-question.xml")
                .startBlankForm("External Audio Question")
                .clickOnString(org.fsr.collect.strings.R.string.capture_audio)
                .assertContentDescriptionNotDisplayed(org.fsr.collect.strings.R.string.stop_recording)
                .assertTextDoesNotExist(org.fsr.collect.strings.R.string.capture_audio)
                .assertContentDescriptionDisplayed(org.fsr.collect.strings.R.string.play_audio);
    }
}
