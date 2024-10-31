package org.fsr.collect.geo

import android.Manifest
import android.app.Activity
import org.fsr.collect.androidshared.ui.ToastUtils
import org.fsr.collect.permissions.ContextCompatPermissionChecker

internal object GeoActivityUtils {

    @JvmStatic
    fun requireLocationPermissions(activity: Activity) {
        val permissionGranted = ContextCompatPermissionChecker(activity).isPermissionGranted(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

        if (!permissionGranted) {
            ToastUtils.showLongToast(activity, org.fsr.collect.strings.R.string.not_granted_permission)
            activity.finish()
        }
    }
}
