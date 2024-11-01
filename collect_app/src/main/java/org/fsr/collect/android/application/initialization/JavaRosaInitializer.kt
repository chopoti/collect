package org.fsr.collect.android.application.initialization

import org.javarosa.core.model.CoreModelModule
import org.javarosa.core.services.PrototypeManager
import org.javarosa.core.util.JavaRosaCoreModule
import org.javarosa.model.xform.XFormsModule
import org.javarosa.xform.parse.XFormParser
import org.javarosa.xform.parse.XFormParserFactory
import org.javarosa.xform.util.XFormUtils
import org.fsr.collect.android.dynamicpreload.DynamicPreloadXFormParserFactory
import org.fsr.collect.android.logic.actions.setgeopoint.CollectSetGeopointActionHandler
import org.fsr.collect.android.projects.ProjectsDataService
import org.fsr.collect.entities.javarosa.intance.LocalEntitiesExternalInstanceParserFactory
import org.fsr.collect.entities.javarosa.parse.EntityXFormParserFactory
import org.fsr.collect.entities.storage.EntitiesRepository
import org.fsr.collect.metadata.PropertyManager
import org.fsr.collect.projects.ProjectDependencyFactory
import org.fsr.collect.settings.SettingsProvider
import org.fsr.collect.settings.keys.ProjectKeys

class JavaRosaInitializer(
    private val propertyManager: PropertyManager,
    private val projectsDataService: ProjectsDataService,
    private val entitiesRepositoryProvider: ProjectDependencyFactory<EntitiesRepository>,
    private val settingsProvider: SettingsProvider
) {

    fun initialize() {
        propertyManager.reload()
        org.javarosa.core.services.PropertyManager
            .setPropertyManager(propertyManager)

        // Register prototypes for classes that FormDef uses
        PrototypeManager.registerPrototypes(JavaRosaCoreModule.classNames)
        PrototypeManager.registerPrototypes(CoreModelModule.classNames)
        XFormsModule().registerModule()

        // When registering prototypes from Collect, a proguard exception also needs to be added
        PrototypeManager.registerPrototype("org.fsr.collect.android.logic.actions.setgeopoint.CollectSetGeopointAction")
        XFormParser.registerActionHandler(
            CollectSetGeopointActionHandler.ELEMENT_NAME,
            CollectSetGeopointActionHandler()
        )

        // Configure default parser factory
        val entityXFormParserFactory =
            EntityXFormParserFactory(
                XFormParserFactory()
            )
        val dynamicPreloadXFormParserFactory =
            DynamicPreloadXFormParserFactory(entityXFormParserFactory)

        XFormUtils.setXFormParserFactory(dynamicPreloadXFormParserFactory)

        val localEntitiesExternalInstanceParserFactory = LocalEntitiesExternalInstanceParserFactory(
            { entitiesRepositoryProvider.create(projectsDataService.getCurrentProject().uuid) },
            { settingsProvider.getUnprotectedSettings().getBoolean(ProjectKeys.KEY_LOCAL_ENTITIES) }
        )

        XFormUtils.setExternalInstanceParserFactory(localEntitiesExternalInstanceParserFactory)
    }
}
