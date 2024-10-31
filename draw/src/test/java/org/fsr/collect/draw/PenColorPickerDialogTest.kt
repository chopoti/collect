package org.fsr.collect.draw

import android.graphics.Color
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.fsr.collect.androidshared.livedata.MutableNonNullLiveData
import org.fsr.collect.androidshared.ui.FragmentFactoryBuilder
import org.fsr.collect.fragmentstest.FragmentScenarioLauncherRule

@RunWith(AndroidJUnit4::class)
class PenColorPickerDialogTest {

    private val viewModel = mock<PenColorPickerViewModel>().also {
        whenever(it.penColor).thenReturn(MutableNonNullLiveData(Color.BLACK))
    }

    @get:Rule
    val launcherRule = FragmentScenarioLauncherRule(
        defaultFactory = FragmentFactoryBuilder()
            .forClass(PenColorPickerDialog::class) {
                PenColorPickerDialog(
                    viewModel
                )
            }
            .build()
    )

    @Test
    fun `dialog should be cancelable`() {
        val scenario = launcherRule.launch(PenColorPickerDialog::class.java)
        scenario.onFragment {
            assertThat(it.isCancelable, `is`(true))
        }
    }

    @Test
    fun `pen color in view model should be set after clicking ok`() {
        launcherRule.launch(PenColorPickerDialog::class.java)

        onView(withText(org.fsr.collect.strings.R.string.ok)).inRoot(isDialog()).perform(click())

        verify(viewModel).setPenColor(Color.BLACK)
    }
}
