package org.fsr.collect.android.mainmenu

import android.app.Dialog
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.fsr.collect.android.R
import org.fsr.collect.permissions.PermissionListener
import org.fsr.collect.permissions.PermissionsProvider

class PermissionsDialogFragment(
    private val permissionsProvider: PermissionsProvider,
    private val requestPermissionsViewModel: RequestPermissionsViewModel
) : DialogFragment() {

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        isCancelable = false

        return MaterialAlertDialogBuilder(requireContext())
            .setTitle(org.fsr.collect.strings.R.string.permission_dialog_title)
            .setView(R.layout.permissions_dialog_layout)
            .setPositiveButton(org.fsr.collect.strings.R.string.ok) { _, _ ->
                requestPermissionsViewModel.permissionsRequested()
                permissionsProvider.requestPermissions(
                    requireActivity(),
                    object : PermissionListener {
                        override fun granted() {}
                    },
                    *requestPermissionsViewModel.permissions
                )
            }
            .create()
    }
}
