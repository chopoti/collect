package org.fsr.collect.android.feature.external

import android.content.Context
import android.content.Intent
import android.provider.BaseColumns._ID
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain
import org.junit.runner.RunWith
import org.fsr.collect.android.external.InstancesContract
import org.fsr.collect.android.support.TestDependencies
import org.fsr.collect.android.support.pages.FormEntryPage
import org.fsr.collect.android.support.pages.OkDialog
import org.fsr.collect.android.support.rules.CollectTestRule
import org.fsr.collect.android.support.rules.TestRuleChain
import org.fsr.collect.android.utilities.ApplicationConstants

@RunWith(AndroidJUnit4::class)
class InstanceUploadActionTest {

    private val rule = CollectTestRule()
    private val context = ApplicationProvider.getApplicationContext<Context>()
    private val testDependencies = TestDependencies()

    @get:Rule
    val chain: RuleChain = TestRuleChain.chain(testDependencies)
        .around(rule)

    @Test
    fun whenIntentIncludesURLExtra_instancesAreUploadedToThatURL() {
        rule.startAtMainMenu()
            .copyForm("one-question.xml")
            .startBlankForm("One Question")
            .fillOutAndFinalize(FormEntryPage.QuestionAndAnswer("what is your age", "34"))

        val instanceId =
            context.contentResolver.query(InstancesContract.getUri("DEMO"), null, null, null, null)
                .use {
                    it!!.moveToFirst()
                    it.getLong(it.getColumnIndex(_ID))
                }

        val intent = Intent("org.fsr.collect.android.INSTANCE_UPLOAD")
        intent.type = InstancesContract.CONTENT_TYPE
        intent.putExtra(ApplicationConstants.BundleKeys.URL, testDependencies.server.url)
        intent.putExtra("instances", longArrayOf(instanceId))

        rule.launch(intent, OkDialog())
            .assertTextInDialog("One Question - Success")
        assertThat(testDependencies.server.submissions.size, equalTo(1))
    }

    @Test
    fun whenInstanceDoesNotExist_showsError() {
        rule.startAtMainMenu()

        val intent = Intent("org.fsr.collect.android.INSTANCE_UPLOAD")
        intent.type = InstancesContract.CONTENT_TYPE
        intent.putExtra("instances", longArrayOf(11))

        rule.launch(intent, OkDialog())
            .assertText(org.fsr.collect.strings.R.string.no_forms_uploaded)
    }
}
