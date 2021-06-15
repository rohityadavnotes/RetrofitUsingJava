package com.retrofit.using.java.utilities.compress;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.text.TextUtils;
import androidx.exifinterface.media.ExifInterface;
import com.retrofit.using.java.utilities.file.RealPathUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class CompressUtils {

    private CompressUtils() {
        throw new UnsupportedOperationException("You can't create instance of Util class. Please use as static..");
    }

    /**
     * Calculate inSampleSize. When generating Bitmap, inSampleSize is the most influential parameter. When calling Bitmap bitmap = BitmapFactory.decodeFile(String pathName, Options opts)
     * opts If you pass in null, if a 2560 x 1440 image is loaded into the memory using the ARGB_8888 format (default), the occupied memory is 2560 x 1440 x 4 = 14745600 (bytes) = 14.0625 MB,
     * The image size is too large. If opts is not limited, it is easy to oom. If inSampleSize = 2, then the width and height of the generated bitmap are 1/2 of the original, and the pixels occupied by the image are 1/4 of the original.
     * The picture above only takes up 2560 x 1440 x 4/4 = 3686400 (bytes) = 3.515625 MB when loaded into memory, which greatly reduces the occupied memory.
     * inSampleSize can only be the n-th power of 2, if inSampleSize = 7, then the system will round down to 4, inSampleSize can only be at least 1, and less than 1 will be treated as 1
     *
     * @param options options
     * @param reqWidth set image width
     * @param reqHeight set the image height
     * @return inSampleSize compression parameters
     */
    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth)
        {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;

        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap)
        {
            inSampleSize++;
        }

        return inSampleSize;
    }

    public static Bitmap getScaledBitmap(Context context,
                                         Uri imageUri,
                                         float maxWidth,
                                         float maxHeight,
                                         Bitmap.Config bitmapConfig) {

        String realPath     = RealPathUtils.getRealPath(context, imageUri);
        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();

        /*
         * By setting this field as true, the actual bitmap pixels are not loaded in the memory.
         * Just the bounds are loaded. If you try the use the bitmap here, you will get null.
         */
        options.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(realPath, options);
        if (bitmap == null)
        {
            InputStream inputStream = null;
            try
            {
                inputStream = new FileInputStream(realPath);
                BitmapFactory.decodeStream(inputStream, null, options);
                inputStream.close();
            }
            catch(FileNotFoundException fileNotFoundException)
            {
                fileNotFoundException.printStackTrace();
            }
            catch(IOException iOException) {
                iOException.printStackTrace();
            }
        }

        int actualHeight    = options.outHeight;
        int actualWidth     = options.outWidth;

        if (actualHeight == -1 || actualWidth == -1)
        {
            try
            {
                ExifInterface exifInterface = new ExifInterface(realPath);

                actualHeight    = exifInterface.getAttributeInt(ExifInterface.TAG_IMAGE_LENGTH, ExifInterface.ORIENTATION_NORMAL); /* Get the height of the picture */
                actualWidth     = exifInterface.getAttributeInt(ExifInterface.TAG_IMAGE_WIDTH, ExifInterface.ORIENTATION_NORMAL); /* Get the width of the picture */
            }
            catch (IOException iOException) {
                iOException.printStackTrace();
            }
        }

        if (actualWidth <= 0 || actualHeight <= 0)
        {
            Bitmap bitmap2 = BitmapFactory.decodeFile(realPath);

            if (bitmap2 != null)
            {
                actualWidth = bitmap2.getWidth();
                actualHeight = bitmap2.getHeight();
            }
            else
            {
                return null;
            }
        }

        float imgRatio = (float) actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

        /* width and height values are set maintaining the aspect ratio of the image */
        if (actualHeight > maxHeight || actualWidth > maxWidth)
        {
            if (imgRatio < maxRatio)
            {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            }
            else if (imgRatio > maxRatio)
            {
                imgRatio = maxWidth / actualWidth;

                actualHeight    = (int) (imgRatio * actualHeight);
                actualWidth     = (int) maxWidth;
            }
            else
            {
                actualHeight    = (int) maxHeight;
                actualWidth     = (int) maxWidth;
            }
        }

        /* setting inSampleSize value allows to load a scaled down version of the original image */
        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);

        /* inJustDecodeBounds set to false to load the actual bitmap */
        options.inJustDecodeBounds = false;

        /* this options allow android to claim the bitmap memory if it runs low on memory */
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try
        {
            /* load the bitmap getTempFile its path */
            bitmap = BitmapFactory.decodeFile(realPath, options);

            if (bitmap == null)
            {
                InputStream inputStream = null;

                try
                {
                    inputStream = new FileInputStream(realPath);
                    BitmapFactory.decodeStream(inputStream, null, options);
                    inputStream.close();
                }
                catch (IOException iOException)
                {
                    iOException.printStackTrace();
                }
            }
        }
        catch (OutOfMemoryError outOfMemoryError)
        {
            outOfMemoryError.printStackTrace();
        }

        if (actualHeight <= 0 || actualWidth <= 0){
            return null;
        }

        try
        {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, bitmapConfig);
        }
        catch (OutOfMemoryError outOfMemoryError)
        {
            outOfMemoryError.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, 0, 0);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bitmap, 0, 0, new Paint(Paint.FILTER_BITMAP_FLAG));

        /* Use ExitInterface to set the image rotation direction, check the rotation of the image and display it properly */
        ExifInterface exifInterface;
        try
        {
            exifInterface   = new ExifInterface(realPath);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0);
            Matrix matrix   = new Matrix();

            if (orientation == 6)
            {
                matrix.postRotate(90);
            }
            else if (orientation == 3)
            {
                matrix.postRotate(180);
            }
            else if (orientation == 8)
            {
                matrix.postRotate(270);
            }

            scaledBitmap = Bitmap.createBitmap(
                    scaledBitmap,
                    0,
                    0,
                    scaledBitmap.getWidth(),
                    scaledBitmap.getHeight(),
                    matrix,
                    true
            );
        }
        catch (IOException iOException)
        {
            iOException.printStackTrace();
        }

        return scaledBitmap;
    }

    public static File compressImage(Context context,
                              Uri imageUri,
                              float maxWidth,
                              float maxHeight,
                              Bitmap.CompressFormat compressFormat,
                              Bitmap.Config bitmapConfig,
                              int quality,
                              String parentPath,
                              String prefix,
                              String fileName) {

        FileOutputStream fileOutputStream = null;
        String filename = generateFilePath(context, parentPath, imageUri, compressFormat.name().toLowerCase(), prefix, fileName);
        try
        {
            fileOutputStream = new FileOutputStream(filename);

            /* write the compressed bitmap at the destination specified by filename. */
            Bitmap newBitmap = getScaledBitmap(context, imageUri, maxWidth, maxHeight, bitmapConfig);

            if (newBitmap != null)
            {
                newBitmap.compress(compressFormat, quality, fileOutputStream);
            }
        }
        catch(FileNotFoundException fileNotFoundException)
        {
            fileNotFoundException.printStackTrace();
        }
        finally
        {
            try
            {
                if (fileOutputStream != null)
                {
                    fileOutputStream.close();
                }
            }
            catch (IOException iOException)
            {
                iOException.printStackTrace();
            }
        }
        return new File(filename);
    }

    private static String generateFilePath(Context context,
                                           String parentPath,
                                           Uri uri,
                                           String extension,
                                           String prefix,
                                           String fileName) {

        File file = new File(parentPath);
        if (!file.exists())
        {
            file.mkdirs();
        }

        /** if prefix is null, set prefix "" */
        prefix = TextUtils.isEmpty(prefix) ? "" : prefix;

        /** reset fileName by prefix and custom file name */
        fileName = TextUtils.isEmpty(fileName) ? prefix + splitFileName(getFileName(context, uri))[0] : fileName;

        return file.getAbsolutePath() + File.separator + fileName + "." + extension;
    }

    /**
     * Intercept file name
     * @param fileName file name
     */
    static String[] splitFileName(String fileName) {
        String name = fileName;
        String extension = "";
        int i = fileName.lastIndexOf(".");
        if (i != -1) {
            name = fileName.substring(0, i);
            extension = fileName.substring(i);
        }
        return new String[]{name, extension};
    }

    /**
     * Get the file name
     * @param context context
     * @param uri uri
     * @return file name
     */
    public static String getFileName(Context context, Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf(File.separator);
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }
}
