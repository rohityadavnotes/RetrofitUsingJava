package com.retrofit.using.java.data.remote;

import android.content.Context;

public class ApiServiceGenerator {

    /**
     * Retrofit Service Generator class which initializes the calling ApiService
     *
     * @param context -  getApplicationContext.
     * @param serviceClass -  The Retrofit Service Interface class.
     */
    public static <S> S createService(Context context, Class<S> serviceClass) {
        return ApiClient.getClient(RemoteConfiguration.BASE_URL).create(serviceClass);
    }
}
