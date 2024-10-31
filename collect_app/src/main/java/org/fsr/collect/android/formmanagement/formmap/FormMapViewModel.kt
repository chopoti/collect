package org.fsr.collect.android.formmanagement.formmap

import android.content.res.Resources
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.json.JSONException
import org.json.JSONObject
import org.fsr.collect.android.R
import org.fsr.collect.android.instancemanagement.getStatusDescription
import org.fsr.collect.android.instancemanagement.showAsEditable
import org.fsr.collect.androidshared.livedata.MutableNonNullLiveData
import org.fsr.collect.androidshared.livedata.NonNullLiveData
import org.fsr.collect.async.Scheduler
import org.fsr.collect.forms.Form
import org.fsr.collect.forms.FormsRepository
import org.fsr.collect.forms.instances.Instance
import org.fsr.collect.forms.instances.InstancesRepository
import org.fsr.collect.geo.selection.IconifiedText
import org.fsr.collect.geo.selection.MappableSelectItem
import org.fsr.collect.geo.selection.SelectionMapData
import org.fsr.collect.geo.selection.Status
import org.fsr.collect.maps.MapPoint
import org.fsr.collect.settings.SettingsProvider
import org.fsr.collect.settings.keys.ProtectedProjectKeys
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.Locale

class FormMapViewModel(
    private val resources: Resources,
    private val formId: Long,
    private val formsRepository: FormsRepository,
    private val instancesRepository: InstancesRepository,
    private val settingsProvider: SettingsProvider,
    private val scheduler: Scheduler
) : ViewModel(), SelectionMapData {

    private var _form: Form? = null

    private val mapTitle = MutableLiveData<String?>()
    private var mappableItems = MutableLiveData<List<MappableSelectItem>>(null)
    private var itemCount = MutableNonNullLiveData(0)
    private val isLoading = MutableNonNullLiveData(false)

    override fun getMapTitle(): LiveData<String?> {
        return mapTitle
    }

    override fun getItemType(): String {
        return resources.getString(org.fsr.collect.strings.R.string.saved_forms)
    }

    override fun getItemCount(): NonNullLiveData<Int> {
        return itemCount
    }

    override fun getMappableItems(): LiveData<List<MappableSelectItem>?> {
        return mappableItems
    }

    override fun isLoading(): NonNullLiveData<Boolean> {
        return isLoading
    }

    fun load() {
        isLoading.value = true

        scheduler.immediate(
            background = {
                val form = _form ?: formsRepository.get(formId)!!.also { _form = it }
                val instances = instancesRepository.getAllByFormId(form.formId)
                val items = mutableListOf<MappableSelectItem>()

                for (instance in instances) {
                    if (instance.geometry != null && instance.geometryType == Instance.GEOMETRY_TYPE_POINT) {
                        try {
                            val geometry = JSONObject(instance.geometry)
                            val coordinates = geometry.getJSONArray("coordinates")

                            // In GeoJSON, longitude comes before latitude.
                            val lon = coordinates.getDouble(0)
                            val lat = coordinates.getDouble(1)

                            items.add(createItem(instance, lat, lon))
                        } catch (e: JSONException) {
                            Timber.w("Invalid JSON in instances table: %s", instance.geometry)
                        }
                    }
                }

                Triple(form.displayName, items, instances.size)
            },
            foreground = {
                mapTitle.value = it.first
                mappableItems.value = it.second as List<MappableSelectItem>
                itemCount.value = it.third
                isLoading.value = false
            }
        )
    }

    private fun createItem(
        instance: Instance,
        latitude: Double,
        longitude: Double
    ): MappableSelectItem {
        val instanceLastStatusChangeDate = instance.getStatusDescription(resources)

        return if (instance.deletedDate != null) {
            val deletedTime = resources.getString(org.fsr.collect.strings.R.string.deleted_on_date_at_time)
            val dateFormat = SimpleDateFormat(
                deletedTime,
                Locale.getDefault()
            )

            val info = "$instanceLastStatusChangeDate\n${dateFormat.format(instance.deletedDate)}"
            MappableSelectItem.MappableSelectPoint(
                instance.dbId,
                instance.displayName,
                point = MapPoint(latitude, longitude),
                smallIcon = getDrawableIdForStatus(instance.status, false),
                largeIcon = getDrawableIdForStatus(instance.status, true),
                info = info
            )
        } else if (!instance.canEditWhenComplete() && listOf(
                Instance.STATUS_COMPLETE,
                Instance.STATUS_SUBMISSION_FAILED,
                Instance.STATUS_SUBMITTED
            ).contains(instance.status)
        ) {
            val info = "$instanceLastStatusChangeDate\n${resources.getString(org.fsr.collect.strings.R.string.cannot_edit_completed_form)}"
            MappableSelectItem.MappableSelectPoint(
                instance.dbId,
                instance.displayName,
                point = MapPoint(latitude, longitude),
                smallIcon = getDrawableIdForStatus(instance.status, false),
                largeIcon = getDrawableIdForStatus(instance.status, true),
                info = info
            )
        } else {
            val action =
                if (instance.showAsEditable(settingsProvider)) {
                    createEditAction()
                } else {
                    createViewAction()
                }

            MappableSelectItem.MappableSelectPoint(
                instance.dbId,
                instance.displayName,
                point = MapPoint(latitude, longitude),
                smallIcon = getDrawableIdForStatus(instance.status, false),
                largeIcon = getDrawableIdForStatus(instance.status, true),
                info = instanceLastStatusChangeDate,
                action = action,
                status = instanceStatusToMappableSelectionItemStatus(instance)
            )
        }
    }

    private fun instanceStatusToMappableSelectionItemStatus(instance: Instance): Status? {
        return when (instance.status) {
            Instance.STATUS_INVALID, Instance.STATUS_INCOMPLETE -> Status.ERRORS
            Instance.STATUS_VALID -> Status.NO_ERRORS
            else -> null
        }
    }

    private fun createViewAction(): IconifiedText {
        return IconifiedText(
            R.drawable.ic_visibility,
            resources.getString(org.fsr.collect.strings.R.string.view_data)
        )
    }

    private fun createEditAction(): IconifiedText {
        val canEditSaved = settingsProvider.getProtectedSettings()
            .getBoolean(ProtectedProjectKeys.KEY_EDIT_SAVED)

        return IconifiedText(
            if (canEditSaved) R.drawable.ic_edit else R.drawable.ic_visibility,
            resources.getString(if (canEditSaved) org.fsr.collect.strings.R.string.edit_data else org.fsr.collect.strings.R.string.view_data)
        )
    }

    private fun getDrawableIdForStatus(status: String, enlarged: Boolean): Int {
        return when (status) {
            Instance.STATUS_INCOMPLETE, Instance.STATUS_VALID, Instance.STATUS_INVALID -> if (enlarged) R.drawable.ic_room_form_state_incomplete_48dp else R.drawable.ic_room_form_state_incomplete_24dp
            Instance.STATUS_COMPLETE -> if (enlarged) R.drawable.ic_room_form_state_complete_48dp else R.drawable.ic_room_form_state_complete_24dp
            Instance.STATUS_SUBMITTED -> if (enlarged) R.drawable.ic_room_form_state_submitted_48dp else R.drawable.ic_room_form_state_submitted_24dp
            Instance.STATUS_SUBMISSION_FAILED -> if (enlarged) R.drawable.ic_room_form_state_submission_failed_48dp else R.drawable.ic_room_form_state_submission_failed_24dp
            else -> org.fsr.collect.icons.R.drawable.ic_map_point
        }
    }
}
