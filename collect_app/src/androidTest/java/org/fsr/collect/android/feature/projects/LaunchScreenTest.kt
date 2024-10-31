package org.fsr.collect.android.feature.projects

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain
import org.junit.runner.RunWith
import org.fsr.collect.android.R
import org.fsr.collect.android.injection.config.AppDependencyModule
import org.fsr.collect.android.support.StubBarcodeViewDecoder
import org.fsr.collect.android.support.pages.MainMenuPage
import org.fsr.collect.android.support.rules.CollectTestRule
import org.fsr.collect.android.support.rules.ResetStateRule
import org.fsr.collect.android.support.rules.TestRuleChain
import org.fsr.collect.android.views.BarcodeViewDecoder

@RunWith(AndroidJUnit4::class)
class LaunchScreenTest {

    private val rule = CollectTestRule(false)

    private val stubBarcodeViewDecoder = StubBarcodeViewDecoder()

    @get:Rule
    val chain: RuleChain = TestRuleChain.chain()
        .around(
            ResetStateRule(object : AppDependencyModule() {
                override fun providesBarcodeViewDecoder(): BarcodeViewDecoder {
                    return stubBarcodeViewDecoder
                }
            })
        )
        .around(rule)

    @Test
    fun clickingTryCollectAtLaunch_setsAppUpWithDemoProject() {
        rule.startAtFirstLaunch()
            .clickTryCollect()
            .openProjectSettingsDialog()
            .assertCurrentProject("Demo project", "demo.getodk.org")
            .clickSettings()
            .clickServerSettings()
            .clickOnURL()
            .assertText("https://demo.getodk.org")
    }

    @Test
    fun clickingManuallyEnterProjectDetails_andAddingProjectDetails_setsAppUpWithProjectDetails() {
        rule.startAtFirstLaunch()
            .clickManuallyEnterProjectDetails()
            .inputUrl("https://my-server.com")
            .inputUsername("John")
            .addProject()
            .assertProjectIcon("M")
            .openProjectSettingsDialog()
            .assertCurrentProject("my-server.com", "John / my-server.com")
    }

    @Test
    fun clickingAutomaticallyEnterProjectDetails_andScanningQRCode_setsAppUpWithProjectDetails() {
        val page = rule.startAtFirstLaunch()
            .clickConfigureWithQrCode()

        stubBarcodeViewDecoder.scan("{\"general\":{\"server_url\":\"https:\\/\\/my-server.com\",\"username\":\"adam\",\"password\":\"1234\"},\"admin\":{}}")
        page.checkIsToastWithMessageDisplayed(org.fsr.collect.strings.R.string.switched_project, "my-server.com")

        MainMenuPage()
            .assertOnPage()
            .openProjectSettingsDialog()
            .assertCurrentProject("my-server.com", "adam / my-server.com")
    }

    @Test
    fun whenThereAreProjects_goesToMainMenu() {
        rule.withProject("https://example.com")
        rule.relaunch(MainMenuPage())
    }
}
