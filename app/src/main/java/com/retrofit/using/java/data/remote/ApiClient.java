package com.retrofit.using.java.data.remote;

import com.google.gson.GsonBuilder;
import com.ihsanbal.logging.Level;
import com.ihsanbal.logging.LoggingInterceptor;
import com.retrofit.using.java.BuildConfig;
import java.util.concurrent.TimeUnit;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import static android.util.Log.VERBOSE;

public class ApiClient {

    private static Retrofit retrofit = null;

    public static Retrofit getClient(String baseUrl) {
        if (retrofit==null) {
            retrofit = getRetrofitInstance(baseUrl);
        }
        return retrofit;
    }

    private static Retrofit getRetrofitInstance(String baseUrl) {
        OkHttpClient.Builder okHttpClientBuilder = getOkHttpClientBuilderInstance();
        OkHttpClient okHttpClient = okHttpClientBuilder.build();

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setLenient();

        Retrofit.Builder builder = new Retrofit.Builder();
        builder.baseUrl(baseUrl);
        builder.client(okHttpClient);
        builder.addConverterFactory(GsonConverterFactory.create(gsonBuilder.create()));
        builder.addCallAdapterFactory(RxJava2CallAdapterFactory.create());

        Retrofit retrofitInstance = builder.build();
        return retrofitInstance;
    }

    private static OkHttpClient.Builder getOkHttpClientBuilderInstance() {
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();

        okHttpClientBuilder.connectTimeout(RemoteConfiguration.HTTP_CONNECT_TIMEOUT, TimeUnit.MINUTES);
        okHttpClientBuilder.writeTimeout(RemoteConfiguration.HTTP_WRITE_TIMEOUT, TimeUnit.SECONDS);
        okHttpClientBuilder.readTimeout(RemoteConfiguration.HTTP_READ_TIMEOUT, TimeUnit.SECONDS);

        if (BuildConfig.DEBUG)
        {
            Interceptor interceptor = new LoggingInterceptor.Builder().setLevel(Level.BASIC).log(VERBOSE).build();
            okHttpClientBuilder.addInterceptor(interceptor);
        }

        return okHttpClientBuilder;
    }
}
