package org.fsr.collect.android.activities

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.fsr.collect.analytics.Analytics
import org.fsr.collect.android.analytics.AnalyticsEvents
import org.fsr.collect.android.databinding.FirstLaunchLayoutBinding
import org.fsr.collect.android.injection.DaggerUtils
import org.fsr.collect.android.mainmenu.MainMenuActivity
import org.fsr.collect.android.projects.ManualProjectCreatorDialog
import org.fsr.collect.android.projects.ProjectsDataService
import org.fsr.collect.android.projects.QrCodeProjectCreatorDialog
import org.fsr.collect.android.version.VersionInformation
import org.fsr.collect.androidshared.ui.DialogFragmentUtils
import org.fsr.collect.async.Scheduler
import org.fsr.collect.material.MaterialProgressDialogFragment
import org.fsr.collect.projects.Project
import org.fsr.collect.projects.ProjectsRepository
import org.fsr.collect.settings.SettingsProvider
import org.fsr.collect.strings.R
import org.fsr.collect.strings.localization.LocalizedActivity
import javax.inject.Inject

class FirstLaunchActivity : LocalizedActivity() {

    @Inject
    lateinit var projectsRepository: ProjectsRepository

    @Inject
    lateinit var versionInformation: VersionInformation

    @Inject
    lateinit var projectsDataService: ProjectsDataService

    @Inject
    lateinit var settingsProvider: SettingsProvider

    @Inject
    lateinit var scheduler: Scheduler

    private val viewModel: FirstLaunchViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return FirstLaunchViewModel(scheduler, projectsRepository, projectsDataService) as T
            }
        }
    }

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DaggerUtils.getComponent(this).inject(this)

        FirstLaunchLayoutBinding.inflate(layoutInflater).apply {
            setContentView(this.root)

            MaterialProgressDialogFragment.showOn(
                this@FirstLaunchActivity,
                viewModel.isLoading,
                supportFragmentManager
            ) {
                MaterialProgressDialogFragment().also { dialog ->
                    dialog.message = getString(org.fsr.collect.strings.R.string.loading)
                }
            }

            viewModel.isLoading.observe(this@FirstLaunchActivity) { isLoading ->
                if (!isLoading) {
                    ActivityUtils.startActivityAndCloseAllOthers(
                        this@FirstLaunchActivity,
                        MainMenuActivity::class.java
                    )
                }
            }

            configureViaQrButton.setOnClickListener {
                DialogFragmentUtils.showIfNotShowing(
                    QrCodeProjectCreatorDialog::class.java,
                    supportFragmentManager
                )
            }

            configureManuallyButton.setOnClickListener {
                DialogFragmentUtils.showIfNotShowing(
                    ManualProjectCreatorDialog::class.java,
                    supportFragmentManager
                )
            }

            appName.text = String.format(
                "%s %s",
                getString(R.string.collect_app_name),
                "v2"
            )

//            dontHaveServer.apply {
//                text = SpannableStringBuilder()
//                    .append(getString(org.fsr.collect.strings.R.string.dont_have_project))
//                    .append(" ")
//                    .color(getThemeAttributeValue(context, com.google.android.material.R.attr.colorAccent)) {
//                        append(getString(org.fsr.collect.strings.R.string.try_demo))
//                    }
//
//                setOnClickListener {
//                    viewModel.tryDemo()
//                }
//            }
        }
    }
}

private class FirstLaunchViewModel(
    private val scheduler: Scheduler,
    private val projectsRepository: ProjectsRepository,
    private val projectsDataService: ProjectsDataService
) : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun tryDemo() {
        Analytics.log(AnalyticsEvents.TRY_DEMO)

        _isLoading.value = true
        scheduler.immediate(
            background = {
                projectsRepository.save(Project.DEMO_PROJECT)
                projectsDataService.setCurrentProject(Project.DEMO_PROJECT_ID)
            },
            foreground = {
                _isLoading.value = false
            }
        )
    }
}
