package org.fsr.collect.android.formentry.support

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.fsr.collect.android.formentry.FormSession
import org.fsr.collect.android.formentry.FormSessionRepository
import org.fsr.collect.android.javarosawrapper.FormController
import org.fsr.collect.forms.Form
import org.fsr.collect.forms.instances.Instance
import org.fsr.collect.shared.strings.UUIDGenerator

class InMemFormSessionRepository : FormSessionRepository {

    private val map = mutableMapOf<String, MutableLiveData<FormSession>>()

    override fun create(): String {
        return UUIDGenerator().generateUUID()
    }

    override fun get(id: String): LiveData<FormSession> {
        return getLiveData(id)
    }

    override fun set(id: String, formController: FormController, form: Form, instance: Instance?) {
        getLiveData(id).value = FormSession(formController, form, instance)
    }

    override fun update(id: String, instance: Instance?) {
        val liveData = getLiveData(id)
        liveData.value?.let {
            liveData.value = it.copy(instance = instance)
        }
    }

    override fun clear(id: String) {
        getLiveData(id).value = null
        map.remove(id)
    }

    private fun getLiveData(id: String): MutableLiveData<FormSession> {
        return map.getOrPut(id) { MutableLiveData<FormSession>(null) }
    }
}