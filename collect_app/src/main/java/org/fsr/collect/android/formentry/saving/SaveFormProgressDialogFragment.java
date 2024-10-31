package org.fsr.collect.android.formentry.saving;

import static org.fsr.collect.android.formentry.saving.FormSaveViewModel.SaveResult.State.SAVING;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import org.fsr.collect.material.MaterialProgressDialogFragment;

public class SaveFormProgressDialogFragment extends MaterialProgressDialogFragment {

    private final ViewModelProvider.Factory viewModelFactory;
    private FormSaveViewModel viewModel;

    public SaveFormProgressDialogFragment(ViewModelProvider.Factory viewModelFactory) {
        this.viewModelFactory = viewModelFactory;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        viewModel = new ViewModelProvider(requireActivity(), viewModelFactory).get(FormSaveViewModel.class);

        setCancelable(false);
        setTitle(getString(org.fsr.collect.strings.R.string.saving_form));

        viewModel.getSaveResult().observe(this, result -> {
            if (result != null && result.getState() == SAVING && result.getMessage() != null) {
                setMessage(getString(org.fsr.collect.strings.R.string.please_wait) + "\n\n" + result.getMessage());
            } else {
                setMessage(getString(org.fsr.collect.strings.R.string.please_wait));
            }
        });
    }

    @Override
    protected OnCancelCallback getOnCancelCallback() {
        return viewModel;
    }
}
