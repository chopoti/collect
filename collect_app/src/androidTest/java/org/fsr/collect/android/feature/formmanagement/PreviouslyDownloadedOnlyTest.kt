package org.fsr.collect.android.feature.formmanagement

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.matcher.ViewMatchers.withId
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain
import org.fsr.collect.android.R
import org.fsr.collect.android.support.TestDependencies
import org.fsr.collect.android.support.pages.ErrorPage
import org.fsr.collect.android.support.pages.MainMenuPage
import org.fsr.collect.android.support.rules.CollectTestRule
import org.fsr.collect.android.support.rules.NotificationDrawerRule
import org.fsr.collect.android.support.rules.TestRuleChain

class PreviouslyDownloadedOnlyTest {

    private val testDependencies = TestDependencies()
    private val notificationDrawerRule = NotificationDrawerRule()
    private val rule = CollectTestRule(useDemoProject = false)

    @get:Rule
    var ruleChain: RuleChain = TestRuleChain.chain(testDependencies)
        .around(notificationDrawerRule)
        .around(rule)

    @Test
    fun whenPreviouslyDownloadedOnlyEnabled_notifiesOnFormUpdates_automaticallyAndRepeatedly() {
        rule.withProject(testDependencies.server, "one-question.xml", "two-question.xml")
            .setServer(testDependencies.server.url)
            .enablePreviouslyDownloadedOnlyUpdates()

        testDependencies.server.addForm(
            "One Question Updated",
            "one_question",
            "2",
            "one-question-updated.xml"
        )
        testDependencies.scheduler.runDeferredTasks()

        notificationDrawerRule.open()
            .assertNotification(
                "ODK Collect",
                "Form updates available",
                testDependencies.server.hostName
            )
            .clearAll()

        testDependencies.server.addForm(
            "Two Question Updated",
            "two_question",
            "1",
            "two-question-updated.xml"
        )

        testDependencies.scheduler.runDeferredTasks()

        notificationDrawerRule.open()
            .assertNotification(
                "ODK Collect",
                "Form updates available",
                testDependencies.server.hostName
            )
            .clickNotification(
                "ODK Collect",
                "Form updates available",
                MainMenuPage()
            )
    }

    @Test
    fun whenPreviouslyDownloadedOnlyEnabledWithAutomaticDownload_checkingAutoDownload_downloadsUpdatedForms_andDisplaysNotification() {
        rule.withProject(testDependencies.server, "one-question.xml")
            .enablePreviouslyDownloadedOnlyUpdatesWithAutomaticDownload()

        testDependencies.server.addForm(
            "One Question Updated",
            "one_question",
            "2",
            "one-question-updated.xml"
        )

        testDependencies.scheduler.runDeferredTasks()

        notificationDrawerRule.open()
            .assertNotification(
                "ODK Collect",
                "Forms download succeeded",
                "All downloads succeeded!"
            )
            .clickNotification(
                "ODK Collect",
                "Forms download succeeded",
                MainMenuPage()
            ).clickFillBlankForm()
            .assertText("One Question Updated")
    }

    @Test
    fun whenPreviouslyDownloadedOnlyEnabledWithAutomaticDownload_checkingAutoDownload_downloadsUpdatedForms_andDisplaysNotificationWhenFails() {
        testDependencies.server.errorOnFetchingForms()

        val mainMenuPage =
            rule.withProject(testDependencies.server, "one-question.xml")
                .enablePreviouslyDownloadedOnlyUpdatesWithAutomaticDownload()

        testDependencies.server.addForm(
            "One Question Updated",
            "one_question",
            "2",
            "one-question-updated.xml"
        )

        testDependencies.scheduler.runDeferredTasks()

        mainMenuPage.clickFillBlankForm()
            .assertFormDoesNotExist("One Question Updated")

        notificationDrawerRule.open()
            .assertNotification(
                "ODK Collect",
                "Forms download failed",
                "1 of 1 downloads failed!"
            )
            .clickAction(
                "ODK Collect",
                "Forms download failed",
                "Show details",
                ErrorPage()
            )
    }

    @Test
    fun whenPreviouslyDownloadedOnlyEnabled_getBlankFormsIsAvailable() {
        rule.withProject(testDependencies.server.url)
            .enablePreviouslyDownloadedOnlyUpdates()
            .assertText(org.fsr.collect.strings.R.string.get_forms)
    }

    @Test
    fun whenPreviouslyDownloadedOnlyEnabled_fillBlankFormRefreshButtonIsGone() {
        rule.withProject(testDependencies.server.url)
            .enablePreviouslyDownloadedOnlyUpdates()
            .clickFillBlankForm()
        onView(withId(R.id.menu_refresh)).check(doesNotExist())
    }

    @Test
    fun whenPreviouslyDownloadedOnlyDisabled_stopsCheckingForUpdates() {
        rule.withProject(testDependencies.server.url)
            .enablePreviouslyDownloadedOnlyUpdates()
            .enableManualUpdates()

        assertThat(testDependencies.scheduler.getDeferredTasks(), equalTo(emptyList()))
    }
}
