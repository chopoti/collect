package org.fsr.collect.android.feature.formentry

import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain
import org.fsr.collect.android.R
import org.fsr.collect.android.support.pages.MainMenuPage
import org.fsr.collect.android.support.rules.CollectTestRule
import org.fsr.collect.android.support.rules.TestRuleChain

class FormSavedSnackbarTest {
    private val rule = CollectTestRule()

    @get:Rule
    val copyFormChain: RuleChain = TestRuleChain.chain().around(rule)

    @Test
    fun whenBlankFormSavedAsDraft_displaySnackbarWithEditAction() {
        rule.startAtMainMenu()
            .copyForm("one-question.xml")
            .startBlankForm("One Question")
            .answerQuestion(0, "25")
            .swipeToEndScreen()
            .clickSaveAsDraft()
            .assertText(org.fsr.collect.strings.R.string.form_saved_as_draft)
            .clickOnString(org.fsr.collect.strings.R.string.edit_form)
            .assertText("25")
            .assertText(org.fsr.collect.strings.R.string.jump_to_beginning)
            .assertText(org.fsr.collect.strings.R.string.jump_to_end)
    }

    @Test
    fun whenDraftFinalized_displaySnackbarWithViewAction() {
        rule.startAtMainMenu()
            .copyForm("one-question.xml")
            .startBlankForm("One Question")
            .answerQuestion(0, "25")
            .swipeToEndScreen()
            .clickSaveAsDraft()
            .clickDrafts()
            .clickOnForm("One Question")
            .clickGoToEnd()
            .clickFinalize()
            .assertText(org.fsr.collect.strings.R.string.form_saved)
            .clickOnString(org.fsr.collect.strings.R.string.view_form)
            .assertText("25")
            .assertTextDoesNotExist(org.fsr.collect.strings.R.string.jump_to_beginning)
            .assertTextDoesNotExist(org.fsr.collect.strings.R.string.jump_to_end)
            .assertText(org.fsr.collect.strings.R.string.exit)
    }

    @Test
    fun snackbarCanBeDismissed_andWillNotBeDisplayedAgainAfterRecreatingTheActivity() {
        rule.startAtMainMenu()
            .copyForm("one-question.xml")
            .startBlankForm("One Question")
            .swipeToEndScreen()
            .clickSaveAsDraft()
            .assertText(org.fsr.collect.strings.R.string.form_saved_as_draft)
            .closeSnackbar()
            .assertTextDoesNotExist(org.fsr.collect.strings.R.string.form_saved_as_draft)
            .rotateToLandscape(MainMenuPage())
            .assertTextDoesNotExist(org.fsr.collect.strings.R.string.form_saved_as_draft)
    }
}