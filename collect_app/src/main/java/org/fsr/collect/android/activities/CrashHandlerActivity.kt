package org.fsr.collect.android.activities

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import org.fsr.collect.android.mainmenu.MainMenuActivity
import org.fsr.collect.crashhandler.CrashHandler
import org.fsr.collect.strings.localization.LocalizedActivity

class CrashHandlerActivity : LocalizedActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val crashHandler = CrashHandler.getInstance(this)!!
        val crashView = crashHandler.getCrashView(this) {
            ActivityUtils.startActivityAndCloseAllOthers(this, MainMenuActivity::class.java)
        }

        if (crashView != null) {
            setContentView(crashView)
        } else {
            finish()
        }

        onBackPressedDispatcher.addCallback(
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    crashView?.dismiss()
                }
            }
        )
    }
}
