package org.fsr.collect.android.support.pages

class ProjectManagementPage : Page<ProjectManagementPage>() {

    override fun assertOnPage(): ProjectManagementPage {
        assertText(org.fsr.collect.strings.R.string.project_management_section_title)
        return this
    }

    fun clickOnResetProject(): ProjectManagementPage {
        clickOnString(org.fsr.collect.strings.R.string.reset_project_settings_title)
        return this
    }

    fun clickConfigureQR(): QRCodePage {
        clickOnString(org.fsr.collect.strings.R.string.reconfigure_with_qr_code_settings_title)
        return QRCodePage().assertOnPage()
    }

    fun clickOnDeleteProject(): ProjectManagementPage {
        scrollToRecyclerViewItemAndClickText(org.fsr.collect.strings.R.string.delete_project)
        return this
    }

    fun deleteProject(): MainMenuPage {
        scrollToRecyclerViewItemAndClickText(org.fsr.collect.strings.R.string.delete_project)
        clickOnString(org.fsr.collect.strings.R.string.delete_project_yes)
        return MainMenuPage()
    }

    fun deleteLastProject(): FirstLaunchPage {
        scrollToRecyclerViewItemAndClickText(org.fsr.collect.strings.R.string.delete_project)
        clickOnString(org.fsr.collect.strings.R.string.delete_project_yes)
        return FirstLaunchPage()
    }
}
