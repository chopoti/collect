package org.fsr.collect.android.support.pages

import org.fsr.collect.strings.R

class ErrorDialog : OkDialog() {
    fun assertOnPage(isFatal: Boolean): ErrorDialog {
        assertOnPage()
        if (isFatal) {
            assertText(R.string.form_cannot_be_used)
        } else {
            assertText(R.string.error_occured)
        }
        return this
    }
}
