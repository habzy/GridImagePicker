/*
 * Copyright 2014 Habzy Huang
 */
package com.habzy.image.viewpager.wrap;

import java.util.ArrayList;

import com.habzy.image.picker.GridItemModel;
import com.habzy.image.picker.R;
import com.jfeinstein.jazzyviewpager.JazzyViewPager;
import com.jfeinstein.jazzyviewpager.JazzyViewPager.TransitionEffect;

import android.annotation.SuppressLint;
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

public class ViewPagerDialogFragment extends DialogFragment {

    private static final String TAG = ViewPagerDialogFragment.class.getName();
    private JazzyViewPager mJazzy;
    private int mCurrentItem;
    private ArrayList<GridItemModel> mModelsList;
    private RelativeLayout mPagerTitleBar;
    private RelativeLayout mPagerBottomBar;
    private Button mBtnBack;
    private Button mBtnDone;
    private ImageView mCheckBox;

    public ViewPagerDialogFragment(ArrayList<GridItemModel> modelsList, int currentItem) {
        mModelsList = modelsList;
        mCurrentItem = currentItem;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "====== OnCreate.");
    }

    @SuppressLint("InflateParams")
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "====== onCreateView.");
        View view_pager = inflater.inflate(R.layout.view_pager, null);
        mJazzy = (JazzyViewPager) view_pager.findViewById(R.id.jazzy_pager);
        mPagerTitleBar = (RelativeLayout) view_pager.findViewById(R.id.pager_title_bar);
        mPagerBottomBar = (RelativeLayout) view_pager.findViewById(R.id.pager_bottom_bar);

        initViews();

        return view_pager;
    }

    private void initViews() {
        mJazzy.setTransitionEffect(TransitionEffect.Tablet);
        mJazzy.setImagePath(mModelsList);
        mJazzy.setCurrentItem(mCurrentItem);
        mJazzy.setPageMargin(0);

        mBtnBack = (Button) mPagerTitleBar.findViewById(R.id.picker_back);
        mBtnDone = (Button) mPagerTitleBar.findViewById(R.id.picker_done);
        mCheckBox = (ImageView) mPagerBottomBar.findViewById(R.id.focus_checkbox);

        mJazzy.setOnPageChangeListener(mOnPageChangeListener);
        mBtnBack.setOnClickListener(mOnBackClickListener);
        mBtnDone.setOnClickListener(mOnDoneClickListener);
        mCheckBox.setOnClickListener(mOnCheckBoxClickedListener);
    }

    private OnClickListener mOnBackClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            ViewPagerDialogFragment.this.dismiss();
        }
    };

    private OnClickListener mOnDoneClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            ViewPagerDialogFragment.this.dismiss();
        }
    };

    private OnClickListener mOnCheckBoxClickedListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            int position = mJazzy.getCurrentItem();
            boolean isSelected = mModelsList.get(position).isSeleted;
            mCheckBox.setSelected(!isSelected);
            mModelsList.get(position).isSeleted = !isSelected;
        }

    };

    private OnPageChangeListener mOnPageChangeListener = new OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            boolean isSelected = mModelsList.get(position).isSeleted;
            mCheckBox.setSelected(isSelected);
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {}

        @Override
        public void onPageScrollStateChanged(int arg0) {}
    };

}
