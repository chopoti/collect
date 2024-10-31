package org.fsr.collect.android.feature.settings

import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain
import org.fsr.collect.android.support.StubOpenRosaServer.EntityListItem
import org.fsr.collect.android.support.TestDependencies
import org.fsr.collect.android.support.pages.AccessControlPage
import org.fsr.collect.android.support.pages.FormEntryPage
import org.fsr.collect.android.support.pages.MainMenuPage
import org.fsr.collect.android.support.pages.ProjectSettingsPage
import org.fsr.collect.android.support.pages.ResetApplicationDialog
import org.fsr.collect.android.support.rules.CollectTestRule
import org.fsr.collect.android.support.rules.TestRuleChain.chain
import org.fsr.collect.strings.R

class ResetProjectTest {

    private val rule = CollectTestRule(useDemoProject = false)
    private val testDependencies = TestDependencies()

    @get:Rule
    val ruleChain: RuleChain = chain(testDependencies)
        .around(rule)

    @Test
    fun canResetBlankForms() {
        rule.startAtFirstLaunch()
            .clickTryCollect()
            .copyForm("all-widgets.xml")
            .openProjectSettingsDialog()
            .clickSettings()
            .clickProjectManagement()
            .clickOnResetProject()
            .assertDisabled(R.string.reset_settings_button_reset)
            .clickOnString(R.string.reset_blank_forms)
            .clickOnString(R.string.reset_settings_button_reset)
            .clickOKOnDialog(MainMenuPage())
            .clickFillBlankForm()
            .assertTextDoesNotExist("All widgets")
    }

    @Test
    fun canResetSavedFormsAndEntities() {
        testDependencies.server.addForm("one-question-entity-registration.xml")
        testDependencies.server.addForm(
            "one-question-entity-update.xml",
            listOf(EntityListItem("people.csv"))
        )

        rule.withMatchExactlyProject(testDependencies.server.url)
            .startBlankForm("One Question Entity Registration")
            .fillOutAndFinalize(FormEntryPage.QuestionAndAnswer("Name", "Logan Roy"))

            .openProjectSettingsDialog()
            .clickSettings()
            .clickProjectManagement()
            .clickOnResetProject()
            .assertDisabled(R.string.reset_settings_button_reset)
            .clickOnString(R.string.reset_saved_forms)
            .clickOnString(R.string.reset_settings_button_reset)
            .clickOKOnDialog(MainMenuPage())

            .clickDrafts()
            .assertTextDoesNotExist("One Question Entity Registration")
            .pressBack(MainMenuPage())

            .startBlankForm("One Question Entity Update")
            .assertTextDoesNotExist("Logan Roy")
    }

    @Test
    fun canResetProtectedSettings() {
        rule.startAtFirstLaunch()
            .clickTryCollect()
            .openProjectSettingsDialog()
            .clickSettings()
            .clickAccessControl()
            .openUserSettings()
            .uncheckServerOption()
            .pressBack(AccessControlPage())
            .pressBack(ProjectSettingsPage())
            .pressBack(MainMenuPage())
            .openProjectSettingsDialog()
            .clickSettings()
            .checkIfServerOptionIsNotDisplayed()
            .pressBack(MainMenuPage())
            .openProjectSettingsDialog()
            .clickSettings()
            .clickProjectManagement()
            .clickOnResetProject()
            .clickOnString(R.string.reset_settings)
            .clickOnString(R.string.reset_settings_button_reset)
            .clickOKOnDialog(MainMenuPage())
            .openProjectSettingsDialog()
            .clickSettings()
            .checkIfServerOptionIsDisplayed()
    }

    @Test
    fun canResetUserInterfaceSettings() {
        rule.startAtFirstLaunch()
            .clickTryCollect()
            .openProjectSettingsDialog()
            .clickSettings()
            .clickOnUserInterface()
            .assertText(R.string.theme_system)
            .clickOnTheme()
            .clickOnString(R.string.theme_dark)

        MainMenuPage()
            .assertOnPage()
            .openProjectSettingsDialog()
            .clickSettings()
            .clickOnUserInterface()
            .assertText(R.string.theme_dark)
            .clickOnLanguage()
            .clickOnSelectedLanguage("español")
            .openProjectSettingsDialog()
            .clickSettings()
            .clickOnUserInterface()
            .assertText("español")
            .pressBack(ProjectSettingsPage())
            .pressBack(MainMenuPage())
            .openProjectSettingsDialog()
            .clickSettings()
            .clickProjectManagement()
            .clickOnResetProject()
            .clickOnString(R.string.reset_settings)
            .clickOnString(R.string.reset_settings_button_reset)
            .clickOKOnDialog(MainMenuPage())
            .openProjectSettingsDialog()
            .clickSettings()
            .clickOnUserInterface()
            .assertText(R.string.theme_system)
            .assertTextDoesNotExist(R.string.theme_dark)
            .assertText(R.string.use_device_language)
            .assertTextDoesNotExist("español")
    }

    @Test
    fun when_rotateScreen_should_resetDialogNotDisappear() {
        rule.startAtFirstLaunch()
            .clickTryCollect()
            .openProjectSettingsDialog()
            .clickSettings()
            .clickProjectManagement()
            .clickOnResetProject()
            .assertText(R.string.reset_settings_dialog_title)
            .rotateToLandscape(ResetApplicationDialog())
            .assertText(R.string.reset_settings_dialog_title)
    }
}
