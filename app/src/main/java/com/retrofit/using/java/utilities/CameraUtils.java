package com.retrofit.using.java.utilities;

import android.app.Activity;
import android.content.pm.PackageManager;

public class CameraUtils {

    private CameraUtils() {
        throw new UnsupportedOperationException("You can't create instance of Util class. Please use as static..");
    }

    /**
     * Checking device has camera hardware or not
     */
    public static boolean isDeviceSupportCamera(Activity activity) {
        PackageManager packageManager = activity.getPackageManager();
        if (packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA) || packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT) || packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {
            return true;
        } else {
            return false;
        }
    }
}
