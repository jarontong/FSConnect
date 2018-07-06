package com.jaron.fsconnectparent.utils;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Glide 图片加载辅助类
 * 适配圆形图片加载情况
 */

public class ImageLoader {
    private ImageLoader() {
    }

    public static void loadImage(RequestManager loader, ImageView view, String url) {
        loadImage(loader, view, url, 0);
    }

    public static void loadImage(RequestManager loader, ImageView view, String url, int placeholder) {
        loadImage(loader, view, url, placeholder, placeholder);
    }

    public static void loadImage(RequestManager loader, ImageView view, String url, int placeholder, int error) {
        boolean isCenterCrop = false;
        if (view instanceof CircleImageView)
            isCenterCrop = true;
        loadImage(loader, view, url, placeholder, error, isCenterCrop);
    }

    public static void loadImage(RequestManager loader, ImageView view, String url, int placeholder, int error, boolean isCenterCrop) {
        if (TextUtils.isEmpty(url)) {
            view.setImageResource(placeholder);
        } else {
            if (view instanceof CircleImageView) {
                RequestOptions requestOptions;
                if (isCenterCrop)
                    requestOptions=new RequestOptions().placeholder(placeholder).error(error).diskCacheStrategy(DiskCacheStrategy.ALL).centerCrop();
                else
                    requestOptions=new RequestOptions().placeholder(placeholder).error(error).diskCacheStrategy(DiskCacheStrategy.ALL);
                RequestBuilder<Bitmap> builder = loader.asBitmap().apply(requestOptions).load(url);

                builder.into(
                        new BitmapImageViewTarget(view) {
                            @Override
                            protected void setResource(Bitmap resource) {
                                RoundedBitmapDrawable circularBitmapDrawable =
                                        RoundedBitmapDrawableFactory.create(view.getResources(), resource);
                                circularBitmapDrawable.setCircular(true);
                                view.setImageDrawable(circularBitmapDrawable);
                            }
                        });
            } else {
                RequestOptions requestOptions;
                if (isCenterCrop)
                    requestOptions=new RequestOptions().placeholder(placeholder).error(error).diskCacheStrategy(DiskCacheStrategy.ALL).centerCrop();
                else
                    requestOptions=new RequestOptions().placeholder(placeholder).error(error).diskCacheStrategy(DiskCacheStrategy.ALL);
                RequestBuilder<Drawable> builder = loader.asDrawable().apply(requestOptions).load(url);
                builder.into(view);
            }
        }
    }
}
