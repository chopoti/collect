package org.fsr.collect.android.support.pages;

public class ExperimentalPage extends Page<ExperimentalPage> {

    @Override
    public ExperimentalPage assertOnPage() {
        assertToolbarTitle(getTranslatedString(org.fsr.collect.strings.R.string.experimental));
        return this;
    }
}
