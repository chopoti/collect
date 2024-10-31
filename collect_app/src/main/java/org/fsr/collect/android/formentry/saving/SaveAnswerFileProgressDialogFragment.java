package org.fsr.collect.android.formentry.saving;

import android.content.Context;

import androidx.annotation.NonNull;

import org.fsr.collect.material.MaterialProgressDialogFragment;

public class SaveAnswerFileProgressDialogFragment extends MaterialProgressDialogFragment {

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        setMessage(getString(org.fsr.collect.strings.R.string.saving_file));
    }
}
