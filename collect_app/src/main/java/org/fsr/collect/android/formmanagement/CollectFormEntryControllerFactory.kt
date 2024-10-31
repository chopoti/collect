package org.fsr.collect.android.formmanagement

import org.javarosa.core.model.FormDef
import org.javarosa.form.api.FormEntryController
import org.javarosa.form.api.FormEntryModel
import org.fsr.collect.android.application.Collect
import org.fsr.collect.android.dynamicpreload.ExternalDataManagerImpl
import org.fsr.collect.android.dynamicpreload.handler.ExternalDataHandlerPull
import org.fsr.collect.android.tasks.FormLoaderTask.FormEntryControllerFactory
import org.fsr.collect.entities.javarosa.filter.LocalEntitiesFilterStrategy
import org.fsr.collect.entities.javarosa.finalization.EntityFormFinalizationProcessor
import org.fsr.collect.entities.storage.EntitiesRepository
import org.fsr.collect.settings.keys.ProjectKeys
import org.fsr.collect.shared.settings.Settings
import java.io.File

class CollectFormEntryControllerFactory(
    private val entitiesRepository: EntitiesRepository,
    private val settings: Settings
) :
    FormEntryControllerFactory {
    override fun create(formDef: FormDef, formMediaDir: File): FormEntryController {
        val externalDataManager = ExternalDataManagerImpl(formMediaDir).also {
            Collect.getInstance().externalDataManager = it
        }

        return FormEntryController(FormEntryModel(formDef)).also {
            it.addFunctionHandler(ExternalDataHandlerPull(externalDataManager))
            it.addPostProcessor(EntityFormFinalizationProcessor())

            if (settings.getBoolean(ProjectKeys.KEY_LOCAL_ENTITIES)) {
                it.addFilterStrategy(LocalEntitiesFilterStrategy(entitiesRepository))
            }
        }
    }
}
