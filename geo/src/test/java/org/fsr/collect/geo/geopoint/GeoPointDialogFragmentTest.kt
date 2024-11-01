package org.fsr.collect.geo.geopoint

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.isEnabled
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.not
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.fsr.collect.androidshared.livedata.MutableNonNullLiveData
import org.fsr.collect.fragmentstest.FragmentScenarioLauncherRule
import org.fsr.collect.geo.DaggerGeoDependencyComponent
import org.fsr.collect.geo.GeoDependencyModule
import org.fsr.collect.geo.support.RobolectricApplication
import org.fsr.collect.strings.localization.getLocalizedString

@RunWith(AndroidJUnit4::class)
class GeoPointDialogFragmentTest {

    private val application = getApplicationContext<RobolectricApplication>()

    private val currentAccuracyLiveData: MutableLiveData<GeoPointAccuracy?> = MutableLiveData(null)
    private val timeElapsedLiveData: MutableNonNullLiveData<Long> = MutableNonNullLiveData(0)
    private val satellitesLiveData = MutableNonNullLiveData(0)
    private val viewModel = mock<GeoPointViewModel> {
        on { currentAccuracy } doReturn currentAccuracyLiveData
        on { timeElapsed } doReturn timeElapsedLiveData
        on { satellites } doReturn satellitesLiveData
    }

    @get:Rule
    val launcherRule = FragmentScenarioLauncherRule()

    @Before
    fun setup() {
        application.geoDependencyComponent = DaggerGeoDependencyComponent.builder()
            .application(application)
            .geoDependencyModule(object : GeoDependencyModule() {
                override fun providesGeoPointViewModelFactory(application: Application) =
                    object : GeoPointViewModelFactory {
                        @Suppress("UNCHECKED_CAST")
                        override fun <T : ViewModel> create(modelClass: Class<T>): T {
                            return viewModel as T
                        }
                    }
            })
            .build()
    }

    @Test
    fun `disables save until location is available`() {
        launcherRule.launch(GeoPointDialogFragment::class.java)
        onView(withText(org.fsr.collect.strings.R.string.save)).inRoot(isDialog()).check(matches(not(isEnabled())))

        currentAccuracyLiveData.value = GeoPointAccuracy.Improving(5.0f)
        onView(withText(org.fsr.collect.strings.R.string.save)).inRoot(isDialog()).check(matches(isEnabled()))
    }

    @Test
    fun `shows accuracy threshold`() {
        whenever(viewModel.accuracyThreshold).thenReturn(5.0f)
        launcherRule.launch(GeoPointDialogFragment::class.java)

        onView(withText(application.getLocalizedString(org.fsr.collect.strings.R.string.point_will_be_saved, "5m")))
            .inRoot(isDialog())
            .perform(scrollTo())
            .check(matches(isDisplayed()))
    }

    @Test
    fun `shows and updates current accuracy`() {
        val scenario = launcherRule.launch(GeoPointDialogFragment::class.java)

        currentAccuracyLiveData.value = GeoPointAccuracy.Improving(50.2f)
        scenario.onFragment {
            assertThat(it.binding.accuracyStatus.accuracy, equalTo(GeoPointAccuracy.Improving(50.2f)))
        }

        currentAccuracyLiveData.value = GeoPointAccuracy.Improving(15.65f)
        onView(withText("15.65m")).inRoot(isDialog())
            .perform(scrollTo()).check(matches(isDisplayed()))
        scenario.onFragment {
            assertThat(it.binding.accuracyStatus.accuracy, equalTo(GeoPointAccuracy.Improving(15.65f)))
        }
    }

    @Test
    fun `shows and updates time elapsed`() {
        launcherRule.launch(GeoPointDialogFragment::class.java)

        timeElapsedLiveData.value = 0
        onView(withText(application.getLocalizedString(org.fsr.collect.strings.R.string.time_elapsed, "00:00")))
            .inRoot(isDialog())
            .perform(scrollTo()).check(matches(isDisplayed()))

        timeElapsedLiveData.value = 62000
        onView(withText(application.getLocalizedString(org.fsr.collect.strings.R.string.time_elapsed, "01:02")))
            .inRoot(isDialog())
            .perform(scrollTo()).check(matches(isDisplayed()))
    }

    @Test
    fun `shows and updates satellites`() {
        launcherRule.launch(GeoPointDialogFragment::class.java)

        onView(withText(application.getLocalizedString(org.fsr.collect.strings.R.string.satellites, 0)))
            .inRoot(isDialog())
            .perform(scrollTo())
            .check(matches(isDisplayed()))

        satellitesLiveData.value = 5

        onView(withText(application.getLocalizedString(org.fsr.collect.strings.R.string.satellites, 5)))
            .inRoot(isDialog())
            .perform(scrollTo())
            .check(matches(isDisplayed()))
    }

    @Test
    fun `clicking cancel calls listener`() {
        val scenario = launcherRule.launch(GeoPointDialogFragment::class.java)

        val listener = mock<GeoPointDialogFragment.Listener>()
        scenario.onFragment {
            it.listener = listener
        }

        onView(withText(org.fsr.collect.strings.R.string.cancel)).inRoot(isDialog()).perform(click())
        verify(listener).onCancel()
    }

    @Test
    fun `pressing back calls listener`() {
        val scenario = launcherRule.launch(GeoPointDialogFragment::class.java)

        val listener = mock<GeoPointDialogFragment.Listener>()
        scenario.onFragment {
            it.listener = listener
        }

        Espresso.pressBack()
        verify(listener).onCancel()
    }

    @Test
    fun `clicking save calls forceLocation() on view model`() {
        launcherRule.launch(GeoPointDialogFragment::class.java)
        currentAccuracyLiveData.value = GeoPointAccuracy.Improving(5.0f)

        onView(withText(org.fsr.collect.strings.R.string.save)).inRoot(isDialog()).perform(click())
        verify(viewModel).forceLocation()
    }

    @Test
    fun `dialog is not cancellable`() {
        launcherRule.launch(GeoPointDialogFragment::class.java).onFragment {
            assertThat(it.isCancelable, equalTo(false))
        }
    }
}
