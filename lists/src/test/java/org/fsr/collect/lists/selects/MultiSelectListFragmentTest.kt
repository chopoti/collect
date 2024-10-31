package org.fsr.collect.lists.selects

import androidx.lifecycle.MutableLiveData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isChecked
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hamcrest.Matchers.not
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.fsr.collect.androidshared.ui.FragmentFactoryBuilder
import org.fsr.collect.fragmentstest.FragmentScenarioLauncherRule
import org.fsr.collect.lists.R
import org.fsr.collect.lists.selects.support.TextAndCheckBoxView
import org.fsr.collect.lists.selects.support.TextAndCheckBoxViewHolder
import org.fsr.collect.testshared.RecyclerViewMatcher.Companion.withRecyclerView
import org.fsr.collect.testshared.ViewActions.clickOnItemWith
import org.fsr.collect.testshared.ViewMatchers.recyclerView

@RunWith(AndroidJUnit4::class)
class MultiSelectListFragmentTest {

    private val data = MutableLiveData<List<SelectItem<String>>>(emptyList())
    private val multiSelectViewModel = MultiSelectViewModel(data)

    @get:Rule
    val fragmentScenarioLauncherRule = FragmentScenarioLauncherRule(
        FragmentFactoryBuilder()
            .forClass(MultiSelectListFragment::class) {
                MultiSelectListFragment(
                    "Action",
                    multiSelectViewModel,
                    { parent -> TextAndCheckBoxViewHolder(parent.context) }
                )
            }.build()
    )

    @Test
    fun `empty message shows when there are no forms`() {
        fragmentScenarioLauncherRule.launchInContainer(MultiSelectListFragment::class.java)
        onView(withId(R.id.empty)).check(matches(isDisplayed()))

        data.value = listOf(SelectItem("1", "Blah"))
        onView(withId(R.id.empty)).check(matches(not(isDisplayed())))
    }

    @Test
    fun `bottom buttons are hidden when there are no forms`() {
        fragmentScenarioLauncherRule.launchInContainer(MultiSelectListFragment::class.java)
        onView(withId(R.id.buttons)).check(matches(not(isDisplayed())))

        data.value = listOf(SelectItem("1", "Blah"))
        onView(withId(R.id.buttons)).check(matches(isDisplayed()))
    }

    @Test
    fun `recreating maintains selection`() {
        val scenario =
            fragmentScenarioLauncherRule.launchInContainer(MultiSelectListFragment::class.java)
        data.value = listOf(SelectItem("1", "Blah 1"), SelectItem("1", "Blah 2"))

        onView(recyclerView()).perform(clickOnItemWith(withText("Blah 2")))

        scenario.recreate()
        onView(withRecyclerView(R.id.list).atPositionOnView(1, TextAndCheckBoxView.TEXT_VIEW_ID))
            .check(matches(withText("Blah 2")))
        onView(withRecyclerView(R.id.list).atPositionOnView(1, TextAndCheckBoxView.CHECK_BOX_ID))
            .check(matches(isChecked()))
    }
}
