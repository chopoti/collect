package org.fsr.collect.android.notifications

import org.fsr.collect.android.formmanagement.ServerFormDetails
import org.fsr.collect.android.formmanagement.download.FormDownloadException
import org.fsr.collect.android.upload.FormUploadException
import org.fsr.collect.forms.FormSourceException
import org.fsr.collect.forms.instances.Instance

interface Notifier {
    fun onUpdatesAvailable(updates: List<ServerFormDetails>, projectId: String)
    fun onUpdatesDownloaded(result: Map<ServerFormDetails, FormDownloadException?>, projectId: String)
    fun onSync(exception: FormSourceException?, projectId: String)
    fun onSubmission(result: Map<Instance, FormUploadException?>, projectId: String)
}
