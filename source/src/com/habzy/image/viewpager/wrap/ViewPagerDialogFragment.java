/*
 * Copyright 2014 Habzy Huang
 */
package com.habzy.image.viewpager.wrap;

import java.util.ArrayList;

import com.habzy.image.models.ItemModel;
import com.habzy.image.models.ViewParams;
import com.habzy.image.picker.R;
import com.jfeinstein.jazzyviewpager.JazzyViewPager;
import com.jfeinstein.jazzyviewpager.PhotoViewListener;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class ViewPagerDialogFragment extends DialogFragment {

    private static final String TAG = ViewPagerDialogFragment.class.getSimpleName();
    private JazzyViewPager mJazzy;
    private int mCurrentItem;
    private ArrayList<ItemModel> mModelsList;
    private ViewPagerListener mViewPagerEventListener;
    private RelativeLayout mPagerTitleBar;
    private RelativeLayout mPagerBottomBar;
    private Button mBtnBack;
    private Button mBtnDone;
    private ImageView mBottonIcon;
    private ViewParams mParams;
    private boolean isFullScreen = false;

    public ViewPagerDialogFragment(ArrayList<ItemModel> modelsList, ViewParams params,
            int currentItem) {
        mModelsList = modelsList;
        mCurrentItem = currentItem;
        mParams = params;
    }

    @SuppressLint("InflateParams")
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view_pager = inflater.inflate(R.layout.view_pager, null);
        mJazzy = (JazzyViewPager) view_pager.findViewById(R.id.jazzy_pager);
        mPagerTitleBar = (RelativeLayout) view_pager.findViewById(R.id.pager_title_bar);
        mPagerBottomBar = (RelativeLayout) view_pager.findViewById(R.id.pager_bottom_bar);

        initViews();

        return view_pager;
    }

    private void initViews() {
        mJazzy.setImagePath(mModelsList);
        mJazzy.setCurrentItem(mCurrentItem);
        mJazzy.setPageMargin(0);

        mBtnBack = (Button) mPagerTitleBar.findViewById(R.id.picker_back);
        mBtnDone = (Button) mPagerTitleBar.findViewById(R.id.picker_done);
        mBottonIcon = (ImageView) mPagerBottomBar.findViewById(R.id.bottom_icon);

        mJazzy.setOnPageChangeListener(mOnPageChangeListener);
        mJazzy.setPhotoViewListener(mPhotoViewListener);
        mBtnBack.setOnClickListener(mOnBackClickListener);
        mBtnDone.setOnClickListener(mOnDoneClickListener);

        mJazzy.setTransitionEffect(mParams.getTransitionEffect());
        if (-1 != mParams.getBarBgColorClarity()) {
            mPagerTitleBar.setBackgroundColor(mParams.getBarBgColorClarity());
            mPagerBottomBar.setBackgroundColor(mParams.getBarBgColorClarity());
        } else {
            mPagerTitleBar.setBackgroundResource(R.color.bg_bar_clarity);
            mPagerBottomBar.setBackgroundResource(R.color.bg_bar_clarity);
        }

        if (null != mParams.getBtnBackDrawable()) {
            mBtnBack.setBackgroundDrawable(mParams.getBtnBackDrawable());
        } else {
            mBtnBack.setBackgroundResource(R.drawable.icon_back);
        }
        if (null != mParams.getBtnDoneBgDrawable()) {
            mBtnDone.setBackgroundDrawable(mParams.getBtnDoneBgDrawable());
        } else {
            mBtnDone.setBackgroundResource(R.color.clarity);
        }

        switch (mParams.getShownStyle()) {
            case Pick_Multiple:
                mBottonIcon.setSelected(mModelsList.get(mCurrentItem).isSeleted);
                mBottonIcon.setOnClickListener(mOnCheckBoxClickedListener);
                if (mParams.getCheckBoxDrawable() != null) {
                    mBottonIcon.setImageDrawable(mParams.getCheckBoxDrawable());
                }
                mPagerBottomBar.setVisibility(View.VISIBLE);
                break;
            case Pick_Single:
                mPagerBottomBar.setVisibility(View.GONE);
                break;
            case ViewOnly:
                mBtnDone.setVisibility(View.GONE);
                mPagerBottomBar.setVisibility(View.GONE);
                break;
            case ViewAndDelete:
                mBtnDone.setVisibility(View.GONE);
                mBottonIcon.setImageResource(R.drawable.icon_delete);
                mBottonIcon.setOnClickListener(mOnDeleteClickedListener);
                if (mParams.getDeleteItemDrawable() != null) {
                    mBottonIcon.setImageDrawable(mParams.getDeleteItemDrawable());
                }
                mPagerBottomBar.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }

    private void fullScreen() {
        isFullScreen = !isFullScreen;
        if (isFullScreen) {
            mPagerTitleBar.setVisibility(View.GONE);
            mPagerBottomBar.setVisibility(View.GONE);
        } else {
            mPagerTitleBar.setVisibility(View.VISIBLE);
            switch (mParams.getShownStyle()) {
                case Pick_Multiple:
                    mPagerBottomBar.setVisibility(View.VISIBLE);
                    break;
                case Pick_Single:
                    mPagerBottomBar.setVisibility(View.GONE);
                    break;
                case ViewOnly:
                    mPagerBottomBar.setVisibility(View.GONE);
                    break;
                case ViewAndDelete:
                    mPagerBottomBar.setVisibility(View.VISIBLE);
                    break;
                default:
                    break;
            }
        }
    }

    private OnClickListener mOnBackClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            mViewPagerEventListener.onDismiss();
            ViewPagerDialogFragment.this.dismiss();
        }
    };

    private OnClickListener mOnDoneClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            mViewPagerEventListener.onDone(mCurrentItem);
            ViewPagerDialogFragment.this.dismiss();
        }
    };

    private OnClickListener mOnCheckBoxClickedListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            int position = mJazzy.getCurrentItem();
            boolean isSelected = mModelsList.get(position).isSeleted;
            if (!isSelected && getSelected() >= mParams.getMaxPickSize()) {
                if (null != mParams.getToastForReachingMax()) {
                    Toast.makeText(v.getContext(), mParams.getToastForReachingMax(),
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(v.getContext(), R.string.reached_max_size, Toast.LENGTH_SHORT)
                            .show();
                }
                return;
            }
            mBottonIcon.setSelected(!isSelected);
            mModelsList.get(position).isSeleted = !isSelected;
        }

    };

    private int getSelected() {
        int size = 0;
        for (int i = 0; i < mModelsList.size(); i++) {
            if (mModelsList.get(i).isSeleted) {
                size = size + 1;
            }
        }
        return size;
    }

    private OnClickListener mOnDeleteClickedListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            int position = mJazzy.getCurrentItem();
            if (mModelsList.size() == 1) {
                mModelsList.remove(position);
                mBtnBack.performClick();
            } else {
                mModelsList.remove(position);
                mJazzy.notifyChange();
            }
        }

    };

    private OnPageChangeListener mOnPageChangeListener = new OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            mCurrentItem = position;
            boolean isSelected = mModelsList.get(position).isSeleted;
            mBottonIcon.setSelected(isSelected);
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {}

        @Override
        public void onPageScrollStateChanged(int arg0) {}
    };

    private PhotoViewListener mPhotoViewListener = new PhotoViewListener() {

        @Override
        public void onPhotoClicked() {
            Log.d(TAG, "=======FullScreen");
            fullScreen();
        }
    };

    public void setOnEventListener(ViewPagerListener viewPagerEventListener) {
        mViewPagerEventListener = viewPagerEventListener;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        mViewPagerEventListener.onDismiss();
        super.onDismiss(dialog);
    }

}
