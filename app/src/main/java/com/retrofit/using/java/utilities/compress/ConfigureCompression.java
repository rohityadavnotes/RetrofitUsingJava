package com.retrofit.using.java.utilities.compress;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.Toast;
import java.io.File;

public class ConfigureCompression {

    private static volatile ConfigureCompression instance;
    private Context context;

    /**
     * The maximum width, the default is 720
     */
    private float maxWidth = 720.0f;

    /**
     * The maximum height, the default is 960
     */
    private float maxHeight = 960.0f;

    /**
     * The default compression method is JPEG, which is lossy compression.
     * PNG loss less image format, when calling bitmap.compress(CompressFormat format, int quality, OutputStream stream)
     * If CompressFormat.PNG is passed in, the picture will not be compressed, on the contrary, the saved picture may be larger than the original picture
     * CompressFormat.WEBP is the image format recommended by Google. If this format is passed in, the image compression ratio will be greater, that is, the compressed image will be much smaller than the original image.
     * For higher requirements, this method is recommended, because the compressed image is small and not very blurry
     */
    private Bitmap.CompressFormat compressFormat = Bitmap.CompressFormat.JPEG;

    /**
     * The default image processing method is ARGB_8888, which is a 32-bit image, and one pixel occupies 4 bytes
     * If a 2560 x 1440 image is loaded into the memory using the ARGB_8888 format (default), the occupied memory is
     * 2560 x 1440 x 4 = 14745600 (bytes) = 14.0625 MB so the big picture is loaded into the memory and easy to oom
     */
    private Bitmap.Config bitmapConfig = Bitmap.Config.ARGB_8888;

    /**
     * The default compression quality is 80, and the compression quality is between 70-80. The quality compression effect is more obvious, and the lower the image size is not obvious, but the image becomes blurred
     */
    private int quality = 80;

    /**
     * Storage path
     */
    private String destinationPathOfCompressedFile;

    /**
     * File name prefix
     */
    private String fileNamePrefix;

    /**
     * file name
     */
    private String fileName;

    private ConfigureCompression(Context context) {
        this.context = context;

        /**
         * Default destination
         */
        File externalFilesDir = context.getExternalFilesDir("Compress");

        assert externalFilesDir != null;
        if (!externalFilesDir.exists()) {
            if (!externalFilesDir.mkdirs()) {
                Toast.makeText(context, "Oops! Failed to create directory", Toast.LENGTH_LONG).show();
                return;
            }
        }

        this.destinationPathOfCompressedFile = externalFilesDir.getAbsolutePath();
    }

    public static ConfigureCompression getInstance(Context context) {
        if (instance == null) {
            synchronized (ConfigureCompression.class) {
                if (instance == null) {
                    instance = new ConfigureCompression(context);
                }
            }
        }
        return instance;
    }

    /**
     * Adopt builder mode, set up Builder
     */
    public static class Builder {
        private ConfigureCompression configureCompression;

        public Builder(Context context) {
            configureCompression = new ConfigureCompression(context);
        }

        /**
         * Set the maximum width of the picture
         * @param maxWidth maximum width
         */
        public Builder setMaxWidth(float maxWidth) {
            configureCompression.maxWidth = maxWidth;
            return this;
        }

        /**
         * Set the maximum height of the picture
         * @param maxHeight maximum height
         */
        public Builder setMaxHeight(float maxHeight) {
            configureCompression.maxHeight = maxHeight;
            return this;
        }

        /**
         * Set the compressed suffix format
         */
        public Builder setCompressFormat(Bitmap.CompressFormat compressFormat) {
            configureCompression.compressFormat = compressFormat;
            return this;
        }

        /**
         * Set the parameters of Bitmap
         */
        public Builder setBitmapConfig(Bitmap.Config bitmapConfig) {
            configureCompression.bitmapConfig = bitmapConfig;
            return this;
        }

        /**
         * Set the compression quality, 80 is recommended
         * @param quality Compression quality, [0,100]
         */
        public Builder setQuality(int quality) {
            configureCompression.quality = quality;
            return this;
        }

        /**
         * Set the destination storage path
         * @param destinationDirectoryPath destination path
         */
        public Builder setDestinationDirectoryPath(String destinationDirectoryPath) {
            configureCompression.destinationPathOfCompressedFile = destinationDirectoryPath;
            return this;
        }

        /**
         * Set file prefix
         * For example, if file name is sample.jpg then add prefix is "compress_", then the name of the file saved will be "compress_sample.jpg".
         *
         * @param prefix prefix
         */
        public Builder setFileNamePrefix(String prefix) {
            configureCompression.fileNamePrefix = prefix;
            return this;
        }

        /**
         * Set file name
         * @param fileName file name
         */
        public Builder setFileName(String fileName) {
            configureCompression.fileName = fileName;
            return this;
        }

        public ConfigureCompression build() {
            return configureCompression;
        }
    }

    /**
     * Compressed into file
     * @param file original file
     * @return compressed file
     */
    public File compressToFile(File file) {
        return CompressUtils.compressImage(context,
                Uri.fromFile(file),
                maxWidth,
                maxHeight,
                compressFormat,
                bitmapConfig,
                quality,
                destinationPathOfCompressedFile,
                fileNamePrefix,
                fileName);
    }

    /**
     * Compressed as Bitmap
     * @param file original file
     * @return compressed Bitmap
     */
    public Bitmap compressToBitmap(File file) {
        return CompressUtils.getScaledBitmap(context,
                Uri.fromFile(file),
                maxWidth,
                maxHeight,
                bitmapConfig);
    }
}
