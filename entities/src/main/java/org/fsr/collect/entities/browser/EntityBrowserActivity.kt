package org.fsr.collect.entities.browser

import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import org.fsr.collect.androidshared.ui.FragmentFactoryBuilder
import org.fsr.collect.async.Scheduler
import org.fsr.collect.entities.EntitiesDependencyComponentProvider
import org.fsr.collect.entities.R
import org.fsr.collect.entities.storage.EntitiesRepository
import org.fsr.collect.strings.localization.LocalizedActivity
import javax.inject.Inject

class EntityBrowserActivity : LocalizedActivity() {

    @Inject
    lateinit var scheduler: Scheduler

    @Inject
    lateinit var entitiesRepository: EntitiesRepository

    val viewModelFactory = viewModelFactory {
        addInitializer(EntitiesViewModel::class) {
            EntitiesViewModel(scheduler, entitiesRepository)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        supportFragmentManager.fragmentFactory = FragmentFactoryBuilder()
            .forClass(EntityListsFragment::class) { EntityListsFragment(viewModelFactory, ::getToolbar) }
            .forClass(EntitiesFragment::class) { EntitiesFragment(viewModelFactory) }
            .build()

        super.onCreate(savedInstanceState)
        (applicationContext as EntitiesDependencyComponentProvider)
            .entitiesDependencyComponent.inject(this)

        setContentView(R.layout.entities_layout)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment
        val navController = navHostFragment.navController

        val appBarConfiguration = AppBarConfiguration(navController.graph)
        getToolbar().setupWithNavController(navController, appBarConfiguration)
    }

    private fun getToolbar() = findViewById<Toolbar>(org.fsr.collect.androidshared.R.id.toolbar)
}
