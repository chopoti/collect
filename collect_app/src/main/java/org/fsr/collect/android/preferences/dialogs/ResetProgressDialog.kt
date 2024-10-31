package org.fsr.collect.android.preferences.dialogs

import android.content.Context
import org.fsr.collect.android.R
import org.fsr.collect.material.MaterialProgressDialogFragment
import org.fsr.collect.strings.localization.getLocalizedString

class ResetProgressDialog : MaterialProgressDialogFragment() {
    override fun onAttach(context: Context) {
        super.onAttach(context)

        setTitle(context.getLocalizedString(org.fsr.collect.strings.R.string.please_wait))
        setMessage(context.getLocalizedString(org.fsr.collect.strings.R.string.reset_in_progress))
        isCancelable = false
    }
}
