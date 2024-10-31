package org.fsr.collect.android.support.pages

class SaveOrIgnoreDrawingDialog<D : Page<D>>(
    private val drawingName: String,
    private val destination: D
) : Page<SaveOrIgnoreDrawingDialog<D>>() {

    override fun assertOnPage(): SaveOrIgnoreDrawingDialog<D> {
        val title = getTranslatedString(org.fsr.collect.strings.R.string.exit) + " " + drawingName
        assertText(title)
        return this
    }

    fun clickSaveChanges(): D {
        return clickOnTextInDialog(org.fsr.collect.strings.R.string.keep_changes, destination)
    }

    fun clickDiscardChanges(): D {
        return clickOnTextInDialog(org.fsr.collect.strings.R.string.discard_changes, destination)
    }
}