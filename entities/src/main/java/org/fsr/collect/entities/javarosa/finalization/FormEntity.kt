package org.fsr.collect.entities.javarosa.finalization

import org.fsr.collect.entities.javarosa.spec.EntityAction

class FormEntity(
    @JvmField val action: EntityAction,
    @JvmField val dataset: String,
    @JvmField val id: String?,
    @JvmField val label: String?,
    @JvmField val properties: List<Pair<String, String>>
)
