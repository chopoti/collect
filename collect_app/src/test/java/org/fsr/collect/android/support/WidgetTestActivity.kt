package org.fsr.collect.android.support

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import org.fsr.collect.android.utilities.ScreenContext

class WidgetTestActivity : AppCompatActivity(), ScreenContext {
    @JvmField
    val viewsRegisterForContextMenu = mutableListOf<View>()

    override fun getActivity(): AppCompatActivity {
        return this
    }

    override fun getViewLifecycle(): LifecycleOwner {
        return this
    }

    override fun registerForContextMenu(view: View) {
        super.registerForContextMenu(view)
        viewsRegisterForContextMenu.add(view)
    }
}
