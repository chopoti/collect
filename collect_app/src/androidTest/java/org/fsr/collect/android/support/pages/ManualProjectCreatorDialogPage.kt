package org.fsr.collect.android.support.pages

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withText
import org.fsr.collect.testshared.WaitFor.tryAgainOnFail

class ManualProjectCreatorDialogPage : Page<ManualProjectCreatorDialogPage>() {
    override fun assertOnPage(): ManualProjectCreatorDialogPage {
        assertText(org.fsr.collect.strings.R.string.add_project)
        return this
    }

    fun inputUrl(url: String): ManualProjectCreatorDialogPage {
        inputText(org.fsr.collect.strings.R.string.server_url, url)
        return this
    }

    fun inputUsername(username: String): ManualProjectCreatorDialogPage {
        inputText(org.fsr.collect.strings.R.string.username, username)
        return this
    }

    fun inputPassword(password: String): ManualProjectCreatorDialogPage {
        inputText(org.fsr.collect.strings.R.string.password, password)
        return this
    }

    fun addProject(): MainMenuPage {
        tryAgainOnFail {
            clickOnString(org.fsr.collect.strings.R.string.add)
            MainMenuPage().assertOnPage()
        }

        return MainMenuPage()
    }

    fun addProjectAndAssertDuplicateDialogShown(): ManualProjectCreatorDialogPage {
        onView(withText(org.fsr.collect.strings.R.string.add)).perform(click())
        assertText(org.fsr.collect.strings.R.string.duplicate_project_details)
        return this
    }

    fun switchToExistingProject(): MainMenuPage {
        clickOnString(org.fsr.collect.strings.R.string.switch_to_existing)
        return MainMenuPage().assertOnPage()
    }

    fun addDuplicateProject(): MainMenuPage {
        clickOnString(org.fsr.collect.strings.R.string.add_duplicate_project)
        return MainMenuPage().assertOnPage()
    }
}
