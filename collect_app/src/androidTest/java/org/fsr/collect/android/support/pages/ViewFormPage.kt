package org.fsr.collect.android.support.pages

import org.fsr.collect.android.R

class ViewFormPage(private val formName: String) : Page<ViewFormPage>() {

    override fun assertOnPage(): ViewFormPage {
        assertToolbarTitle(formName)
        assertText(org.fsr.collect.strings.R.string.exit)
        return this
    }
}
