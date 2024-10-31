package org.fsr.collect.android.injection.config

import org.fsr.collect.permissions.PermissionsChecker
import org.fsr.collect.selfiecamera.SelfieCameraDependencyModule

class CollectSelfieCameraDependencyModule(
    private val appDependencyComponent: AppDependencyComponent
) : SelfieCameraDependencyModule() {
    override fun providesPermissionChecker(): PermissionsChecker {
        return appDependencyComponent.permissionsChecker()
    }
}
