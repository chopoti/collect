package org.fsr.collect.android.support.pages;

import org.fsr.collect.android.support.FakeClickableMapFragment;

public class FormMapPage extends Page<FormMapPage> {

    private final String formName;

    public FormMapPage(String formName) {
        this.formName = formName;
    }

    @Override
    public FormMapPage assertOnPage() {
        assertText(formName);
        checkIsIdDisplayed(org.fsr.collect.geo.R.id.geometry_status);
        return this;
    }

    public FormEntryPage clickFillBlankFormButton(String formName) {
        clickOnId(org.fsr.collect.geo.R.id.new_item);
        return new FormEntryPage(formName).assertOnPage();
    }

    public FormMapPage selectForm(FakeClickableMapFragment mapFragment, int index) {
        mapFragment.clickOnFeature(index);
        return this;
    }

    public FormHierarchyPage clickEditSavedForm(String formName) {
        clickOnString(org.fsr.collect.strings.R.string.edit_data);
        return new FormHierarchyPage(formName).assertOnPage();
    }
}
