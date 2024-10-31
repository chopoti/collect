package org.fsr.collect.android.formlists.blankformlist

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.fsr.collect.android.R
import org.fsr.collect.android.activities.FormMapActivity
import org.fsr.collect.android.formmanagement.FormFillingIntentFactory
import org.fsr.collect.android.injection.DaggerUtils
import org.fsr.collect.android.preferences.dialogs.ServerAuthDialogFragment
import org.fsr.collect.androidshared.ui.DialogFragmentUtils
import org.fsr.collect.androidshared.ui.ObviousProgressBar
import org.fsr.collect.androidshared.ui.SnackbarUtils
import org.fsr.collect.async.network.NetworkStateProvider
import org.fsr.collect.lists.EmptyListView
import org.fsr.collect.lists.RecyclerViewUtils
import org.fsr.collect.permissions.PermissionListener
import org.fsr.collect.permissions.PermissionsProvider
import org.fsr.collect.strings.localization.LocalizedActivity
import javax.inject.Inject

class BlankFormListActivity : LocalizedActivity(), OnFormItemClickListener {

    @Inject
    lateinit var viewModelFactory: BlankFormListViewModel.Factory

    @Inject
    lateinit var networkStateProvider: NetworkStateProvider

    @Inject
    lateinit var permissionsProvider: PermissionsProvider

    private val viewModel: BlankFormListViewModel by viewModels { viewModelFactory }

    private val adapter: BlankFormListAdapter = BlankFormListAdapter(this)

    private val formLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            setResult(RESULT_OK, it.data)
            finish()
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DaggerUtils.getComponent(this).inject(this)
        setContentView(R.layout.activity_blank_form_list)
        title = getString(org.fsr.collect.strings.R.string.enter_data)
        setSupportActionBar(findViewById(org.fsr.collect.androidshared.R.id.toolbar))

        val menuProvider = BlankFormListMenuProvider(this, viewModel, networkStateProvider)
        addMenuProvider(menuProvider, this)

        val list = findViewById<RecyclerView>(R.id.form_list)
        list.layoutManager = LinearLayoutManager(this)
        list.addItemDecoration(RecyclerViewUtils.verticalLineDivider(this))
        list.adapter = adapter

        initObservers()
    }

    override fun onFormClick(formUri: Uri) {
        if (Intent.ACTION_PICK == intent.action) {
            // caller is waiting on a picked form
            setResult(RESULT_OK, Intent().setData(formUri))
            finish()
        } else {
            // caller wants to view/edit a form, so launch FormFillingActivity
            formLauncher.launch(FormFillingIntentFactory.newInstanceIntent(this, formUri))
        }
    }

    override fun onMapButtonClick(id: Long) {
        permissionsProvider.requestEnabledLocationPermissions(
            this,
            object : PermissionListener {
                override fun granted() {
                    startActivity(
                        Intent(this@BlankFormListActivity, FormMapActivity::class.java).also {
                            it.putExtra(FormMapActivity.EXTRA_FORM_ID, id)
                        }
                    )
                }
            }
        )
    }

    private fun initObservers() {
        viewModel.isLoading.observe(this) { isLoading ->
            if (isLoading) {
                findViewById<ObviousProgressBar>(org.fsr.collect.androidshared.R.id.progressBar).show()
            } else {
                findViewById<ObviousProgressBar>(org.fsr.collect.androidshared.R.id.progressBar).hide()
            }
        }

        viewModel.syncResult.observe(this) { result ->
            if (result != null) {
                SnackbarUtils.showShortSnackbar(findViewById(R.id.form_list), result)
            }
        }

        viewModel.formsToDisplay.observe(this) { forms ->
            findViewById<RecyclerView>(R.id.form_list).visibility =
                if (forms.isEmpty()) View.GONE else View.VISIBLE

            findViewById<EmptyListView>(R.id.empty_list_message).visibility =
                if (forms.isEmpty()) View.VISIBLE else View.GONE

            adapter.setData(forms)
        }

        viewModel.isAuthenticationRequired().observe(this) { authenticationRequired ->
            if (authenticationRequired) {
                DialogFragmentUtils.showIfNotShowing(
                    ServerAuthDialogFragment::class.java,
                    supportFragmentManager
                )
            } else {
                DialogFragmentUtils.dismissDialog(
                    ServerAuthDialogFragment::class.java,
                    supportFragmentManager
                )
            }
        }
    }
}
