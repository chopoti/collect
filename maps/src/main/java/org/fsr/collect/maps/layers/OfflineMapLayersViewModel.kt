package org.fsr.collect.maps.layers

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.fsr.collect.analytics.Analytics
import org.fsr.collect.androidshared.async.TrackableWorker
import org.fsr.collect.androidshared.data.Consumable
import org.fsr.collect.androidshared.system.copyToFile
import org.fsr.collect.androidshared.system.getFileExtension
import org.fsr.collect.androidshared.system.getFileName
import org.fsr.collect.async.Scheduler
import org.fsr.collect.maps.AnalyticsEvents
import org.fsr.collect.settings.SettingsProvider
import org.fsr.collect.settings.keys.ProjectKeys
import org.fsr.collect.shared.TempFiles
import java.io.File

class OfflineMapLayersViewModel(
    private val referenceLayerRepository: ReferenceLayerRepository,
    scheduler: Scheduler,
    private val settingsProvider: SettingsProvider
) : ViewModel() {
    val trackableWorker = TrackableWorker(scheduler)

    private val _existingLayers = MutableLiveData<List<ReferenceLayer>>()
    val existingLayers: LiveData<List<ReferenceLayer>> = _existingLayers

    private val _layersToImport = MutableLiveData<Consumable<LayersToImport>>()
    val layersToImport: LiveData<Consumable<LayersToImport>> = _layersToImport

    private lateinit var tempLayersDir: File

    fun loadExistingLayers() {
        trackableWorker.immediate(
            background = {
                val layers = referenceLayerRepository.getAll().sortedBy { it.name }
                _existingLayers.postValue(layers)
            }
        )
    }

    fun loadLayersToImport(uris: List<Uri>, context: Context) {
        trackableWorker.immediate(
            background = {
                tempLayersDir = TempFiles.createTempDir().also {
                    it.deleteOnExit()
                }
                val layers = mutableListOf<ReferenceLayer>()
                uris.forEach { uri ->
                    if (uri.getFileExtension(context) == MbtilesFile.FILE_EXTENSION) {
                        uri.getFileName(context)?.let { fileName ->
                            val layerFile = File(tempLayersDir, fileName).also { file ->
                                uri.copyToFile(context, file)
                            }
                            layers.add(ReferenceLayer(layerFile.absolutePath, layerFile, MbtilesFile.readName(layerFile) ?: layerFile.name))
                        }
                    }
                }
                _layersToImport.postValue(
                    Consumable(
                        LayersToImport(
                            uris.size,
                            uris.size - layers.size,
                            layers.sortedBy { it.name }
                        )
                    )
                )
            }
        )
    }

    fun importNewLayers(shared: Boolean) {
        trackableWorker.immediate(
            background = {
                val layers = tempLayersDir.listFiles()
                logImport(layers)

                layers?.forEach {
                    referenceLayerRepository.addLayer(it, shared)
                }
                tempLayersDir.delete()
            },
            foreground = {
                loadExistingLayers()
            }
        )
    }

    fun saveCheckedLayer(layerId: String?) {
        settingsProvider.getUnprotectedSettings().save(ProjectKeys.KEY_REFERENCE_LAYER, layerId)
    }

    fun deleteLayer(layerId: String) {
        trackableWorker.immediate {
            if (settingsProvider.getUnprotectedSettings().getString(ProjectKeys.KEY_REFERENCE_LAYER) == layerId) {
                settingsProvider.getUnprotectedSettings().save(ProjectKeys.KEY_REFERENCE_LAYER, null)
            }

            referenceLayerRepository.delete(layerId)
            _existingLayers.postValue(_existingLayers.value?.filter { it.id != layerId })
        }
    }

    private fun logImport(layers: Array<File>?) {
        val count = layers?.size ?: return
        val event = when {
            count == 1 -> AnalyticsEvents.IMPORT_LAYER_SINGLE
            count <= 5 -> AnalyticsEvents.IMPORT_LAYER_FEW
            else -> AnalyticsEvents.IMPORT_LAYER_MANY
        }

        Analytics.log(event)
    }

    data class LayersToImport(
        val numberOfSelectedLayers: Int,
        val numberOfUnsupportedLayers: Int,
        val layers: List<ReferenceLayer>
    )
}
