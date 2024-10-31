package org.fsr.collect.async

interface Cancellable {
    fun cancel(): Boolean
}
