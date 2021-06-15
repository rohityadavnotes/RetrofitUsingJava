package com.retrofit.using.java.utilities.file;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import androidx.core.content.FileProvider;
import java.io.File;

public class FileProviderUtils {

    private FileProviderUtils() {
        throw new UnsupportedOperationException("You can't create instance of Util class. Please use as static..");
    }

    /**
     * Get File Uri
     *
     * @param context context
     * @param file file
     * @param packageName BuildConfig.APPLICATION_ID ("com.mvp.using.java")
     */
    public static Uri getFileUri(Context context, File file, String packageName) {
        /**
         * Note: We are using getUriForFile(Context, String, File) which returns a content:// URI.
         * For more recent apps targeting Android 7.0 (API level 24) and higher, passing a file:// URI
         * across a package boundary causes a FileUriExposedException.
         * Therefore, we now present a more generic way of storing images using a FileProvider.
         */
        Uri fileUri;
        if (Build.VERSION.SDK_INT >= 24) {
            fileUri = getUriForFile24(context, packageName, file);
        } else {
            fileUri = Uri.fromFile(file);
        }
        return fileUri;
    }

    public static Uri getUriForFile24(Context context, String packageName, File file) {
        return FileProvider.getUriForFile(context, packageName + ".fileProvider", file);
    }
}
