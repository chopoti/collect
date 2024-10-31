package org.fsr.collect.android.support.pages;

public class ServerAuthDialog extends Page<ServerAuthDialog> {

    @Override
    public ServerAuthDialog assertOnPage() {
        assertText(org.fsr.collect.strings.R.string.server_requires_auth);
        return this;
    }

    public ServerAuthDialog fillUsername(String username) {
        inputText(org.fsr.collect.strings.R.string.username, username);
        return this;
    }

    public ServerAuthDialog fillPassword(String password) {
        inputText(org.fsr.collect.strings.R.string.password, password);
        return this;
    }

    public <D extends Page<D>> D clickOK(D destination) {
        clickOnString(org.fsr.collect.strings.R.string.ok);
        return destination.assertOnPage();
    }
}
