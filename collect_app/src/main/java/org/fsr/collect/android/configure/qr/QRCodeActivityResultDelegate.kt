package org.fsr.collect.android.configure.qr

import android.app.Activity
import android.content.Intent
import android.widget.Toast
import org.fsr.collect.analytics.Analytics.Companion.log
import org.fsr.collect.android.activities.ActivityUtils
import org.fsr.collect.android.analytics.AnalyticsEvents
import org.fsr.collect.android.mainmenu.MainMenuActivity
import org.fsr.collect.projects.Project.Saved
import org.fsr.collect.qrcode.QRCodeDecoder
import org.fsr.collect.settings.ODKAppSettingsImporter
import org.fsr.collect.settings.importing.SettingsImportingResult
import java.io.FileNotFoundException
import java.io.InputStream

class QRCodeActivityResultDelegate(
    private val activity: Activity,
    private val settingsImporter: ODKAppSettingsImporter,
    private val qrCodeDecoder: QRCodeDecoder,
    private val project: Saved
) {
    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == QRCodeMenuProvider.SELECT_PHOTO && resultCode == Activity.RESULT_OK && data != null) {
            val imageUri = data.data
            if (imageUri != null) {
                val imageStream: InputStream? = try {
                    activity.contentResolver.openInputStream(imageUri)
                } catch (e: FileNotFoundException) {
                    // Not sure how this could happen? If you work it out: write a test!
                    return
                }
                try {
                    val response = qrCodeDecoder.decode(imageStream)

                    when (settingsImporter.fromJSON(response, project)) {
                        SettingsImportingResult.SUCCESS -> {
                            log(AnalyticsEvents.RECONFIGURE_PROJECT)
                            showToast(org.fsr.collect.strings.R.string.successfully_imported_settings)
                            ActivityUtils.startActivityAndCloseAllOthers(
                                activity,
                                MainMenuActivity::class.java
                            )
                        }
                        SettingsImportingResult.INVALID_SETTINGS -> showToast(org.fsr.collect.strings.R.string.invalid_qrcode)
                        SettingsImportingResult.GD_PROJECT -> showToast(org.fsr.collect.strings.R.string.settings_with_gd_protocol)
                    }
                } catch (e: QRCodeDecoder.QRCodeInvalidException) {
                    showToast(org.fsr.collect.strings.R.string.invalid_qrcode)
                } catch (e: QRCodeDecoder.QRCodeNotFoundException) {
                    showToast(org.fsr.collect.strings.R.string.qr_code_not_found)
                }
            }
        }
    }

    private fun showToast(string: Int) {
        Toast.makeText(activity, activity.getString(string), Toast.LENGTH_LONG).show()
    }
}
