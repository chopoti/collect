package org.fsr.collect.android.injection.config;

import android.app.Application;

import org.javarosa.core.reference.ReferenceManager;
import org.fsr.collect.android.activities.AboutActivity;
import org.fsr.collect.android.activities.AppListActivity;
import org.fsr.collect.android.activities.DeleteFormsActivity;
import org.fsr.collect.android.activities.FirstLaunchActivity;
import org.fsr.collect.android.activities.FormDownloadListActivity;
import org.fsr.collect.android.activities.FormFillingActivity;
import org.fsr.collect.android.activities.FormMapActivity;
import org.fsr.collect.android.activities.InstanceChooserList;
import org.fsr.collect.android.application.Collect;
import org.fsr.collect.android.application.initialization.ApplicationInitializer;
import org.fsr.collect.android.application.initialization.ExistingProjectMigrator;
import org.fsr.collect.android.audio.AudioRecordingControllerFragment;
import org.fsr.collect.android.audio.AudioRecordingErrorDialogFragment;
import org.fsr.collect.android.backgroundwork.AutoUpdateTaskSpec;
import org.fsr.collect.android.backgroundwork.SendFormsTaskSpec;
import org.fsr.collect.android.backgroundwork.SyncFormsTaskSpec;
import org.fsr.collect.android.configure.qr.QRCodeScannerFragment;
import org.fsr.collect.android.configure.qr.QRCodeTabsActivity;
import org.fsr.collect.android.configure.qr.ShowQRCodeFragment;
import org.fsr.collect.android.entities.EntitiesRepositoryProvider;
import org.fsr.collect.android.external.AndroidShortcutsActivity;
import org.fsr.collect.android.external.FormUriActivity;
import org.fsr.collect.android.external.FormsProvider;
import org.fsr.collect.android.external.InstanceProvider;
import org.fsr.collect.android.formentry.BackgroundAudioPermissionDialogFragment;
import org.fsr.collect.android.formentry.ODKView;
import org.fsr.collect.android.formentry.repeats.DeleteRepeatDialogFragment;
import org.fsr.collect.android.formentry.saving.SaveAnswerFileErrorDialogFragment;
import org.fsr.collect.android.formentry.saving.SaveFormProgressDialogFragment;
import org.fsr.collect.android.formhierarchy.FormHierarchyActivity;
import org.fsr.collect.android.formlists.blankformlist.BlankFormListActivity;
import org.fsr.collect.android.formmanagement.FormSourceProvider;
import org.fsr.collect.android.formmanagement.FormsDataService;
import org.fsr.collect.android.fragments.BarCodeScannerFragment;
import org.fsr.collect.android.fragments.dialogs.FormsDownloadResultDialog;
import org.fsr.collect.android.fragments.dialogs.SelectMinimalDialog;
import org.fsr.collect.android.instancemanagement.send.InstanceUploaderActivity;
import org.fsr.collect.android.instancemanagement.send.InstanceUploaderListActivity;
import org.fsr.collect.android.mainmenu.MainMenuActivity;
import org.fsr.collect.android.openrosa.OpenRosaHttpInterface;
import org.fsr.collect.android.preferences.dialogs.AdminPasswordDialogFragment;
import org.fsr.collect.android.preferences.dialogs.ChangeAdminPasswordDialog;
import org.fsr.collect.android.preferences.dialogs.ResetDialogPreferenceFragmentCompat;
import org.fsr.collect.android.preferences.dialogs.ServerAuthDialogFragment;
import org.fsr.collect.android.preferences.screens.BasePreferencesFragment;
import org.fsr.collect.android.preferences.screens.BaseProjectPreferencesFragment;
import org.fsr.collect.android.preferences.screens.ExperimentalPreferencesFragment;
import org.fsr.collect.android.preferences.screens.FormManagementPreferencesFragment;
import org.fsr.collect.android.preferences.screens.FormMetadataPreferencesFragment;
import org.fsr.collect.android.preferences.screens.IdentityPreferencesFragment;
import org.fsr.collect.android.preferences.screens.MapsPreferencesFragment;
import org.fsr.collect.android.preferences.screens.ProjectDisplayPreferencesFragment;
import org.fsr.collect.android.preferences.screens.ProjectManagementPreferencesFragment;
import org.fsr.collect.android.preferences.screens.ProjectPreferencesActivity;
import org.fsr.collect.android.preferences.screens.ProjectPreferencesFragment;
import org.fsr.collect.android.preferences.screens.ServerPreferencesFragment;
import org.fsr.collect.android.preferences.screens.UserInterfacePreferencesFragment;
import org.fsr.collect.android.projects.ManualProjectCreatorDialog;
import org.fsr.collect.android.projects.ProjectResetter;
import org.fsr.collect.android.projects.ProjectSettingsDialog;
import org.fsr.collect.android.projects.ProjectsDataService;
import org.fsr.collect.android.projects.QrCodeProjectCreatorDialog;
import org.fsr.collect.android.storage.StoragePathProvider;
import org.fsr.collect.android.tasks.DownloadFormListTask;
import org.fsr.collect.android.tasks.InstanceUploaderTask;
import org.fsr.collect.android.tasks.MediaLoadingTask;
import org.fsr.collect.android.utilities.AuthDialogUtility;
import org.fsr.collect.android.utilities.FormsRepositoryProvider;
import org.fsr.collect.android.utilities.InstancesRepositoryProvider;
import org.fsr.collect.android.utilities.SavepointsRepositoryProvider;
import org.fsr.collect.android.utilities.ThemeUtils;
import org.fsr.collect.android.widgets.QuestionWidget;
import org.fsr.collect.android.widgets.items.SelectOneFromMapDialogFragment;
import org.fsr.collect.async.Scheduler;
import org.fsr.collect.async.network.NetworkStateProvider;
import org.fsr.collect.draw.DrawActivity;
import org.fsr.collect.googlemaps.GoogleMapFragment;
import org.fsr.collect.location.LocationClient;
import org.fsr.collect.maps.MapFragmentFactory;
import org.fsr.collect.maps.layers.ReferenceLayerRepository;
import org.fsr.collect.permissions.PermissionsChecker;
import org.fsr.collect.permissions.PermissionsProvider;
import org.fsr.collect.projects.ProjectsRepository;
import org.fsr.collect.settings.ODKAppSettingsImporter;
import org.fsr.collect.settings.SettingsProvider;
import org.fsr.collect.webpage.ExternalWebPageHelper;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;

/**
 * Dagger component for the application. Should include
 * application level Dagger Modules and be built with Application
 * object.
 * <p>
 * Add an `inject(MyClass myClass)` method here for objects you want
 * to inject into so Dagger knows to wire it up.
 * <p>
 * Annotated with @Singleton so modules can include @Singletons that will
 * be retained at an application level (as this an instance of this components
 * is owned by the Application object).
 * <p>
 * If you need to call a provider directly from the component (in a test
 * for example) you can add a method with the type you are looking to fetch
 * (`MyType myType()`) to this interface.
 * <p>
 * To read more about Dagger visit: https://google.github.io/dagger/users-guide
 **/

@Singleton
@Component(modules = {
        AppDependencyModule.class
})
public interface AppDependencyComponent {

    @Component.Builder
    interface Builder {

        @BindsInstance
        Builder application(Application application);

        Builder appDependencyModule(AppDependencyModule testDependencyModule);

        AppDependencyComponent build();
    }

    void inject(Collect collect);

    void inject(AboutActivity aboutActivity);

    void inject(FormFillingActivity formFillingActivity);

    void inject(InstanceUploaderTask uploader);

    void inject(ServerPreferencesFragment serverPreferencesFragment);

    void inject(ProjectDisplayPreferencesFragment projectDisplayPreferencesFragment);

    void inject(ProjectManagementPreferencesFragment projectManagementPreferencesFragment);

    void inject(AuthDialogUtility authDialogUtility);

    void inject(FormDownloadListActivity formDownloadListActivity);

    void inject(InstanceUploaderListActivity activity);

    void inject(QuestionWidget questionWidget);

    void inject(ODKView odkView);

    void inject(FormMetadataPreferencesFragment formMetadataPreferencesFragment);

    void inject(FormMapActivity formMapActivity);

    void inject(GoogleMapFragment mapFragment);

    void inject(MainMenuActivity mainMenuActivity);

    void inject(QRCodeTabsActivity qrCodeTabsActivity);

    void inject(ShowQRCodeFragment showQRCodeFragment);

    void inject(SendFormsTaskSpec sendFormsTaskSpec);

    void inject(AdminPasswordDialogFragment adminPasswordDialogFragment);

    void inject(FormHierarchyActivity formHierarchyActivity);

    void inject(FormManagementPreferencesFragment formManagementPreferencesFragment);

    void inject(IdentityPreferencesFragment identityPreferencesFragment);

    void inject(UserInterfacePreferencesFragment userInterfacePreferencesFragment);

    void inject(SaveFormProgressDialogFragment saveFormProgressDialogFragment);

    void inject(BarCodeScannerFragment barCodeScannerFragment);

    void inject(QRCodeScannerFragment qrCodeScannerFragment);

    void inject(ProjectPreferencesActivity projectPreferencesActivity);

    void inject(ResetDialogPreferenceFragmentCompat resetDialogPreferenceFragmentCompat);

    void inject(SyncFormsTaskSpec syncWork);

    void inject(ExperimentalPreferencesFragment experimentalPreferencesFragment);

    void inject(AutoUpdateTaskSpec autoUpdateTaskSpec);

    void inject(ServerAuthDialogFragment serverAuthDialogFragment);

    void inject(BasePreferencesFragment basePreferencesFragment);

    void inject(InstanceUploaderActivity instanceUploaderActivity);

    void inject(ProjectPreferencesFragment projectPreferencesFragment);

    void inject(DeleteFormsActivity deleteFormsActivity);

    void inject(SelectMinimalDialog selectMinimalDialog);

    void inject(AudioRecordingControllerFragment audioRecordingControllerFragment);

    void inject(SaveAnswerFileErrorDialogFragment saveAnswerFileErrorDialogFragment);

    void inject(AudioRecordingErrorDialogFragment audioRecordingErrorDialogFragment);

    void inject(InstanceChooserList instanceChooserList);

    void inject(FormsProvider formsProvider);

    void inject(InstanceProvider instanceProvider);

    void inject(BackgroundAudioPermissionDialogFragment backgroundAudioPermissionDialogFragment);

    void inject(ChangeAdminPasswordDialog changeAdminPasswordDialog);

    void inject(MediaLoadingTask mediaLoadingTask);

    void inject(ThemeUtils themeUtils);

    void inject(BaseProjectPreferencesFragment baseProjectPreferencesFragment);

    void inject(AndroidShortcutsActivity androidShortcutsActivity);

    void inject(ProjectSettingsDialog projectSettingsDialog);

    void inject(ManualProjectCreatorDialog manualProjectCreatorDialog);

    void inject(QrCodeProjectCreatorDialog qrCodeProjectCreatorDialog);

    void inject(FirstLaunchActivity firstLaunchActivity);

    void inject(FormUriActivity formUriActivity);

    void inject(MapsPreferencesFragment mapsPreferencesFragment);

    void inject(FormsDownloadResultDialog formsDownloadResultDialog);

    void inject(SelectOneFromMapDialogFragment selectOneFromMapDialogFragment);

    void inject(DrawActivity drawActivity);

    void inject(BlankFormListActivity blankFormListActivity);

    void inject(DeleteRepeatDialogFragment deleteRepeatDialogFragment);

    void inject(AppListActivity appListActivity);

    void inject(DownloadFormListTask downloadFormListTask);

    OpenRosaHttpInterface openRosaHttpInterface();

    ReferenceManager referenceManager();

    SettingsProvider settingsProvider();

    ApplicationInitializer applicationInitializer();

    ODKAppSettingsImporter settingsImporter();

    ProjectsRepository projectsRepository();

    ProjectsDataService currentProjectProvider();

    StoragePathProvider storagePathProvider();

    FormsRepositoryProvider formsRepositoryProvider();

    InstancesRepositoryProvider instancesRepositoryProvider();

    SavepointsRepositoryProvider savepointsRepositoryProvider();

    FormSourceProvider formSourceProvider();

    ExistingProjectMigrator existingProjectMigrator();

    ProjectResetter projectResetter();

    MapFragmentFactory mapFragmentFactory();

    Scheduler scheduler();

    LocationClient locationClient();

    PermissionsProvider permissionsProvider();

    PermissionsChecker permissionsChecker();

    ReferenceLayerRepository referenceLayerRepository();

    NetworkStateProvider networkStateProvider();

    EntitiesRepositoryProvider entitiesRepositoryProvider();

    FormsDataService formsDataService();

    ProjectDependencyModuleFactory projectDependencyModuleFactory();

    ExternalWebPageHelper externalWebPageHelper();
}