package org.fsr.collect.android.support.pages;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

public class ServerSettingsPage extends Page<ServerSettingsPage> {

    @Override
    public ServerSettingsPage assertOnPage() {
        assertText(org.fsr.collect.strings.R.string.server_preferences);
        return this;
    }

    public ServerSettingsPage clickServerUsername() {
        onView(withText(getTranslatedString(org.fsr.collect.strings.R.string.username))).perform(click());
        return this;
    }

    public ServerSettingsPage clickOnURL() {
        onView(withText(getTranslatedString(org.fsr.collect.strings.R.string.server_url))).perform(click());
        return this;
    }

    public ServerSettingsPage clickServerPassword() {
        onView(withText(getTranslatedString(org.fsr.collect.strings.R.string.password))).perform(click());
        return this;
    }
}
