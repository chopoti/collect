package org.fsr.collect.android.support;

import android.app.Application;
import android.content.Context;
import android.webkit.MimeTypeMap;

import androidx.work.WorkManager;

import org.fsr.collect.android.injection.config.AppDependencyModule;
import org.fsr.collect.android.openrosa.OpenRosaHttpInterface;
import org.fsr.collect.android.storage.StoragePathProvider;
import org.fsr.collect.android.version.VersionInformation;
import org.fsr.collect.android.views.BarcodeViewDecoder;
import org.fsr.collect.async.Scheduler;
import org.fsr.collect.async.network.NetworkStateProvider;
import org.fsr.collect.utilities.UserAgentProvider;

public class TestDependencies extends AppDependencyModule {

    public final StubOpenRosaServer server = new StubOpenRosaServer();
    public final FakeNetworkStateProvider networkStateProvider = new FakeNetworkStateProvider();
    public final TestScheduler scheduler = new TestScheduler(networkStateProvider);
    public final StoragePathProvider storagePathProvider = new StoragePathProvider();
    public final StubBarcodeViewDecoder stubBarcodeViewDecoder = new StubBarcodeViewDecoder();
    private final boolean useRealServer;

    public TestDependencies() {
        this(false);
    }

    public TestDependencies(boolean useRealServer) {
        this.useRealServer = useRealServer;
    }

    @Override
    public OpenRosaHttpInterface provideHttpInterface(MimeTypeMap mimeTypeMap, UserAgentProvider userAgentProvider, Application application, VersionInformation versionInformation) {
        if (useRealServer) {
            return super.provideHttpInterface(mimeTypeMap, userAgentProvider, application, versionInformation);
        } else {
            return server;
        }
    }

    @Override
    public Scheduler providesScheduler(WorkManager workManager) {
        return scheduler;
    }

    @Override
    public BarcodeViewDecoder providesBarcodeViewDecoder() {
        return stubBarcodeViewDecoder;
    }

    @Override
    public NetworkStateProvider providesNetworkStateProvider(Context context) {
        return networkStateProvider;
    }
}
