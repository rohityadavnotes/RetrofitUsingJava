package com.retrofit.using.java.utilities.file;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;
import androidx.annotation.NonNull;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MediaFileUtils {

    private MediaFileUtils() {
        throw new UnsupportedOperationException("You can't create instance of Util class. Please use as static..");
    }

    /**
     * Generate random file name
     *
     * @return random file name
     */
    public static String getRandomFileName(int mediaType) {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String fileName = timeStamp;

        if (mediaType == 1) {
            fileName = "IMG_" + timeStamp;
        }

        if (mediaType == 2) {
            fileName = "VID_" + timeStamp;
        }

        return fileName;
    }

    /**
     * Create directory
     *
     * @param context
     * @return directory path
     */
    public static File getMediaDir(@NonNull Context context, int mediaType, String customDirectoryName) {

        //File mediaStorageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        //File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Camera Example");

        File extStorageDirectory = Environment.getExternalStorageDirectory();
        String rootDirectory = extStorageDirectory + "/" + customDirectoryName + "/";

        File mediaStorageDir = new File(rootDirectory);

        if (mediaType == 1) {
            String picturesPath = extStorageDirectory + "Pictures/";
            mediaStorageDir = new File(picturesPath);
        }

        if (mediaType == 2) {
            String videosPath = extStorageDirectory + "Videos/";
            mediaStorageDir = new File(videosPath);
        }

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Toast.makeText(context, "Oops! Failed to create directory", Toast.LENGTH_LONG).show();
                return null;
            }
        }

        return mediaStorageDir;
    }

    /**
     * Create file
     *
     * @param context
     * @param mediaType
     * @param customDirectoryName
     * @param extension
     * @param context
     * @return file
     * @throws IOException
     */
    public static File createFile(@NonNull Context context, int mediaType, String customDirectoryName, String extension) throws IOException {
        File mediaStorageDir = getMediaDir(context, mediaType, customDirectoryName);
        String fileName = getRandomFileName(mediaType);

        File imageTempFile = File.createTempFile(
                fileName,  /* prefix */
                extension,  /* suffix */
                mediaStorageDir     /* directory */
        );

        return imageTempFile;
    }


    public static void deleteUriFile(String path) {
        File file = new File(path);
        if (file.exists()) {
            if (file.delete()) {
                System.out.println("file Deleted :" + path);
            } else {
                System.out.println("file not Deleted :" + path);
            }
        }
    }
}
