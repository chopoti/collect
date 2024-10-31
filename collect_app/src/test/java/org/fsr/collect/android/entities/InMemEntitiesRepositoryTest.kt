package org.fsr.collect.android.entities

import org.fsr.collect.entities.storage.EntitiesRepository
import org.fsr.collect.entities.storage.InMemEntitiesRepository

class InMemEntitiesRepositoryTest : EntitiesRepositoryTest() {

    override fun buildSubject(): EntitiesRepository {
        return InMemEntitiesRepository()
    }
}
