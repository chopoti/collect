package org.fsr.collect.android.feature.smoke;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.RuleChain;
import org.junit.runner.RunWith;
import org.fsr.collect.android.support.rules.CollectTestRule;
import org.fsr.collect.android.support.TestDependencies;
import org.fsr.collect.android.support.rules.TestRuleChain;
import org.fsr.collect.android.support.pages.MainMenuPage;
import org.fsr.collect.android.support.pages.SendFinalizedFormPage;

@RunWith(AndroidJUnit4.class)
public class GetAndSubmitFormTest {

    private final CollectTestRule rule = new CollectTestRule(false);
    private final TestDependencies testDependencies = new TestDependencies();

    @Rule
    public RuleChain chain = TestRuleChain.chain(testDependencies).around(rule);

    @Test
    public void canGetBlankForm_fillItIn_andSubmit() {
        testDependencies.server.addForm("One Question", "one-question", "1", "one-question.xml");

        rule.withProject(testDependencies.server.getURL())
                // Fetch form
                .clickGetBlankForm()
                .clickGetSelected()
                .assertMessage("All downloads succeeded!")
                .clickOKOnDialog(new MainMenuPage())

                // Fill out form
                .startBlankForm("One Question")
                .swipeToEndScreen()
                .clickFinalize()

                // Send form
                .clickSendFinalizedForm(1)
                .clickSelectAll()
                .clickSendSelected()
                .assertText("One Question - Success")
                .clickOK(new SendFinalizedFormPage())
                .assertTextDoesNotExist("One Question")

                // Back to the start
                .pressBack(new MainMenuPage())
                .assertNumberOfFinalizedForms(0);
    }
}