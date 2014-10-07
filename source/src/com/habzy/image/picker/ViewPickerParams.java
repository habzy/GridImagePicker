/*
 * Copyright 2014 Habzy Huang
 */
package com.habzy.image.picker;

import android.graphics.drawable.Drawable;

public class ViewPickerParams {

    private final static int DEFAULT_NUM_CLUMNS = 3;
    private int mNumClumns = DEFAULT_NUM_CLUMNS;
    private boolean isMutiPick = true;
    private boolean isViewOnlyModel = false;
    private Drawable mCheckBoxDrawable = null;

    public ViewPickerParams(boolean isMutiPick) {
        this.isMutiPick = isMutiPick;
    }

    public int getNumClumns() {
        return mNumClumns;
    }

    public void setNumClumns(int numClumns) {
        this.mNumClumns = numClumns;
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

}
