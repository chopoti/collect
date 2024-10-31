package org.fsr.collect.android.support.pages

class EntitiesPage : Page<EntitiesPage>() {

    override fun assertOnPage(): EntitiesPage {
        assertToolbarTitle(org.fsr.collect.strings.R.string.entities_title)
        return this
    }

    fun clickOnList(list: String): EntityListPage {
        clickOnText(list)
        return EntityListPage(list)
    }
}
