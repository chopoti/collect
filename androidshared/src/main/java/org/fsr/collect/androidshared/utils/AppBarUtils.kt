package org.fsr.collect.androidshared.utils

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

object AppBarUtils {

    @JvmStatic
    fun setupAppBarLayout(activity: Activity, title: CharSequence) {
        val toolbar = activity.findViewById<Toolbar>(org.fsr.collect.androidshared.R.id.toolbar)
        if (toolbar != null && activity is AppCompatActivity) {
            toolbar.title = title
            activity.setSupportActionBar(toolbar)
        }
    }
}
