/**
 *
 * Copyright habzy
 *
 */
package com.habzy.image.picker;

public interface ViewPickerListener {

    /**
     * When cancel the intent of picking images.
     */
    void onCanceled();

    /**
     * When finish the intent of picking images.
     * @return the images' paths which are picked.
     */
    String[] onDone();
}
