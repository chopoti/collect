package org.fsr.collect.android.configure.qr

import android.content.Intent
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import org.fsr.collect.android.R
import org.fsr.collect.android.injection.DaggerUtils
import org.fsr.collect.android.projects.ProjectsDataService
import org.fsr.collect.android.utilities.FileProvider
import org.fsr.collect.androidshared.system.IntentLauncher
import org.fsr.collect.androidshared.ui.ListFragmentStateAdapter
import org.fsr.collect.androidshared.utils.AppBarUtils.setupAppBarLayout
import org.fsr.collect.async.Scheduler
import org.fsr.collect.permissions.PermissionListener
import org.fsr.collect.permissions.PermissionsProvider
import org.fsr.collect.qrcode.QRCodeDecoder
import org.fsr.collect.settings.ODKAppSettingsImporter
import org.fsr.collect.settings.SettingsProvider
import org.fsr.collect.strings.localization.LocalizedActivity
import javax.inject.Inject

class QRCodeTabsActivity : LocalizedActivity() {
    @Inject
    lateinit var qrCodeGenerator: QRCodeGenerator

    @Inject
    lateinit var intentLauncher: IntentLauncher

    @Inject
    lateinit var fileProvider: FileProvider

    @Inject
    lateinit var scheduler: Scheduler

    @Inject
    lateinit var qrCodeDecoder: QRCodeDecoder

    @Inject
    lateinit var settingsImporter: ODKAppSettingsImporter

    @Inject
    lateinit var appConfigurationGenerator: AppConfigurationGenerator

    @Inject
    lateinit var projectsDataService: ProjectsDataService

    @Inject
    lateinit var permissionsProvider: PermissionsProvider

    @Inject
    lateinit var settingsProvider: SettingsProvider

    private lateinit var activityResultDelegate: QRCodeActivityResultDelegate

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DaggerUtils.getComponent(this).inject(this)
        setContentView(R.layout.tabs_layout)
        setupAppBarLayout(this, getString(org.fsr.collect.strings.R.string.reconfigure_with_qr_code_settings_title))

        activityResultDelegate = QRCodeActivityResultDelegate(
            this,
            settingsImporter,
            qrCodeDecoder,
            projectsDataService.getCurrentProject()
        )

        val menuProvider = QRCodeMenuProvider(
            this,
            intentLauncher,
            qrCodeGenerator,
            appConfigurationGenerator,
            fileProvider,
            settingsProvider,
            scheduler
        )
        addMenuProvider(menuProvider, this)

        permissionsProvider.requestCameraPermission(
            this,
            object : PermissionListener {
                override fun granted() {
                    setupViewPager()
                }

                override fun additionalExplanationClosed() {
                    finish()
                }
            }
        )
    }

    private fun setupViewPager() {
        val fragmentTitleList = arrayOf(
            getString(org.fsr.collect.strings.R.string.scan_qr_code_fragment_title),
            getString(org.fsr.collect.strings.R.string.view_qr_code_fragment_title)
        )

        val viewPager = findViewById<ViewPager2>(R.id.viewPager)
        val tabLayout = findViewById<TabLayout>(R.id.tabLayout)
        viewPager.adapter = ListFragmentStateAdapter(
            this,
            listOf(QRCodeScannerFragment::class.java, ShowQRCodeFragment::class.java)
        )

        TabLayoutMediator(tabLayout, viewPager) { tab: TabLayout.Tab, position: Int ->
            tab.text = fragmentTitleList[position]
        }.attach()
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        activityResultDelegate.onActivityResult(requestCode, resultCode, data)
    }
}
