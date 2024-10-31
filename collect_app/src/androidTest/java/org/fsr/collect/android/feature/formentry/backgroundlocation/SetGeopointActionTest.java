package org.fsr.collect.android.feature.formentry.backgroundlocation;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.RuleChain;
import org.fsr.collect.android.formentry.FormEntryMenuProvider;
import org.fsr.collect.android.support.rules.BlankFormTestRule;
import org.fsr.collect.android.support.rules.TestRuleChain;

public class SetGeopointActionTest {
    private static final String SETGEOPOINT_ACTION_FORM = "setgeopoint-action.xml";

    public BlankFormTestRule rule = new BlankFormTestRule(SETGEOPOINT_ACTION_FORM, "setgeopoint-action-instance-load");

    @Rule
    public RuleChain copyFormChain = TestRuleChain.chain()
            .around(rule);

    @Test
    public void locationCollectionSnackbar_ShouldBeDisplayedAtFormLaunch() {
        rule.startInFormEntry()
                .checkIsSnackbarWithMessageDisplayed(org.fsr.collect.strings.R.string.background_location_enabled, "⋮");
    }

    /**
     * Could be replaced in test for {@link FormEntryMenuProvider}
     */
    @Test
    public void locationCollectionToggle_ShouldBeAvailable() {
        rule.startInFormEntry()
                .clickOptionsIcon()
                .assertText(org.fsr.collect.strings.R.string.track_location_on);
    }
}
