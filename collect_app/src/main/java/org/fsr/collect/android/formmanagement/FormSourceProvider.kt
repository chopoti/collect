package org.fsr.collect.android.formmanagement

import org.fsr.collect.android.openrosa.OpenRosaFormSource
import org.fsr.collect.android.openrosa.OpenRosaHttpInterface
import org.fsr.collect.android.openrosa.OpenRosaResponseParserImpl
import org.fsr.collect.android.utilities.WebCredentialsUtils
import org.fsr.collect.forms.FormSource
import org.fsr.collect.projects.ProjectDependencyFactory
import org.fsr.collect.settings.keys.ProjectKeys
import org.fsr.collect.shared.settings.Settings

class FormSourceProvider(
    private val settingsFactory: ProjectDependencyFactory<Settings>,
    private val openRosaHttpInterface: OpenRosaHttpInterface
) : ProjectDependencyFactory<FormSource> {

    override fun create(projectId: String): FormSource {
        val settings = settingsFactory.create(projectId)
        val serverURL = settings.getString(ProjectKeys.KEY_SERVER_URL)

        return OpenRosaFormSource(
            serverURL,
            openRosaHttpInterface,
            WebCredentialsUtils(settings),
            OpenRosaResponseParserImpl()
        )
    }
}
