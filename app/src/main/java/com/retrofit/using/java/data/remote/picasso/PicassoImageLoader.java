package com.retrofit.using.java.data.remote.picasso;

import android.content.Context;
import android.widget.ImageView;
import androidx.annotation.DrawableRes;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class PicassoImageLoader {

    public static final String TAG = PicassoImageLoader.class.getSimpleName();

    /**
     * Load picture
     *
     * @param context
     * @param url
     * @param placeholder
     * @param error
     * @param imageView
     */
    public static void load(Context context, String url, @DrawableRes int placeholder, @DrawableRes int error, ImageView imageView, PicassoImageLoadingListener picassoImageLoadingListener) {
        Picasso.get()
                .load(url)
                .placeholder(placeholder)
                .error(error)
                .into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        picassoImageLoadingListener.imageLoadSuccess();
                    }

                    @Override
                    public void onError(Exception exception) {
                        picassoImageLoadingListener.imageLoadError(exception);
                    }
        });
    }
}
