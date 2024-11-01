package org.fsr.collect.android.feature.formmanagement;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.RuleChain;
import org.junit.runner.RunWith;
import org.fsr.collect.android.R;
import org.fsr.collect.android.support.rules.CollectTestRule;
import org.fsr.collect.android.support.rules.TestRuleChain;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class ManualUpdatesTest {

    public CollectTestRule rule = new CollectTestRule();

    @Rule
    public RuleChain copyFormChain = TestRuleChain.chain()
            .around(rule);

    @Test
    public void whenManualUpdatesEnabled_getBlankFormsIsAvailable() {
        rule.startAtMainMenu()
                .enableManualUpdates()
                .assertText(org.fsr.collect.strings.R.string.get_forms);
    }

    @Test
    public void whenManualUpdatesEnabled_fillBlankFormRefreshButtonIsGone() {
        rule.startAtMainMenu()
                .enableManualUpdates()
                .clickFillBlankForm();

        onView(withId(R.id.menu_refresh)).check(doesNotExist());
    }
}
