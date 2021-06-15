package com.retrofit.using.java.utilities;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;

public class ImplicitIntentUtils {

    private ImplicitIntentUtils() {
        throw new UnsupportedOperationException("Should not create instance of Util class. Please use as static..");
    }

    public static void actionGetContentIntent(Activity currentActivity, String mimeType, String extraMimeTypes, boolean showCloudStorage, int requestCode) {
        Intent intent = new Intent();
        intent.setType(mimeType);

        if (Build.VERSION.SDK_INT < 19)
        {
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent = Intent.createChooser(intent, "Select a file");
        }
        else
        {
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.putExtra(Intent.EXTRA_MIME_TYPES, extraMimeTypes);
            intent.putExtra(Intent.EXTRA_LOCAL_ONLY, !showCloudStorage);
        }
        currentActivity.startActivityForResult(intent, requestCode);
    }

    public static void actionPickIntent(int mediaType, Activity activity, int requestCode) {
        Intent intent = new Intent(Intent.ACTION_PICK);

        if (mediaType == 1)
        {
            intent.setType("image/*");

            if (Build.VERSION.SDK_INT < 19)
            {
                intent = Intent.createChooser(intent, "Select Image");
            }
            else
            {
                String[] mimeTypes = {"image/jpeg", "image/png"};
                intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
            }
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            activity.startActivityForResult(intent, requestCode);
        }
        else if (mediaType == 2)
        {
            intent.setType("video/*");

            if (Build.VERSION.SDK_INT < 19)
            {
                intent = Intent.createChooser(intent, "Select Video");
            }
            else
            {
                String[] mimeTypes = {"video/mp4"};
                intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
            }
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            activity.startActivityForResult(intent, requestCode);
        }
    }
}
