package com.jpventura.footballscores.widget;

import android.annotation.TargetApi;
import android.graphics.drawable.PictureDrawable;
import android.net.Uri;
import android.os.Build;
import android.widget.ImageView;

import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.bumptech.glide.request.target.Target;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class DrawableRequestListener implements RequestListener<Uri, PictureDrawable> {
    @Override
    public boolean onException(Exception e, Uri uri, Target<PictureDrawable> target, boolean isFirstResource) {
        if (Build.VERSION_CODES.HONEYCOMB <= Build.VERSION.SDK_INT) {
            ImageView view = ((ImageViewTarget<?>) target).getView();
            view.setLayerType(ImageView.LAYER_TYPE_NONE, null);
        }

        return false;
    }

    @Override
    public boolean onResourceReady(PictureDrawable resource, Uri uri, Target<PictureDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
        if (Build.VERSION_CODES.HONEYCOMB <= Build.VERSION.SDK_INT) {
            ImageView view = ((ImageViewTarget<?>) target).getView();
            view.setLayerType(ImageView.LAYER_TYPE_SOFTWARE, null);
        }

        return false;
    }
}
