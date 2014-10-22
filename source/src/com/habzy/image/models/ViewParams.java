/*
 * Copyright 2014 Habzy Huang
 */
package com.habzy.image.models;

import java.util.ArrayList;
import java.util.Random;

import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.util.Log;

public class ViewParams {

    public final static int DEFAULT_NUM_CLUMNS = 4;
    public final static float DEFAULT_ITEM_PADDING_DIP = 4;
    public final static int DEFAULT_MAX_PICK_SIZE = 9;

    public enum TransitionEffect {
        Standard,       //
        Tablet,         //
        CubeIn,         // There may be some bugs in the model, need to fix JazzyViewPager.
        CubeOut,        //
        FlipVertical,   //
        FlipHorizontal, //
        Stack,          //
        ZoomIn,         //
        ZoomOut,        //
        RotateUp,       //
        RotateDown,     //
        Accordion       // I like this one. -- Habzy (:
    }

    public enum ShownStyle {
        Pick_Multiple,   //
        Pick_Single,     //
        ViewOnly,        //
        ViewAndDelete    //
    }

    private static final String TAG = ViewParams.class.getSimpleName();
    private final static TransitionEffect DEFAULT_TRANSITION_EFFECT = TransitionEffect.Accordion;
    private final static ShownStyle DEFAULT_SHOWN_MODEL = ShownStyle.Pick_Multiple;

    private final DisplayMetrics mMetics;

    private int mNumClumns = DEFAULT_NUM_CLUMNS;

    private ShownStyle mShownStyle = DEFAULT_SHOWN_MODEL;
    private boolean mIsGridViewScrollEnable = true;

    private Drawable mCheckBoxDrawable = null;
    private Drawable mLoadingImageDrawable = null;
    private Drawable mLoadingFailedDrawable = null;
    private Drawable mDeleteItemDrawable = null;
    private Drawable mBtnBackDrawable = null;
    private Drawable mBtnDoneBgDrawable = null;
    private float mItemPaddingDip = DEFAULT_ITEM_PADDING_DIP;
    private ArrayList<TransitionEffect> mTransitionEffects =
            new ArrayList<ViewParams.TransitionEffect>();
    private String mTitleSt = null;
    private String mDoneSt = null;
    private int mMaxPickSize = DEFAULT_MAX_PICK_SIZE;
    private String mToastForReachingMax = null;
    private int mBarBgColorOpacity = -1;
    private int mBarBgColorClarity = -1;

    public ViewParams(DisplayMetrics metrics) {
        this.mMetics = metrics;
        mTransitionEffects.add(DEFAULT_TRANSITION_EFFECT);
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

    /**
     * @param transitionEffects copy values to the mTransitionEffects
     */
    public void setTransitionEffects(ArrayList<TransitionEffect> transitionEffects) {
        if (!transitionEffects.isEmpty()) {
            this.mTransitionEffects.clear();
            this.mTransitionEffects.addAll(transitionEffects);
        } else {
            Log.w(TAG, "Transition Effects can not be empty");
        }
    }

    /**
     * @return the random value in mTransitionEffects
     */
    public TransitionEffect getTransitionEffect() {
        int effectPosition = new Random().nextInt(mTransitionEffects.size());
        return mTransitionEffects.get(effectPosition);
    }

    /**
     * @return the mShownStyle
     */
    public ShownStyle getShownStyle() {
        return mShownStyle;
    }

    /**
     * @param shownStyle the mShownStyle to set
     */
    public void setShownStyle(ShownStyle shownStyle) {
        this.mShownStyle = shownStyle;
    }

    /**
     * @return the mDeleteItemDrawable
     */
    public Drawable getDeleteItemDrawable() {
        return mDeleteItemDrawable;
    }

    /**
     * @param deleteItemDrawable the mDeleteItemDrawable to set
     */
    public void setDeleteItemDrawable(Drawable deleteItemDrawable) {
        this.mDeleteItemDrawable = deleteItemDrawable;
    }

    public String getTitleSt() {
        return mTitleSt;
    }

    public void setTitleSt(String titleSt) {
        this.mTitleSt = titleSt;
    }

    public int getMaxPickSize() {
        return mMaxPickSize;
    }

    public void setMaxPickSize(int maxPickSize) {
        this.mMaxPickSize = maxPickSize;
    }

    public String getToastForReachingMax() {
        return mToastForReachingMax;
    }

    public void setToastForReachingMax(String toastForReachingMax) {
        this.mToastForReachingMax = toastForReachingMax;
    }

    public int getBarBgColorOpacity() {
        return mBarBgColorOpacity;
    }

    public void setBarBgColorOpacity(int barBgColorOpacity) {
        this.mBarBgColorOpacity = barBgColorOpacity;
    }

    /**
     * @return the mBarBgColorClarity
     */
    public int getBarBgColorClarity() {
        return mBarBgColorClarity;
    }

    /**
     * @param barBgColorClarity the mBarBgColorClarity to set
     */
    public void setBarBgColorClarity(int barBgColorClarity) {
        this.mBarBgColorClarity = barBgColorClarity;
    }

    /**
     * @return the mBtnBackDrawable
     */
    public Drawable getBtnBackDrawable() {
        return mBtnBackDrawable;
    }

    /**
     * @param btnBackDrawable the mBtnBackDrawable to set
     */
    public void setBtnBackDrawable(Drawable btnBackDrawable) {
        this.mBtnBackDrawable = btnBackDrawable;
    }

    /**
     * @return the mBtnDoneBgDrawable
     */
    public Drawable getBtnDoneBgDrawable() {
        return mBtnDoneBgDrawable;
    }

    /**
     * @param btnDoneBgDrawable the mBtnDoneBgDrawable to set
     */
    public void setBtnDoneBgDrawable(Drawable btnDoneBgDrawable) {
        this.mBtnDoneBgDrawable = btnDoneBgDrawable;
    }

    /**
     * @return the mDoneSt
     */
    public String getDoneSt() {
        return mDoneSt;
    }

    /**
     * @param doneSt the mDoneSt to set
     */
    public void setDoneSt(String doneSt) {
        this.mDoneSt = doneSt;
    }

    /**
     * @return the mIsGridViewScrollEnable
     */
    public boolean isGridViewScrollEnable() {
        return mIsGridViewScrollEnable;
    }

    /**
     * @param isGridViewScrollEnable the mIsGridViewScrollEnable to set
     */
    public void setGridViewScrollEnable(boolean isGridViewScrollEnable) {
        this.mIsGridViewScrollEnable = isGridViewScrollEnable;
    }

    public Drawable getLoadingFailedDrawable() {
        return mLoadingFailedDrawable;
    }

    public void setLoadingFailedDrawable(Drawable loadingFailedDrawable) {
        this.mLoadingFailedDrawable = loadingFailedDrawable;
    }

}
