/**
 * 
 * Copyright habzy
 * 
 */
package com.habzy.image.tools;

import java.io.File;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.utils.StorageUtils;

public class ImageTools {

    public static ImageLoader getImageLoader(Context context) {
        ImageLoader imageLoader = null;
        try {
            String CACHE_DIR =
                    Environment.getExternalStorageDirectory().getAbsolutePath() + "/.temp_tmp";
            new File(CACHE_DIR).mkdirs();

            File cacheDir = StorageUtils.getOwnCacheDirectory(context, CACHE_DIR);

            DisplayImageOptions defaultOptions =
                    new DisplayImageOptions.Builder().cacheOnDisk(true)
                            .imageScaleType(ImageScaleType.EXACTLY)
                            .bitmapConfig(Bitmap.Config.RGB_565).build();
            ImageLoaderConfiguration.Builder builder =
                    new ImageLoaderConfiguration.Builder(context)
                            .defaultDisplayImageOptions(defaultOptions)
                            .diskCache(new UnlimitedDiskCache(cacheDir))
                            .memoryCache(new WeakMemoryCache());

            ImageLoaderConfiguration config = builder.build();
            imageLoader = ImageLoader.getInstance();
            imageLoader.init(config);

        } catch (Exception e) {

        }
        return imageLoader;
    }

}
