/*
 * Copyright 2014 Habzy Huang
 */
package com.habzy.image.tools;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.TypedValue;

import com.habzy.image.models.ItemModel;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.utils.StorageUtils;

public class ImageTools {

    private static ImageLoader instance = null;

    public static ImageLoader getImageLoaderInstance(Context context) {
        if (null == instance) {
            instance = getImageLoader(context);
        }
        return instance;
    }

    private static ImageLoader getImageLoader(Context context) {
        ImageLoader imageLoader = null;
        try {
            String CACHE_DIR =
                    Environment.getExternalStorageDirectory().getAbsolutePath() + "/.temp_tmp";
            new File(CACHE_DIR).mkdirs();

            File cacheDir = StorageUtils.getOwnCacheDirectory(context, CACHE_DIR);

            DisplayImageOptions defaultOptions =
                    new DisplayImageOptions.Builder().cacheOnDisk(true).cacheInMemory(true)
                            .imageScaleType(ImageScaleType.EXACTLY)
                            .bitmapConfig(Bitmap.Config.RGB_565).build();
            ImageLoaderConfiguration.Builder builder =
                    new ImageLoaderConfiguration.Builder(context)
                            .defaultDisplayImageOptions(defaultOptions).threadPoolSize(5)
                            .diskCache(new UnlimitedDiscCache(cacheDir))
                            .memoryCache(new UsingFreqLimitedMemoryCache(1024 * 1024 * 20));

            ImageLoaderConfiguration config = builder.build();
            imageLoader = ImageLoader.getInstance();
            imageLoader.init(config);

        } catch (Exception e) {

        }
        return imageLoader;
    }

    public static ArrayList<ItemModel> getGalleryPhotos(ContentResolver resolver) {
        ArrayList<ItemModel> galleryList = new ArrayList<ItemModel>();

        try {
            final String[] columns = {MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID};
            final String orderBy = MediaStore.Images.Media._ID;

            Cursor imagecursor =
                    resolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null,
                            null, orderBy);

            if (imagecursor != null && imagecursor.getCount() > 0) {

                while (imagecursor.moveToNext()) {
                    ItemModel item = new ItemModel();

                    int dataColumnIndex = imagecursor.getColumnIndex(MediaStore.Images.Media.DATA);

                    item.mPath = "file://" + imagecursor.getString(dataColumnIndex);

                    galleryList.add(item);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // show newest photo at beginning of the list
        Collections.reverse(galleryList);
        return galleryList;
    }

    public static int dpToPx(Resources res, int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                res.getDisplayMetrics());
    }

}
