package com.retrofit.using.java.utilities.file;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.os.storage.StorageManager;
import android.util.Log;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MemoryUtils {

    public static final String TAG = MemoryUtils.class.getSimpleName();

    /**
     * Unit of memory start from byte, 1024 bytes = 1KB
     */
    public static final int BYTE = 1;

    /**
     * Multiple of KB
     *
     * e.g.,
     *          1024 * 1 = 1KB
     *          1024 * 10 = 10KB
     *          1024 * 9 = 9KB
     */
    public static final int KB = 1024;

    /**
     * Multiple of MB
     *
     * e.g.,
     *          1048576 * 1 = 1GB
     *          1048576 * 10 = 10GB
     *          1048576 * 9 = 9GB
     */
    public static final int MB = 1048576;

    /**
     * Multiple of GB
     *
     * e.g.,
     *          1073741824 * 1 = 1GB
     *          1073741824 * 10 = 10GB
     *          1073741824 * 9 = 9GB
     */
    public static final int GB = 1073741824;

    public enum MemoryUnit {
        BYTE,
        KB,
        MB,
        GB
    }

    /* Storage states */
    public static boolean externalStorageAvailable, externalStorageWriteable;

    private MemoryUtils() {
        throw new UnsupportedOperationException("You can't create instance of Util class. Please use as static..");
    }

    /**
     * Checks the external storage's state and saves it in member attributes.
     */
    public static boolean isSDCardPresent() {
        /* Get the external storage's state */
        String state = Environment.getExternalStorageState();

        if (state.equals(Environment.MEDIA_MOUNTED)) {
            /* Storage is available and writeable */
            externalStorageAvailable = externalStorageWriteable = true;
        } else if (state.equals(Environment.MEDIA_MOUNTED_READ_ONLY)) {
            /* Storage is only readable */
            externalStorageAvailable = true;
            externalStorageWriteable = false;
        } else {
            /* Storage is neither readable nor writeable */
            externalStorageAvailable = externalStorageWriteable = false;
        }
        return externalStorageWriteable;
    }

    /**
     * Checks the state of the external storage.
     *
     * @return True if the external storage is available, false otherwise.
     */
    public static boolean isExternalStorageAvailable() {
        isSDCardPresent();
        return externalStorageAvailable;
    }

    /**
     * Checks the state of the external storage.
     *
     * @return True if the external storage is writeable, false otherwise.
     */
    public static boolean isExternalStorageWriteable() {
        isSDCardPresent();
        return externalStorageWriteable;
    }

    /**
     * Checks the state of the external storage.
     *
     * @return True if the external storage is available and writeable, false
     * otherwise.
     */
    public static boolean isExternalStorageAvailableAndWriteable() {
        isSDCardPresent();
        if (!externalStorageAvailable) {
            return false;
        } else if (!externalStorageWriteable) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Get the file size in a human-readable string.
     *
     * @param size the size passed in
     * @return formatting unit returns the value after formatting
     */
    public static String getReadableFileSize(double size) {
        double kiloByte = size / 1024;
        if (kiloByte < 1) {
            return size + " Byte";
        }

        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + " KB";
        }

        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + " MB";
        }

        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + " GB";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString()
                + " TB";
    }

    /**
     * Get the amount of free space inside the phone
     */
    public static long getAvailableInternalMemorySize() {
        File path = Environment.getDataDirectory();// Get Android data directory
        StatFs stat = new StatFs(path.getPath());// A class that simulates the df command of Linux to get the usage of SD card and mobile phone memory
        long blockSize = stat.getBlockSize();// returns Int, the size, in bytes, a file system
        long availableBlocks = stat.getAvailableBlocks();// Return Int to get the currently available storage space
        return availableBlocks * blockSize;
    }

    /**
     * Get the size of the phone's internal space
     */
    public static long getTotalInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();// Get the number of file systems available in the area
        return totalBlocks * blockSize;
    }
    /**
     * Get the amount of free space outside the phone
     */
    public static long getAvailableExternalMemorySize() {
        if (isSDCardPresent()) {
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSize();
            long availableBlocks = stat.getAvailableBlocks();
            return availableBlocks * blockSize;
        } else {
            throw new RuntimeException("Don't have sdcard.");
        }
    }

    /**
     * Get the size of the phone's external space
     */
    public static long getTotalExternalMemorySize() {
        if (isSDCardPresent()) {
            File path = Environment.getExternalStorageDirectory();// Get the external storage directory i.e. SDCard
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSize();
            long totalBlocks = stat.getBlockCount();
            return totalBlocks * blockSize;
        } else {
            throw new RuntimeException("Don't have sdcard.");
        }
    }


    /**
     * Get SD card path
     *
     * @param removable true: external SD card false: internal SD card
     * @return SD card path
     *
     * Example :
     *              List<String> bothRemovableAndNonRemovableStringList = MediaStoreUtils.getSDCardPaths(this);
     *
     *              for(int i=0;i<bothRemovableAndNonRemovableStringList.size();i++){
     *                      System.out.println("==================BOTH REMOVABLE & NON REMOVABLE================= "+MediaStoreUtils.getSDCardPaths(this).get(i));
     *              }
     */
    @SuppressWarnings("TryWithIdenticalCatches")
    public static List<String> getSDCardPaths(Context context, final boolean removable) {
        List<String> paths = new ArrayList<>();
        StorageManager sm =
                (StorageManager) context.getSystemService(Context.STORAGE_SERVICE);
        try {
            Class<?> storageVolumeClazz = Class.forName("android.os.storage.StorageVolume");
            Method getVolumeList = StorageManager.class.getMethod("getVolumeList");
            Method getPath = storageVolumeClazz.getMethod("getPath");
            Method isRemovable = storageVolumeClazz.getMethod("isRemovable");
            Object result = getVolumeList.invoke(sm);
            final int length = Array.getLength(result);
            for (int i = 0; i < length; i++) {
                Object storageVolumeElement = Array.get(result, i);
                String path = (String) getPath.invoke(storageVolumeElement);
                boolean res = (Boolean) isRemovable.invoke(storageVolumeElement);
                if (removable == res) {
                    paths.add(path);
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return paths;
    }

    /**
     * Get SD card path
     *
     * @return SD card path
     *
     * Example :
     *              List<String> removableStringList = MediaStoreUtils.getSDCardPaths(this, true);
     *
     *              for(int i=0;i<removableStringList.size();i++){
     *                      System.out.println("==================REMOVABLE================= "+MediaStoreUtils.getSDCardPaths(this).get(i));
     *              }
     */
    @SuppressWarnings("TryWithIdenticalCatches")
    public static List<String> getSDCardPaths(Context context) {
        StorageManager storageManager = (StorageManager) context
                .getSystemService(Context.STORAGE_SERVICE);
        List<String> paths = new ArrayList<>();
        try {
            Method getVolumePathsMethod = StorageManager.class.getMethod("getVolumePaths");
            getVolumePathsMethod.setAccessible(true);
            Object invoke = getVolumePathsMethod.invoke(storageManager);
            paths = Arrays.asList((String[]) invoke);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return paths;
    }

    /**
     * Get SD card path list.
     */
    public static ArrayList<String> getSDCardPathEx() {
        ArrayList<String> list = new ArrayList<String>();
        try {
            Runtime runtime = Runtime.getRuntime();
            Process proc = runtime.exec("mount");
            InputStream is = proc.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            String line;
            BufferedReader br = new BufferedReader(isr);
            while ((line = br.readLine()) != null) {
                Log.i(TAG, "mount:  " + line);
                if (line.contains("secure")) {
                    continue;
                }
                if (line.contains("asec")) {
                    continue;
                }

                if (line.contains("fat")) {
                    String columns[] = line.split(" ");
                    if (columns.length > 1) {
                        list.add("*" + columns[1]);
                    }
                } else if (line.contains("fuse")) {
                    String columns[] = line.split(" ");
                    if (columns.length > 1) {
                        list.add(columns[1]);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }
}
