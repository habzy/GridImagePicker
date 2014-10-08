/*
 * Copyright 2014 Habzy Huang
 */
package com.habzy.image.picker;

import java.util.ArrayList;

import com.habzy.image.models.ItemModel;
import com.habzy.image.models.ViewParams;
import com.habzy.image.tools.ImageTools;
import com.habzy.image.viewpager.wrap.ViewPagerListener;
import com.habzy.image.viewpager.wrap.ViewPagerDialogFragment;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;

import android.annotation.SuppressLint;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class GridViewPicker {

    private static final String TAG = GridViewPicker.class.getName();
    private LayoutInflater mInfalter;
    private ViewGroup mTitleBar;

    private View mImagePicker;
    private GridView mGridGallery;
    private GalleryAdapter mAdapter;
    private ImageView mImgNoMedia;

    private ViewParams mParams;

    private Handler mHandler;
    private Context mContext;

    private Button mBtnDone;
    private LinearLayout mParentLayout;
    private ViewPickerListener mListener;
    private Button mBtnBack;

    private FragmentManager mFragmentManager; // Required
    private ArrayList<ItemModel> mModelsList;

    /**
     * @param parentView
     */
    public GridViewPicker(LinearLayout parentView, ViewParams params, ViewPickerListener listener) {
        mParentLayout = parentView;
        mParams = params;
        mListener = listener;
        mContext = parentView.getContext();
        mHandler = new Handler();
    }

    @SuppressLint("InflateParams")
    public void initialize(FragmentManager manager) {
        mFragmentManager = manager;

        mInfalter = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mTitleBar = (ViewGroup) mInfalter.inflate(R.layout.title_bar, null);
        mImagePicker = (View) mInfalter.inflate(R.layout.image_picker, null);

        init();
        mParentLayout.addView(mTitleBar);
        mParentLayout.addView(mImagePicker);
        updateViews();
    }

    public void setImagePath(final ArrayList<ItemModel> modelsList) {
        mModelsList = modelsList;
        new Thread() {
            // TODO Move to thread pools.
            @Override
            public void run() {
                Looper.prepare();
                mHandler.post(new Runnable() {

                    @Override
                    public void run() {
                        mAdapter.addAll(modelsList);
                        checkImageStatus();
                    }
                });
                Looper.loop();
            };

        }.start();
    }

    private void init() {
        mGridGallery = (GridView) mImagePicker.findViewById(R.id.gridGallery);
        // mGridGallery.setFastScrollEnabled(true);
        mImgNoMedia = (ImageView) mImagePicker.findViewById(R.id.imgNoMedia);

        mBtnDone = (Button) mTitleBar.findViewById(R.id.picker_done);
        mBtnDone.setOnClickListener(mDoneClickListener);

        mBtnBack = (Button) mTitleBar.findViewById(R.id.picker_back);
        mBtnBack.setOnClickListener(mBackClickListener);
    }

    private void updateViews() {
        ImageLoader imageLoader = ImageTools.getImageLoader(mContext);

        mAdapter = new GalleryAdapter(mContext, imageLoader, mParams);
        PauseOnScrollListener listener = new PauseOnScrollListener(imageLoader, true, true);
        mGridGallery.setOnScrollListener(listener);
        mGridGallery.setOnItemClickListener(mItemClickListener);
        mGridGallery.setNumColumns(mParams.getNumClumns());

        if (mParams.isMutiPick()) {
            mTitleBar.setVisibility(View.VISIBLE);
        } else {
            mTitleBar.setVisibility(View.GONE);
        }

        mGridGallery.setAdapter(mAdapter);
    }

    private void checkImageStatus() {
        if (mAdapter.isEmpty()) {
            mImgNoMedia.setVisibility(View.VISIBLE);
        } else {
            mImgNoMedia.setVisibility(View.GONE);
        }
    }


    View.OnClickListener mDoneClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            ArrayList<ItemModel> selected = mAdapter.getSelected();

            String[] paths = new String[selected.size()];
            for (int i = 0; i < paths.length; i++) {
                paths[i] = selected.get(i).mPath;
            }
            mListener.onDone(paths);

        }
    };

    View.OnClickListener mBackClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            mListener.onCanceled();
        }
    };

    AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> l, View v, int position, long id) {
            if (mModelsList.get(position).isCameraPhoto) {
                Log.d(TAG, "======Wana to take photo.");
                return;
            }
            ViewPagerDialogFragment fragment =
                    new ViewPagerDialogFragment(mModelsList, mParams, position);
            fragment.setStyle(DialogFragment.STYLE_NO_TITLE, R.style.viewpager);
            fragment.setOnDismissListener(mViewPagerDismissListener);
            fragment.show(mFragmentManager, "viewpager");
        }
    };

    ViewPagerListener mViewPagerDismissListener = new ViewPagerListener() {

        @Override
        public void onStatusChanged(int currentPosition, boolean isSelected) {
            mAdapter.updateStatus(currentPosition, isSelected);
        }

        @Override
        public void onDone(int currentPosition) {
            if (mParams.isMutiPick()) {
                ArrayList<ItemModel> selected = mAdapter.getSelected();

                String[] paths = new String[selected.size()];
                for (int i = 0; i < paths.length; i++) {
                    paths[i] = selected.get(i).mPath;
                }
                mListener.onDone(paths);
            } else {
                String[] paths = new String[1];
                paths[0] = mModelsList.get(currentPosition).mPath;
                mListener.onDone(paths);
            }
        }
    };

}
