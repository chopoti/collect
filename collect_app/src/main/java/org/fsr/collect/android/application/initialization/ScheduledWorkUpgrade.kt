package org.fsr.collect.android.application.initialization

import org.fsr.collect.android.backgroundwork.FormUpdateScheduler
import org.fsr.collect.android.backgroundwork.InstanceSubmitScheduler
import org.fsr.collect.async.Scheduler
import org.fsr.collect.projects.ProjectsRepository
import org.fsr.collect.upgrade.Upgrade

/**
 * Reschedule all background work to prevent problems with tag or class name changes etc
 */
class ScheduledWorkUpgrade(
    private val scheduler: Scheduler,
    private val projectsRepository: ProjectsRepository,
    private val formUpdateScheduler: FormUpdateScheduler,
    private val instanceSubmitScheduler: InstanceSubmitScheduler
) : Upgrade {

    override fun key(): String? {
        return null
    }

    override fun run() {
        scheduler.cancelAllDeferred()

        projectsRepository.getAll().forEach {
            formUpdateScheduler.scheduleUpdates(it.uuid)
            instanceSubmitScheduler.scheduleAutoSend(it.uuid)
            instanceSubmitScheduler.scheduleFormAutoSend(it.uuid)
        }
    }
}
