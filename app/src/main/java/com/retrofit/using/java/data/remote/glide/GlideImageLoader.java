package com.retrofit.using.java.data.remote.glide;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

public class GlideImageLoader {

    public static final String TAG = GlideImageLoader.class.getSimpleName();

    /**
     * Load picture
     *
     * @param context
     * @param url
     * @param placeholder
     * @param error
     * @param imageView
     */
    public static void load(Context context, String url, @DrawableRes int placeholder, @DrawableRes int error, ImageView imageView, GlideImageLoadingListener glideImageLoadingListener) {
        Glide.with(context)
                .load(url)
                .fitCenter()
                .placeholder(placeholder)
                .error(error)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        glideImageLoadingListener.imageLoadError();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        glideImageLoadingListener.imageLoadSuccess();
                        return false;
                    }
                })
                .into(imageView);
    }
}
