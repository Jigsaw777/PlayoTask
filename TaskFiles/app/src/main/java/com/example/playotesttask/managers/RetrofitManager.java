package com.example.playotesttask.managers;

import com.example.playotesttask.BuildConfig;
import com.example.playotesttask.remote.endpoints.SearchEndPoint;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

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

    public SearchEndPoint getSearchResults(){
        return retrofit.create(SearchEndPoint.class);
    }
}
