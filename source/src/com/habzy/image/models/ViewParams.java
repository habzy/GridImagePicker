/*
 * Copyright 2014 Habzy Huang
 */
package com.habzy.image.models;

import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.util.Log;

public class ViewParams {

    public final static int DEFAULT_NUM_CLUMNS = 4;
    public final static float DEFAULT_ITEM_PADDING_DIP = 5;

    private static final String TAG = ViewParams.class.getSimpleName();

    private final DisplayMetrics mMetics;

    private int mNumClumns = DEFAULT_NUM_CLUMNS;
    private boolean isMutiPick = true;
    private boolean isViewOnlyModel = false;
    private Drawable mCheckBoxDrawable = null;
    private Drawable mTakePhotoDrawable = null;
    private Drawable mLoadingImageDrawable = null;
    private float mItemPaddingDip = DEFAULT_ITEM_PADDING_DIP;

    public ViewParams(DisplayMetrics metrics) {
        this.mMetics = metrics;
    }

    public int getNumClumns() {
        return mNumClumns;
    }

    public void setNumClumns(int numClumns) {
        if (numClumns > 0) {
            this.mNumClumns = numClumns;
        } else {
            Log.w(TAG, numClumns + " numClumns is not supported");
        }
    }

    /**
     * @return the isViewModel
     */
    public boolean isViewOnlyModel() {
        return isViewOnlyModel;
    }

    /**
     * @param isViewModel the isViewModel to set
     */
    public void setViewOnlyModel(boolean isViewOnlyModel) {
        this.isViewOnlyModel = isViewOnlyModel;
    }

    /**
     * @return the isMutiPick
     */
    public boolean isMutiPick() {
        return isMutiPick;
    }

    /**
     * @param isMutiPick the isMutiPick to set
     */
    public void setMutiPick(boolean isMutiPick) {
        this.isMutiPick = isMutiPick;
    }

    /**
     * @return the mCheckBoxDrawable
     */
    public Drawable getCheckBoxDrawable() {
        return mCheckBoxDrawable;
    }

    /**
     * @param checkBoxDrawable the mCheckBoxDrawable to set
     */
    public void setCheckBoxDrawable(Drawable checkBoxDrawable) {
        this.mCheckBoxDrawable = checkBoxDrawable;
    }

    /**
     * @return the mTakePhotoDrawable
     */
    public Drawable getTakePhotoDrawable() {
        return mTakePhotoDrawable;
    }

    /**
     * @param takePhotoDrawable the mTakePhotoDrawable to set
     */
    public void setTakePhotoDrawable(Drawable takePhotoDrawable) {
        this.mTakePhotoDrawable = takePhotoDrawable;
    }

    /**
     * @return the mLoadingImageDrawable
     */
    public Drawable getLoadingImageDrawable() {
        return mLoadingImageDrawable;
    }

    /**
     * @param loadingImageDrawable the mLoadingImageDrawable to set
     */
    public void setLoadingImageDrawable(Drawable loadingImageDrawable) {
        this.mLoadingImageDrawable = loadingImageDrawable;
    }

    /**
     * @return the density of this device.
     */
    public float getDensity() {
        return mMetics.density;
    }

    /**
     * @return the mItemPaddingDip
     */
    public float getItemPaddingDip() {
        return mItemPaddingDip;
    }

    // /**
    // * @param itemPaddingDip the mItemPaddingDip to set
    // */
    // public void setItemPaddingDip(float itemPaddingDip) {
    // this.mItemPaddingDip = itemPaddingDip;
    // }

}
