/*
 * Copyright 2014 Habzy Huang
 */
package com.habzy.image.picker;

public interface ViewPickerListener {

    /**
     * When cancel the intent of picking images.
     */
    void onCanceled();

    /**
     * When finish the intent of picking images.
     * @param paths The images' paths which are picked.
     */
    void onDone(String[] paths);

    void onTakeingPhoto();

    /**
     * When picking images changed, such as delete images.
     * @param paths The image paths which are picked.
     */
    void onImageChanged(String[] paths);
}
