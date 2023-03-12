package me.msile.app.androidapp.common.net.okhttp;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.internal.tls.OkHostnameVerifier;
import okhttp3.tls.HandshakeCertificates;


/**
 * okHttp client 管理类
 */

public enum OkHttpManager {

    INSTANCE;

    private final int MAX_TIME_OUT = 15;
    private OkHttpClient mDefHttpClient;
    private OkHttpClient mCacheHttpClient;

    /**
     * 获取默认App client
     */
    public OkHttpClient getAppHttpClient() {
        if (mDefHttpClient == null) {
            OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
            //log
//            httpClientBuilder.addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY));
            httpClientBuilder.connectTimeout(MAX_TIME_OUT, TimeUnit.SECONDS)
                    .readTimeout(MAX_TIME_OUT, TimeUnit.SECONDS)
                    .writeTimeout(MAX_TIME_OUT, TimeUnit.SECONDS)
                    .retryOnConnectionFailure(true);
            HandshakeCertificates clientCertificates = new HandshakeCertificates.Builder()
                    .addPlatformTrustedCertificates()
                    .build();
            httpClientBuilder.sslSocketFactory(clientCertificates.sslSocketFactory(), clientCertificates.trustManager());
            httpClientBuilder.hostnameVerifier(OkHostnameVerifier.INSTANCE);
            mDefHttpClient = httpClientBuilder.build();
        }
        return mDefHttpClient;
    }

    public OkHttpClient getCacheHttpClient() {
        if (mCacheHttpClient == null) {
            mCacheHttpClient = createNewOkHttpClient(MAX_TIME_OUT, true);
        }
        return mCacheHttpClient;
    }

    public void clearCacheHttpClient() {
        mCacheHttpClient = null;
    }

    /**
     * 创建新的ok http client
     */
    public OkHttpClient createNewOkHttpClient(int timeOut, boolean retry) {
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        httpClientBuilder.connectTimeout(timeOut, TimeUnit.SECONDS)
                .readTimeout(timeOut, TimeUnit.SECONDS)
                .writeTimeout(timeOut, TimeUnit.SECONDS)
                .retryOnConnectionFailure(retry);
        return httpClientBuilder.build();
    }

}