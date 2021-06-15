package com.retrofit.using.java.utilities.mediastore;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Log;

import com.retrofit.using.java.utilities.mediastore.audio.AudioMediaFile;
import com.retrofit.using.java.utilities.mediastore.images.ImagesMediaFile;
import com.retrofit.using.java.utilities.mediastore.video.VideoMediaFile;

import java.io.FileDescriptor;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class MediaStoreUtils {

    public static final String TAG = MediaStoreUtils.class.getSimpleName();

    private MediaStoreUtils() {
        throw new UnsupportedOperationException("Should not create instance of Util class. Please use as static..");
    }

    /**
     * Get source of media, internal or external
     *
     * @param sourceOfMedia         e.g.,   EXTERNAL, INTERNAL
     * @param mediaType             e.g.,   IMAGE, AUDIO, VIDEO
     * @return uri
     */
    public static Uri getSourceOfMedia(SourceOfMedia sourceOfMedia, MediaType mediaType) {

        Uri uri = null;

        if(sourceOfMedia == SourceOfMedia.INTERNAL)
        {
            switch(mediaType)
            {
                case IMAGES:
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        uri = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_INTERNAL);
                    } else {
                        uri = MediaStore.Images.Media.INTERNAL_CONTENT_URI;
                    }
                    break;
                case AUDIO:
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        uri = MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_INTERNAL);
                    } else {
                        uri = MediaStore.Audio.Media.INTERNAL_CONTENT_URI;
                    }
                    break;
                case VIDEO:
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        uri = MediaStore.Video.Media.getContentUri(MediaStore.VOLUME_INTERNAL);
                    } else {
                        uri = MediaStore.Video.Media.INTERNAL_CONTENT_URI;
                    }
                    break;
                case DOWNLOADS:
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        uri = MediaStore.Downloads.getContentUri(MediaStore.VOLUME_INTERNAL);
                    }
                    break;
                case FILES:
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        uri = MediaStore.Files.getContentUri(MediaStore.VOLUME_INTERNAL);
                    }
                    break;
                default:
                    throw new RuntimeException("invalid media type");
            }
        }
        else if(sourceOfMedia == SourceOfMedia.EXTERNAL)
        {
            switch(mediaType)
            {
                case IMAGES:
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        uri = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL);
                    } else {
                        uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    }
                    break;
                case AUDIO:
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        uri = MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL);
                    } else {
                        uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                    }
                    break;
                case VIDEO:
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        uri = MediaStore.Video.Media.getContentUri(MediaStore.VOLUME_EXTERNAL);
                    } else {
                        uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    }
                    break;
                case DOWNLOADS:
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        uri = MediaStore.Downloads.getContentUri(MediaStore.VOLUME_EXTERNAL);
                    }
                    break;
                case FILES:
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        uri = MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL);
                    }
                    break;
                default:
                    throw new RuntimeException("invalid media type");
            }
        }

        return uri;
    }

    /**
     * Get projection
     *
     * @param mediaType             e.g.,   IMAGE, AUDIO, VIDEO
     */
    public static List<String> getProjection(MediaType mediaType) {

        List<String> projection = new ArrayList<>();

        switch(mediaType)
        {
            case IMAGES:

                projection.add(MediaStore.Images.Media._ID);

                /* file name with extension */
                projection.add(MediaStore.Images.Media.DISPLAY_NAME);

                /* file name without extension */
                projection.add(MediaStore.Images.Media.TITLE);

                projection.add(MediaStore.Images.Media.DESCRIPTION);
                projection.add(MediaStore.Images.Media.MIME_TYPE);
                projection.add(MediaStore.Images.Media.SIZE);

                /* The width of the image/video in pixels. Added in API level 16 */
                projection.add(MediaStore.Images.Media.WIDTH);

                /* The height of the image/video in pixels. Added in API level 16 */
                projection.add(MediaStore.Images.Media.HEIGHT);

                projection.add(MediaStore.Images.Media.DATE_ADDED);
                projection.add(MediaStore.Images.Media.DATE_MODIFIED);

                /*
                 * This constant was deprecated in API level 29.
                 * Apps may not have filesystem permissions to directly access this path.
                 * Instead of trying to open this path directly, apps should use ContentResolver#openFileDescriptor(Uri, String) to gain access.
                 *
                 * return : path e.g., /storage/emulated/0/DCIM/Camera/20201226.jpg
                 */
                projection.add(MediaStore.Images.Media.DATA);

                /*
                 * This constant was deprecated in API level 29.
                 * Location details are no longer indexed for privacy reasons, and this value is now always null.
                 * You can still manually obtain location metadata using
                 */
                projection.add(MediaStore.Images.Media.LATITUDE);
                projection.add(MediaStore.Images.Media.LONGITUDE);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

                    /* folder id where image is store */
                    projection.add(MediaStore.Images.Media.BUCKET_ID);

                    /* folder name where image is store */
                    projection.add(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);

                    /*
                     * The date & time that the image was taken in units of milliseconds.
                     * It return a which date you image capture, or take screenshot.
                     * If image is download, send by other device, or copy from other source return null
                     */
                    projection.add(MediaStore.Images.Media.DATE_TAKEN);

                    projection.add(MediaStore.Images.Media.ORIENTATION);
                }

                break;
            case AUDIO:

                projection.add(MediaStore.Audio.Media._ID);

                /* file name with extension */
                projection.add(MediaStore.Audio.Media.DISPLAY_NAME);

                /* file name without extension */
                projection.add(MediaStore.Audio.Media.TITLE);

                projection.add(MediaStore.Audio.Media.MIME_TYPE);
                projection.add(MediaStore.Audio.Media.SIZE);

                /* The album the audio file is from, if any. */
                projection.add(MediaStore.Audio.Media.ALBUM);
                /* The id of the album the audio file is from, if any. */
                projection.add(MediaStore.Audio.Media.ALBUM_ID);
                /* A non human readable key calculated from the ALBUM, used for searching, sorting and grouping. */
                projection.add(MediaStore.Audio.Media.ALBUM_KEY);

                /* The artist who created the audio file, if any. */
                projection.add(MediaStore.Audio.Media.ARTIST);
                /* The id of the artist who created the audio file, if any. */
                projection.add(MediaStore.Audio.Media.ARTIST_ID);
                /* A non human readable key calculated from the ARTIST, used for searching, sorting and grouping. */
                projection.add(MediaStore.Audio.Media.ARTIST_KEY);

                /* The track number of this song on the album, if any. */
                projection.add(MediaStore.Audio.Media.TRACK);

                /* Non-zero if the audio file may be a ringtone. */
                projection.add(MediaStore.Audio.Media.IS_RINGTONE);

                /* A non human readable key calculated from the TITLE, used for searching, sorting and grouping. */
                projection.add(MediaStore.Audio.Media.TITLE_KEY);

                /* The year the audio file was recorded, if any. */
                projection.add(MediaStore.Audio.Media.YEAR);

                /* Non-zero if the audio file is a podcast. */
                projection.add(MediaStore.Audio.Media.IS_PODCAST);

                /* Non-zero if the audio file may be a notification sound. */
                projection.add(MediaStore.Audio.Media.IS_NOTIFICATION);

                /* Non-zero if the audio file is music. */
                projection.add(MediaStore.Audio.Media.IS_MUSIC);

                /* The position, in ms, playback was at when playback for this file was last stopped. */
                projection.add(MediaStore.Audio.Media.BOOKMARK);

                /* The composer of the audio file, if any. */
                projection.add(MediaStore.Audio.Media.COMPOSER);

                /* Non-zero if the audio file may be an alarm. */
                projection.add(MediaStore.Audio.Media.IS_ALARM);

                projection.add(MediaStore.Audio.Media.DATE_ADDED);
                projection.add(MediaStore.Audio.Media.DATE_MODIFIED);

                /*
                 * This constant was deprecated in API level 29.
                 * Apps may not have filesystem permissions to directly access this path.
                 * Instead of trying to open this path directly, apps should use ContentResolver#openFileDescriptor(Uri, String) to gain access.
                 *
                 * return : path e.g., /storage/emulated/0/DCIM/Camera/20201226.jpg
                 */
                projection.add(MediaStore.Audio.Media.DATA);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

                    /* folder id where image is store */
                    projection.add(MediaStore.Audio.Media.BUCKET_ID);

                    /* folder name where image is store */
                    projection.add(MediaStore.Audio.Media.BUCKET_DISPLAY_NAME);

                    /*
                     * The date & time that the image was taken in units of milliseconds.
                     * It return a which date you image capture, or take screenshot.
                     * If image is download, send by other device, or copy from other source return null
                     */
                    projection.add(MediaStore.Audio.Media.DATE_TAKEN);

                    /* The duration of the audio file, in ms. */
                    projection.add(MediaStore.Audio.Media.DURATION);
                }

                break;
            case VIDEO:

                projection.add(MediaStore.Video.Media._ID);

                /* file name with extension */
                projection.add(MediaStore.Video.Media.DISPLAY_NAME);

                /* file name without extension */
                projection.add(MediaStore.Video.Media.TITLE);

                projection.add(MediaStore.Video.Thumbnails.DATA);
                projection.add(MediaStore.Video.Media.DESCRIPTION);
                projection.add(MediaStore.Video.Media.MIME_TYPE);
                projection.add(MediaStore.Video.Media.SIZE);

                /* The width of the image/video in pixels. Added in API level 16 */
                projection.add(MediaStore.Video.Media.WIDTH);

                /* The height of the image/video in pixels. Added in API level 16 */
                projection.add(MediaStore.Video.Media.HEIGHT);

                projection.add(MediaStore.Video.Media.DATE_ADDED);
                projection.add(MediaStore.Video.Media.DATE_MODIFIED);

                /*
                 * This constant was deprecated in API level 29.
                 * Apps may not have filesystem permissions to directly access this path.
                 * Instead of trying to open this path directly, apps should use ContentResolver#openFileDescriptor(Uri, String) to gain access.
                 *
                 * return : path e.g., /storage/emulated/0/DCIM/Camera/20201226.jpg
                 */
                projection.add(MediaStore.Video.Media.DATA);

                /*
                 * This constant was deprecated in API level 29.
                 * Location details are no longer indexed for privacy reasons, and this value is now always null.
                 * You can still manually obtain location metadata using
                 */
                projection.add(MediaStore.Video.Media.LATITUDE);
                projection.add(MediaStore.Video.Media.LONGITUDE);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

                    /* folder id where image is store */
                    projection.add(MediaStore.Video.Media.BUCKET_ID);

                    /* folder name where image is store */
                    projection.add(MediaStore.Video.Media.BUCKET_DISPLAY_NAME);

                    /*
                     * The date & time that the image was taken in units of milliseconds.
                     * It return a which date you image capture, or take screenshot.
                     * If image is download, send by other device, or copy from other source return null
                     */
                    projection.add(MediaStore.Video.Media.DATE_TAKEN);

                    projection.add(MediaStore.Video.Media.ORIENTATION);

                    /* The duration of the audio file, in ms. */
                    projection.add(MediaStore.Video.Media.DURATION);
                }

                break;
            default:
                throw new RuntimeException("invalid media type");
        }

        return projection;
    }

    /**
     * Read Image Media Files
     *
     * @param context
     *                              The context.
     *
     *                              e.g.,   getApplicationContext()
     *
     * @param sourceUri
     *                              Define source of Media, internal or external storage
     *                              Where the file is saved
     *
     *                              e.g.,
     *                                      Uri sourceUri;
     *                                      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
     *                                          sourceUri = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL);
     *                                      } else {
     *                                          sourceUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
     *                                      }
     *
     * @param projection
     *                              First we have to specify what are the contents we are going to retrieve, for that we will create an array of projections we need.
     *                              Which columns values retrieve
     *
     *                              e.g.,
     *                                      String[] projection = new String[]{
     *                                            MediaStore.Images.Media._ID,
     *                                            MediaStore.Images.Media.DISPLAY_NAME,
     *                                            MediaStore.Images.Media.DATE_ADDED,
     *                                            MediaStore.Images.Media.SIZE
     *                                      };
     *
     * @param selection
     *                              Same as the WHERE keyword in database.
     *                              Filter used in the query.
     *
     *                              e.g., String selection = MediaStore.Images.Media.DATE_ADDED + " >= ?";
     *
     * @param selectionArgs
     *                              Selection arguments used in the query.
     *
     *                              e.g., String[] selectionArgs = new String[] { String.valueOf(1586180938) };
     *
     * @param sortOrder
     *                              Specify the order in which the rows of query results are displayed.
     *
     *                              e.g., String sortOrder = MediaStore.Images.Media.DATE_ADDED + " DESC";
     */
    public static List<ImagesMediaFile> readImageMediaFiles(Context context,
                                                            Uri sourceUri,
                                                            String[] projection,
                                                            String selection,
                                                            String[] selectionArgs,
                                                            String sortOrder) {

        List<ImagesMediaFile> mediaFilesArrayList = new ArrayList<ImagesMediaFile>();

        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = null;

        try
        {
            /*
             * Now set the sourceUri, projection, selection, selectionArgs, sortOrder and make a query for data, for that purpose we will use Cursor interface.
             * Cursor is very useful when you are retrieving some data from storage. It provides read-write access to the result set return by a query.
             */
            cursor = contentResolver.query(
                    sourceUri,
                    projection,
                    selection,
                    selectionArgs,
                    sortOrder
            );

            if (cursor == null)
            {
                /* Query failed... */
                Log.e(TAG, "Failed to retrieve music: cursor is null.");
            }
            else if (!cursor.moveToFirst())
            {
                /* Nothing to query. There is no music on the device. How boring. cursor is empty*/
                Log.e(TAG, "Failed to move cursor to first row (no query results).");
            }
            else
            {
                while (cursor.moveToNext())
                {
                    long mediaId                        = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID));
                    Uri contentUri                      = ContentUris.withAppendedId(sourceUri, mediaId);

                    String imageNameWithExtension       = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME));
                    String imageNameWithoutExtension    = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.TITLE));
                    String description                  = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DESCRIPTION));
                    String mimeType                     = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.MIME_TYPE));
                    int fileSize                        = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE));
                    int width                           = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.WIDTH));
                    int height                          = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.HEIGHT));
                    long dateAdded                      = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_ADDED));
                    long dateModified                   = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_MODIFIED));

                    String data                         = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
                    String latitude                     = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.LATITUDE));
                    String longitude                    = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.LONGITUDE));

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        int bucketId                        = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_ID));
                        String bucketName                   = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME));
                        long dateTaken                      = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_TAKEN));
                        int orientation                     = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.ORIENTATION));

                        mediaFilesArrayList.add(new ImagesMediaFile(
                                contentUri,
                                imageNameWithExtension,
                                imageNameWithoutExtension,
                                description,
                                mimeType,
                                fileSize,
                                width,
                                height,
                                dateAdded,
                                dateModified,
                                data,
                                latitude,
                                longitude,
                                bucketId,
                                bucketName,
                                dateTaken,
                                orientation)
                        );

                        Log.i(TAG, "MEDIA ID : " + mediaId);
                        Log.i(TAG, "IMAGE NAME WITH EXTENSION : " + imageNameWithExtension);
                        Log.i(TAG, "IMAGE NAME WITHOUT EXTENSION : " + imageNameWithoutExtension);
                        Log.i(TAG, "DESCRIPTION : " + description);
                        Log.i(TAG, "MIME TYPE : " + mimeType);
                        Log.i(TAG, "FILE SIZE : " + getReadableFileSize(fileSize));
                        Log.i(TAG, "WIDTH : " + width);
                        Log.i(TAG, "HEIGHT : " + height);
                        Log.i(TAG, "DATE ADDED : " + dateAdded);
                        Log.i(TAG, "DATE MODIFIED : " + dateModified);
                        Log.i(TAG, "DATA : " + data);
                        Log.i(TAG, "LATITUDE : " + latitude);
                        Log.i(TAG, "LONGITUDE : " + longitude);
                        Log.i(TAG, "BUCKET ID : " + bucketId);
                        Log.i(TAG, "BUCKET NAME : " + bucketName);
                        Log.i(TAG, "DATE TAKEN : " + dateTaken);
                        Log.i(TAG, "ORIENTATION : " + orientation);
                        Log.i(TAG, "========================================================================");
                    }
                    else
                    {
                        mediaFilesArrayList.add(new ImagesMediaFile(
                                contentUri,
                                imageNameWithExtension,
                                imageNameWithoutExtension,
                                description,
                                mimeType,
                                fileSize,
                                width,
                                height,
                                dateAdded,
                                dateModified,
                                data,
                                latitude,
                                longitude)
                        );

                        Log.i(TAG, "MEDIA ID : " + mediaId);
                        Log.i(TAG, "IMAGE NAME WITH EXTENSION : " + imageNameWithExtension);
                        Log.i(TAG, "IMAGE NAME WITHOUT EXTENSION : " + imageNameWithoutExtension);
                        Log.i(TAG, "DESCRIPTION : " + description);
                        Log.i(TAG, "MIME TYPE : " + mimeType);
                        Log.i(TAG, "FILE SIZE : " + getReadableFileSize(fileSize));
                        Log.i(TAG, "WIDTH : " + width);
                        Log.i(TAG, "HEIGHT : " + height);
                        Log.i(TAG, "DATE ADDED : " + dateAdded);
                        Log.i(TAG, "DATE MODIFIED : " + dateModified);
                        Log.i(TAG, "DATA : " + data);
                        Log.i(TAG, "LATITUDE : " + latitude);
                        Log.i(TAG, "LONGITUDE : " + longitude);
                        Log.i(TAG, "========================================================================");
                    }
                }
            }
        }
        finally
        {
            if (cursor != null)
                cursor.close();
        }
        return mediaFilesArrayList;
    }

    /**
     * Read Audio Media Files
     */
    public static List<AudioMediaFile> readAudioMediaFiles(Context context,
                                                           Uri sourceUri,
                                                           String[] projection,
                                                           String selection,
                                                           String[] selectionArgs,
                                                           String sortOrder) {

        List<AudioMediaFile> mediaFilesArrayList = new ArrayList<AudioMediaFile>();

        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = null;

        try
        {
            /*
             * Now set the sourceUri, projection, selection, selectionArgs, sortOrder and make a query for data, for that purpose we will use Cursor interface.
             * Cursor is very useful when you are retrieving some data from storage. It provides read-write access to the result set return by a query.
             */
            cursor = contentResolver.query(
                    sourceUri,
                    projection,
                    selection,
                    selectionArgs,
                    sortOrder
            );

            if (cursor == null)
            {
                /* Query failed... */
                Log.e(TAG, "Failed to retrieve music: cursor is null.");
            }
            else if (!cursor.moveToFirst())
            {
                /* Nothing to query. There is no music on the device. How boring. cursor is empty*/
                Log.e(TAG, "Failed to move cursor to first row (no query results).");
            }
            else
            {
                while (cursor.moveToNext())
                {
                    long mediaId                        = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID));
                    Uri contentUri                      = ContentUris.withAppendedId(sourceUri, mediaId);

                    String audioNameWithExtension       = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME));
                    String audioNameWithoutExtension    = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
                    String album                        = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM));
                    String albumId                      = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID));
                    String albumKey                     = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_KEY));
                    String artist                       = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
                    String artistId                     = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST_ID));
                    String artistKey                    = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST_KEY));
                    String track                        = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TRACK));
                    String isRingtone                   = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.IS_RINGTONE));
                    String titleKey                     = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE_KEY));
                    String year                         = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.YEAR));
                    String isPodcast                    = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.IS_PODCAST));
                    String isNotification               = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.IS_NOTIFICATION));
                    String isMusic                      = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.IS_MUSIC));
                    String bookmark                     = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.BOOKMARK));
                    String composer                     = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.COMPOSER));
                    String isAlarm                      = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.IS_ALARM));
                    String mimeType                     = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.MIME_TYPE));
                    int fileSize                        = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE));

                    long dateAdded                      = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATE_ADDED));
                    long dateModified                   = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATE_MODIFIED));

                    String data                         = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        int bucketId                        = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.BUCKET_ID));
                        String bucketName                   = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.BUCKET_DISPLAY_NAME));
                        long dateTaken                      = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATE_TAKEN));
                        String duration                     = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));

                        mediaFilesArrayList.add(new AudioMediaFile(
                                contentUri,
                                audioNameWithExtension,
                                audioNameWithoutExtension,
                                album,
                                albumId,
                                albumKey,
                                artist,
                                artistId,
                                artistKey,
                                track,
                                isRingtone,
                                titleKey,
                                year,
                                isPodcast,
                                isNotification,
                                isMusic,
                                bookmark,
                                composer,
                                isAlarm,
                                duration,
                                mimeType,
                                fileSize,
                                dateAdded,
                                dateModified,
                                data,
                                bucketId,
                                bucketName,
                                dateTaken));

                        Log.i(TAG, "MEDIA ID : " + mediaId);
                        Log.i(TAG, "IMAGE NAME WITH EXTENSION : " + audioNameWithExtension);
                        Log.i(TAG, "IMAGE NAME WITHOUT EXTENSION : " + audioNameWithoutExtension);
                        Log.i(TAG, "MIME TYPE : " + mimeType);
                        Log.i(TAG, "FILE SIZE : " + getReadableFileSize(fileSize));
                        Log.i(TAG, "DATE ADDED : " + dateAdded);
                        Log.i(TAG, "DATE MODIFIED : " + dateModified);
                        Log.i(TAG, "DATA : " + data);
                        Log.i(TAG, "BUCKET ID : " + bucketId);
                        Log.i(TAG, "BUCKET NAME : " + bucketName);
                        Log.i(TAG, "DATE TAKEN : " + dateTaken);
                        Log.i(TAG, "========================================================================");
                    }
                    else
                    {
                        mediaFilesArrayList.add(new AudioMediaFile(
                                contentUri,
                                audioNameWithExtension,
                                audioNameWithoutExtension,
                                album,
                                albumId,
                                albumKey,
                                artist,
                                artistId,
                                artistKey,
                                track,
                                isRingtone,
                                titleKey,
                                year,
                                isPodcast,
                                isNotification,
                                isMusic,
                                bookmark,
                                composer,
                                isAlarm,
                                mimeType,
                                fileSize,
                                dateAdded,
                                dateModified,
                                data)
                        );

                        Log.i(TAG, "MEDIA ID : " + mediaId);
                        Log.i(TAG, "IMAGE NAME WITH EXTENSION : " + audioNameWithExtension);
                        Log.i(TAG, "IMAGE NAME WITHOUT EXTENSION : " + audioNameWithoutExtension);
                        Log.i(TAG, "MIME TYPE : " + mimeType);
                        Log.i(TAG, "FILE SIZE : " + getReadableFileSize(fileSize));
                        Log.i(TAG, "DATE ADDED : " + dateAdded);
                        Log.i(TAG, "DATE MODIFIED : " + dateModified);
                        Log.i(TAG, "DATA : " + data);
                        Log.i(TAG, "========================================================================");
                    }
                }
            }
        }
        finally
        {
            if (cursor != null)
                cursor.close();
        }
        return mediaFilesArrayList;
    }

    /**
     * Read Video Media Files
     */
    public static List<VideoMediaFile> readVideoMediaFiles(Context context,
                                                           Uri sourceUri,
                                                           String[] projection,
                                                           String selection,
                                                           String[] selectionArgs,
                                                           String sortOrder) {

        List<VideoMediaFile> mediaFilesArrayList = new ArrayList<VideoMediaFile>();

        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = null;

        try
        {
            /*
             * Now set the sourceUri, projection, selection, selectionArgs, sortOrder and make a query for data, for that purpose we will use Cursor interface.
             * Cursor is very useful when you are retrieving some data from storage. It provides read-write access to the result set return by a query.
             */
            cursor = contentResolver.query(
                    sourceUri,
                    projection,
                    selection,
                    selectionArgs,
                    sortOrder
            );

            if (cursor == null)
            {
                /* Query failed... */
                Log.e(TAG, "Failed to retrieve music: cursor is null.");
            }
            else if (!cursor.moveToFirst())
            {
                /* Nothing to query. There is no music on the device. How boring. cursor is empty*/
                Log.e(TAG, "Failed to move cursor to first row (no query results).");
            }
            else
            {
                while (cursor.moveToNext())
                {
                    long mediaId                        = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID));
                    Uri contentUri                      = ContentUris.withAppendedId(sourceUri, mediaId);

                    String imageNameWithExtension       = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME));
                    String imageNameWithoutExtension    = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE));
                    String thumbnails                   = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Thumbnails.DATA));
                    String description                  = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DESCRIPTION));
                    String mimeType                     = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.MIME_TYPE));
                    int fileSize                        = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE));
                    int width                           = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.WIDTH));
                    int height                          = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.HEIGHT));
                    long dateAdded                      = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATE_ADDED));
                    long dateModified                   = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATE_MODIFIED));

                    String data                         = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
                    String latitude                     = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.LATITUDE));
                    String longitude                    = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.LONGITUDE));

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        int bucketId                        = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.BUCKET_ID));
                        String bucketName                   = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.BUCKET_DISPLAY_NAME));
                        long dateTaken                      = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATE_TAKEN));
                        int orientation                     = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.ORIENTATION));
                        String duration                     = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION));

                        mediaFilesArrayList.add(new VideoMediaFile(
                                contentUri,
                                imageNameWithExtension,
                                imageNameWithoutExtension,
                                description,
                                mimeType,
                                fileSize,
                                width,
                                height,
                                dateAdded,
                                dateModified,
                                data,
                                latitude,
                                longitude,
                                bucketId,
                                bucketName,
                                dateTaken,
                                orientation,
                                duration)
                        );

                        Log.i(TAG, "MEDIA ID : " + mediaId);
                        Log.i(TAG, "IMAGE NAME WITH EXTENSION : " + imageNameWithExtension);
                        Log.i(TAG, "IMAGE NAME WITHOUT EXTENSION : " + imageNameWithoutExtension);
                        Log.i(TAG, "THUMBNAILS : " + thumbnails);
                        Log.i(TAG, "DESCRIPTION : " + description);
                        Log.i(TAG, "MIME TYPE : " + mimeType);
                        Log.i(TAG, "FILE SIZE : " + getReadableFileSize(fileSize));
                        Log.i(TAG, "WIDTH : " + width);
                        Log.i(TAG, "HEIGHT : " + height);
                        Log.i(TAG, "DATE ADDED : " + dateAdded);
                        Log.i(TAG, "DATE MODIFIED : " + dateModified);
                        Log.i(TAG, "DATA : " + data);
                        Log.i(TAG, "LATITUDE : " + latitude);
                        Log.i(TAG, "LONGITUDE : " + longitude);
                        Log.i(TAG, "BUCKET ID : " + bucketId);
                        Log.i(TAG, "BUCKET NAME : " + bucketName);
                        Log.i(TAG, "DATE TAKEN : " + dateTaken);
                        Log.i(TAG, "ORIENTATION : " + orientation);
                        Log.i(TAG, "========================================================================");
                    }
                    else
                    {
                        mediaFilesArrayList.add(new VideoMediaFile(
                                contentUri,
                                imageNameWithExtension,
                                imageNameWithoutExtension,
                                description,
                                mimeType,
                                fileSize,
                                width,
                                height,
                                dateAdded,
                                dateModified,
                                data,
                                latitude,
                                longitude)
                        );

                        Log.i(TAG, "MEDIA ID : " + mediaId);
                        Log.i(TAG, "IMAGE NAME WITH EXTENSION : " + imageNameWithExtension);
                        Log.i(TAG, "IMAGE NAME WITHOUT EXTENSION : " + imageNameWithoutExtension);
                        Log.i(TAG, "DESCRIPTION : " + description);
                        Log.i(TAG, "MIME TYPE : " + mimeType);
                        Log.i(TAG, "FILE SIZE : " + getReadableFileSize(fileSize));
                        Log.i(TAG, "WIDTH : " + width);
                        Log.i(TAG, "HEIGHT : " + height);
                        Log.i(TAG, "DATE ADDED : " + dateAdded);
                        Log.i(TAG, "DATE MODIFIED : " + dateModified);
                        Log.i(TAG, "DATA : " + data);
                        Log.i(TAG, "LATITUDE : " + latitude);
                        Log.i(TAG, "LONGITUDE : " + longitude);
                        Log.i(TAG, "========================================================================");
                    }
                }
            }
        }
        finally
        {
            if (cursor != null)
                cursor.close();
        }
        return mediaFilesArrayList;
    }

    /**
     * Read Image Using Uri
     * After obtaining Uri through the above query method, read the file.
     *
     * @param context
     * @param imageUri
     */
    public static Bitmap readImage(Context context, Uri imageUri) {

        ParcelFileDescriptor parcelFileDescriptor = null;
        Bitmap bitmap = null;

        try
        {
            parcelFileDescriptor = context.getContentResolver().openFileDescriptor(imageUri, "r");

            if (parcelFileDescriptor != null) {
                FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
                bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor);
            }
        }
        catch (IOException iOException)
        {
            Log.e(TAG, "Could not read file 1 : " + iOException.getMessage());
            bitmap = null;
        }
        finally
        {
            if (parcelFileDescriptor  != null)
            {
                try
                {
                    parcelFileDescriptor.close();
                }
                catch (IOException iOException)
                {
                    Log.e(TAG, "Could not read file 3 : " + iOException.getMessage());
                }
            }
        }
        return bitmap;
    }

    /**
     * Create image media file
     *
     * @param context
     * @param sourceUri
     * @param fileName
     * @param fileNameWithExtension
     * @param mimeType
     * @param environment   image allowed directories are [Environment.DIRECTORY_DCIM, Environment.DIRECTORY_PICTURES]
     * @param customFolder
     */
    public static Uri createImagesMediaFile(Context context,
                                            Uri sourceUri,
                                            String fileName,
                                            String fileNameWithExtension,
                                            String mimeType,
                                            String environment,
                                            String customFolder) {

        ContentResolver contentResolver = context.getContentResolver();

        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.TITLE, fileName);
        contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, fileNameWithExtension);
        contentValues.put(MediaStore.Images.Media.MIME_TYPE, mimeType);

        /* Add the date meta data to ensure the image is added at the front of the gallery */
        long millis = System.currentTimeMillis();
        contentValues.put(MediaStore.Images.Media.DATE_ADDED, millis / 1000L);
        contentValues.put(MediaStore.Images.Media.DATE_MODIFIED, millis / 1000L);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            contentValues.put(MediaStore.Images.Media.DATE_TAKEN, millis);
            contentValues.put(MediaStore.Images.Media.RELATIVE_PATH, environment+"/"+customFolder);
            contentValues.put(MediaStore.Images.Media.IS_PENDING, true);
        }

        Uri createdFileUri = contentResolver.insert(sourceUri, contentValues);
        return createdFileUri;
    }

    /**
     * Create audio media file
     *
     * @param context
     * @param sourceUri
     * @param fileName
     * @param fileNameWithExtension
     * @param mimeType
     * @param environment   audio allowed directories are [Environment.DIRECTORY_ALARMS, Environment.DIRECTORY_MUSIC, Environment.NOTIFICATIONS, Environment.DIRECTORY_PODCASTS, Environment.RINGTONES, Environment.DIRECTORY_AUDIOBOOKS]
     * @param customFolder
     */
    public static Uri createAudioMediaFile(Context context,
                                           Uri sourceUri,
                                           String fileName,
                                           String fileNameWithExtension,
                                           String mimeType,
                                           String environment,
                                           String customFolder) {

        ContentResolver contentResolver = context.getContentResolver();

        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Audio.Media.TITLE, fileName);
        contentValues.put(MediaStore.Audio.Media.DISPLAY_NAME, fileNameWithExtension);
        contentValues.put(MediaStore.Audio.Media.MIME_TYPE, mimeType);

        /* Add the date meta data to ensure the image is added at the front of the gallery */
        long millis = System.currentTimeMillis();
        contentValues.put(MediaStore.Audio.Media.DATE_ADDED, millis / 1000L);
        contentValues.put(MediaStore.Audio.Media.DATE_MODIFIED, millis / 1000L);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            contentValues.put(MediaStore.Audio.Media.DATE_TAKEN, millis);
            contentValues.put(MediaStore.Audio.Media.RELATIVE_PATH, environment+"/"+customFolder);
            contentValues.put(MediaStore.Audio.Media.IS_PENDING, true);
        }

        Uri createdFileUri = contentResolver.insert(sourceUri, contentValues);
        return createdFileUri;
    }

    /**
     * Create video media file
     *
     * @param context
     * @param sourceUri
     * @param fileName
     * @param fileNameWithExtension
     * @param mimeType
     * @param environment   video allowed directories are [Environment.DIRECTORY_DCIM, Environment.DIRECTORY_MOVIES]
     * @param customFolder
     */
    public static Uri createVideoMediaFile(Context context,
                                           Uri sourceUri,
                                           String fileName,
                                           String fileNameWithExtension,
                                           String mimeType,
                                           String environment,
                                           String customFolder) {

        ContentResolver contentResolver = context.getContentResolver();

        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Video.Media.TITLE, fileName);
        contentValues.put(MediaStore.Video.Media.DISPLAY_NAME, fileNameWithExtension);
        contentValues.put(MediaStore.Video.Media.MIME_TYPE, mimeType);

        /* Add the date meta data to ensure the image is added at the front of the gallery */
        long millis = System.currentTimeMillis();
        contentValues.put(MediaStore.Video.Media.DATE_ADDED, millis / 1000L);
        contentValues.put(MediaStore.Video.Media.DATE_MODIFIED, millis / 1000L);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            contentValues.put(MediaStore.Video.Media.DATE_TAKEN, millis);
            contentValues.put(MediaStore.Video.Media.RELATIVE_PATH, environment+"/"+customFolder);
            /*
             * Android 10 is inserted into the gallery flag
             *
             * true or 1 means is not complete not show in gallery
             * false or 0 means is complete show in gallery
             */
            contentValues.put(MediaStore.Video.Media.IS_PENDING, true);
        }

        Uri createdFileUri = contentResolver.insert(sourceUri, contentValues);
        return createdFileUri;
    }

    /**
     * Update image media file (Update display name)
     *
     * @param context
     * @param updateThisUri
     * @param fileNameWithExtension
     */
    public static int updateImagesMediaFile(Context context,
                                            Uri updateThisUri,
                                            String fileNameWithExtension) {

        ContentResolver contentResolver = context.getContentResolver();

        ContentValues updateContentValue = new ContentValues();
        updateContentValue.put(MediaStore.Images.Media.DISPLAY_NAME, fileNameWithExtension);

        int numberOfRowUpdated = contentResolver.update(updateThisUri, updateContentValue, null, null);
        return numberOfRowUpdated;
    }

    /**
     * Update audio media file (Update display name)
     *
     * @param context
     * @param updateThisUri
     * @param fileNameWithExtension
     */
    public static int updateAudioMediaFile(Context context,
                                           Uri updateThisUri,
                                           String fileNameWithExtension) {

        ContentResolver contentResolver = context.getContentResolver();

        ContentValues updateContentValue = new ContentValues();
        updateContentValue.put(MediaStore.Audio.Media.DISPLAY_NAME, fileNameWithExtension);

        int numberOfRowUpdated = contentResolver.update(updateThisUri, updateContentValue, null, null);
        return numberOfRowUpdated;
    }

    /**
     * Update video media file (Update display name)
     *
     * @param context
     * @param updateThisUri
     * @param fileNameWithExtension
     */
    public static int updateVideoMediaFile(Context context,
                                           Uri updateThisUri,
                                           String fileNameWithExtension) {

        ContentResolver contentResolver = context.getContentResolver();

        ContentValues updateContentValue = new ContentValues();
        updateContentValue.put(MediaStore.Video.Media.DISPLAY_NAME, fileNameWithExtension);

        int numberOfRowUpdated = contentResolver.update(updateThisUri, updateContentValue, null, null);
        return numberOfRowUpdated;
    }

    /**
     * Get the file size in a human-readable string.
     *
     * @param size
     */
    public static String getReadableFileSize(int size) {
        final int BYTES_IN_KILOBYTES = 1024;
        final DecimalFormat dec = new DecimalFormat("###.#");
        final String KILOBYTES = " KB";
        final String MEGABYTES = " MB";
        final String GIGABYTES = " GB";
        float fileSize = 0;
        String suffix = KILOBYTES;

        if (size > BYTES_IN_KILOBYTES)
        {
            fileSize = size / BYTES_IN_KILOBYTES;
            if (fileSize > BYTES_IN_KILOBYTES)
            {
                fileSize = fileSize / BYTES_IN_KILOBYTES;
                if (fileSize > BYTES_IN_KILOBYTES)
                {
                    fileSize = fileSize / BYTES_IN_KILOBYTES;
                    suffix = GIGABYTES;
                }
                else
                {
                    suffix = MEGABYTES;
                }
            }
        }
        return String.valueOf(dec.format(fileSize) + suffix);
    }

    /**
     * converting long duration to  hh:mm:ss
     *
     * @param millie
     */
    public static String convertMillieToHMmSs(long millie) {
        String audioTime;

        long seconds = (millie / 1000);
        long second = seconds % 60;
        long minute = (seconds / 60) % 60;
        long hour = (seconds / (60 * 60)) % 24;

        if (hour > 0) {
            audioTime = String.format("%02d:%02d:%02d", hour, minute, second);
        } else {
            audioTime = String.format("%02d:%02d" , minute, second);
        }
        return audioTime;
    }
}
