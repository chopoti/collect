package org.fsr.collect.android.openrosa;

import android.webkit.MimeTypeMap;

import org.fsr.collect.android.openrosa.okhttp.OkHttpConnection;
import org.fsr.collect.android.openrosa.okhttp.OkHttpOpenRosaServerClientProvider;

import okhttp3.OkHttpClient;

public class OkHttpConnectionGetRequestTest extends OpenRosaGetRequestTest {

    @Override
    protected OpenRosaHttpInterface buildSubject() {
        return new OkHttpConnection(
                new OkHttpOpenRosaServerClientProvider(new OkHttpClient()),
                new CollectThenSystemContentTypeMapper(MimeTypeMap.getSingleton()),
                USER_AGENT
        );
    }
}