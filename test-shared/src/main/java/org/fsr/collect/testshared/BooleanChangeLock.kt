package org.fsr.collect.testshared

import org.fsr.collect.shared.locks.ChangeLock
import java.util.function.Function

class BooleanChangeLock : ChangeLock {
    private var locked = false

    override fun <T> withLock(function: Function<Boolean, T>): T {
        return function.apply(!locked)
    }

    fun lock() {
        locked = true
    }
}
