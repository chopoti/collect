package org.fsr.collect.shared.strings

import java.util.UUID

class UUIDGenerator {
    fun generateUUID() = UUID.randomUUID().toString()
}
