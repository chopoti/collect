package org.fsr.collect.android.support.pages;

public class MapsSettingsPage extends Page<MapsSettingsPage> {

    @Override
    public MapsSettingsPage assertOnPage() {
        assertText(org.fsr.collect.strings.R.string.maps);
        return this;
    }
}
