package org.fsr.collect.android.audio;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import android.app.Application;
import android.content.DialogInterface;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.testing.FragmentScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.fsr.collect.android.injection.config.AppDependencyModule;
import org.fsr.collect.android.support.CollectHelpers;
import org.fsr.collect.audiorecorder.recording.AudioRecorder;
import org.fsr.collect.audiorecorder.testsupport.StubAudioRecorder;
import org.fsr.collect.fragmentstest.FragmentScenarioLauncherRule;
import org.fsr.collect.testshared.RobolectricHelpers;

import java.io.File;

@RunWith(AndroidJUnit4.class)
public class AudioRecordingFormErrorDialogFragmentTest {

    private StubAudioRecorder audioRecorder;

    @Rule
    public FragmentScenarioLauncherRule launcherRule = new FragmentScenarioLauncherRule();

    @Before
    public void setup() throws Exception {
        File stubRecording = File.createTempFile("test", ".m4a");
        stubRecording.deleteOnExit();
        audioRecorder = new StubAudioRecorder(stubRecording.getAbsolutePath());
        CollectHelpers.overrideAppDependencyModule(new AppDependencyModule() {
            @Override
            public AudioRecorder providesAudioRecorder(Application application) {
                return audioRecorder;
            }
        });
    }

    @Test
    public void clickingOK_dismissesDialog() {
        FragmentScenario<AudioRecordingErrorDialogFragment> scenario = launcherRule.launch(AudioRecordingErrorDialogFragment.class);
        scenario.onFragment(f -> {
            AlertDialog dialog = (AlertDialog) f.getDialog();

            Button button = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
            assertThat(button.getText(), is(f.getString(org.fsr.collect.strings.R.string.ok)));

            button.performClick();
            RobolectricHelpers.runLooper();
            assertThat(dialog.isShowing(), is(false));
        });
    }

    @Test
    public void onDismiss_consumesConsumable() {
        FragmentScenario<AudioRecordingErrorDialogFragment> scenario = launcherRule.launch(AudioRecordingErrorDialogFragment.class);
        scenario.onFragment(DialogFragment::dismiss);
        assertThat(audioRecorder.failedToStart().getValue().isConsumed(), is(true));
    }
}
