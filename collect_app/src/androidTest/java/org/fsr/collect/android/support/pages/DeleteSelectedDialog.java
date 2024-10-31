package org.fsr.collect.android.support.pages;

public class DeleteSelectedDialog extends Page<DeleteSelectedDialog> {

    private final int numberSelected;
    private final DeleteSavedFormPage destination;

    public DeleteSelectedDialog(int numberSelected, DeleteSavedFormPage destination) {
        this.numberSelected = numberSelected;
        this.destination = destination;
    }

    @Override
    public DeleteSelectedDialog assertOnPage() {
        assertTextInDialog(getTranslatedString(org.fsr.collect.strings.R.string.delete_confirm, numberSelected));
        return this;
    }

    public DeleteSavedFormPage clickDeleteForms() {
        clickOnTextInDialog(org.fsr.collect.strings.R.string.delete_yes);
        return destination.assertOnPage();
    }
}
