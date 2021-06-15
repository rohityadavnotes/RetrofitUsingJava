package com.retrofit.using.java.data.remote.glide;

import android.content.Context;
import android.os.Looper;
import android.text.TextUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.cache.ExternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import java.io.File;
import java.math.BigDecimal;

public class GlideCacheUtil {

    public static final String TAG = GlideCacheUtil.class.getSimpleName();

    private static GlideCacheUtil instance;

    public static GlideCacheUtil getInstance() {
        if (instance == null) {
            synchronized (GlideCacheUtil.class) {
                if (instance == null) {
                    instance = new GlideCacheUtil();
                }
            }
        }
        return instance;
    }

    /**
     * Clear image disk cache
     */
    public void clearDiskCache(final Context context){
        try {
            if (Looper.myLooper() == Looper.getMainLooper()){
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.get(context.getApplicationContext()).clearDiskCache();
                    }
                }).start();
            }else {
                Glide.get(context.getApplicationContext()).clearDiskCache();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Clear image memory cache
     */
    public void clearMemoryCache(Context context){
        try {
            if (Looper.myLooper() == Looper.getMainLooper()){
                Glide.get(context.getApplicationContext()).clearMemory();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Clear all caches of pictures
     */
    public void clearAllCache(Context context){
        clearDiskCache(context);
        clearMemoryCache(context);
        deleteFolderFile(context.getApplicationContext().getExternalCacheDir()+ ExternalCacheDiskCacheFactory.DEFAULT_DISK_CACHE_DIR,true);
    }

    /**
     * Get the image cache size caused by Glide
     * @return cacheSize
     */
    public String getCacheSize(Context context){
        try {
            return getReadableFileSize(getFolderSize(new File(context.getApplicationContext().getCacheDir()+"/"+ InternalCacheDiskCacheFactory.DEFAULT_DISK_CACHE_DIR)));
        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }

    /**
     * Get the sum of the size of all files in the specified folder
     * @param file file
     * @return file size
     * @throws Exception
     */
    public long getFolderSize(File file) throws Exception{
        long size=0;
        try {
            File[] fileList=file.listFiles();
            for (File aFileList : fileList){
                if (aFileList.isDirectory()){
                    size=size+getFolderSize(aFileList);
                }else {
                    size=size+aFileList.length();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return size;
    }

    /**
     * Delete files in the specified directory
     * @param filePath file directory
     * @param deleteThisPath
     */
    public void deleteFolderFile(String filePath, boolean deleteThisPath){
        if (!TextUtils.isEmpty(filePath)){
            try {
                File file=new File(filePath);
                if (file.isDirectory()){
                    File files[] = file.listFiles();
                    for (File file1 : files){
                        deleteFolderFile(file1.getAbsolutePath(),true);
                    }
                }
                if (deleteThisPath){
                    if (!file.isDirectory()){
                        file.delete();
                    }else {
                        if (file.listFiles().length == 0){
                            file.delete();
                        }
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
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
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + " KB";
        }

        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + " MB";
        }

        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + " GB";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + " TB";
    }
}

