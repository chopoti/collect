package org.fsr.collect.formstest

import org.fsr.collect.forms.savepoints.SavepointsRepository

class InMemSavepointsRepositoryTest : SavepointsRepositoryTest() {
    override fun buildSubject(cacheDirPath: String, instancesDirPath: String): SavepointsRepository {
        return InMemSavepointsRepository()
    }
}
