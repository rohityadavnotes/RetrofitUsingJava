package com.retrofit.using.java.utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkConnectivityUtils {

    private NetworkConnectivityUtils() {
        throw new UnsupportedOperationException("Should not create instance of NetworkConnectivityUtil class. Please use as static..");
    }

    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

            if (networkInfo != null) {
                return networkInfo.isAvailable();
            }
        }
        return false;
    }
}
