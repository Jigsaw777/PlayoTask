package com.example.playotesttask.managers;

import com.example.playotesttask.BuildConfig;
import com.example.playotesttask.remote.endpoints.SearchEndPoint;

import java.io.IOException;
import java.security.cert.CertificateException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RetrofitManager {
    private static final String BASE_URL = "https://hn.algolia.com/api/v1/";
    final OkHttpClient client = getHttpClient().build();
    private Retrofit retrofit = null;

    public RetrofitManager() {

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .client(client)
                    .build();
        }
    }

    private OkHttpClient.Builder getHttpClient() {

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.readTimeout(120, TimeUnit.SECONDS);
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();

                // Customize the request
                Request request = original.newBuilder()
                        .method(original.method(), original.body())
                        .build();
                Response response = chain.proceed(request);
                return response;
            }
        });

        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor mLogging = new HttpLoggingInterceptor();
            mLogging.setLevel(HttpLoggingInterceptor.Level.BODY);
            httpClient.addInterceptor(mLogging);
        }
        return httpClient;
    }

    public static OkHttpClient.Builder getUnsafeOkHttpClient() {

        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });

            if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor mLogging = new HttpLoggingInterceptor();
                mLogging.setLevel(HttpLoggingInterceptor.Level.BODY);
                builder.addInterceptor(mLogging);
            }

            return builder;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public SearchEndPoint getSearchResults(){
        return retrofit.create(SearchEndPoint.class);
    }
}
