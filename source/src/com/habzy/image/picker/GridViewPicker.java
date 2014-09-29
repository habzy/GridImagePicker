/**
 *
 * Copyright habzy
 *
 */
package com.habzy.image.picker;

import java.util.ArrayList;

import com.habzy.image.tools.ImageTools;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class GridViewPicker {

    private LayoutInflater mInfalter;
    private ViewGroup mTitleBar;

    private View mImagePicker;
    private GridView mGridGallery;
    private GalleryAdapter mAdapter;
    private ImageView mImgNoMedia;

    private ViewPickerParams mParams;

    private Handler mHandler;
    private Context mContext;

    private Button mBtnDone;
    private LinearLayout mParentLayout;
    private ViewPickerListener mListener;
    private Button mBtnBack;

    /**
     * @param parentView
     */
    public GridViewPicker(LinearLayout parentView, ViewPickerParams params,
            ViewPickerListener listener) {
        mParentLayout = parentView;
        mParams = params;
        mListener = listener;
        mContext = parentView.getContext();
        mHandler = new Handler();
    }

    @SuppressLint("InflateParams")
    public void initialize() {
        mInfalter = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mTitleBar = (ViewGroup) mInfalter.inflate(R.layout.title_bar, null);
        mImagePicker = (View) mInfalter.inflate(R.layout.image_picker, null);

        init();
        mParentLayout.addView(mTitleBar);
        mParentLayout.addView(mImagePicker);
        updateViews();
    }

    public void setImagePath(final ArrayList<GridItemModel> modelsList) {
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
        mGridGallery.setFastScrollEnabled(true);
        mImgNoMedia = (ImageView) mImagePicker.findViewById(R.id.imgNoMedia);

        ImageLoader imageLoader = ImageTools.getImageLoader(mContext);
        mAdapter = new GalleryAdapter(mContext, imageLoader, mParams.getNumClumns());
        PauseOnScrollListener listener = new PauseOnScrollListener(imageLoader, true, true);
        mGridGallery.setOnScrollListener(listener);
        mGridGallery.setAdapter(mAdapter);

        mBtnDone = (Button) mTitleBar.findViewById(R.id.picker_done);
        mBtnDone.setOnClickListener(mDoneClickListener);

        mBtnBack = (Button) mTitleBar.findViewById(R.id.picker_back);
        mBtnBack.setOnClickListener(mBackClickListener);
    }

    private void updateViews() {
        mGridGallery.setNumColumns(mParams.getNumClumns());
        mAdapter.setNumColumns(mParams.getNumClumns());

        if (mParams.isMutiPick()) {
            mTitleBar.setVisibility(View.VISIBLE);
            mGridGallery.setOnItemClickListener(mItemMulClickListener);
            mAdapter.setMultiplePick(true);
        } else {
            mTitleBar.setVisibility(View.GONE);
            mGridGallery.setOnItemClickListener(mItemSingleClickListener);
            mAdapter.setMultiplePick(false);
        }
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
            ArrayList<GridItemModel> selected = mAdapter.getSelected();

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



    AdapterView.OnItemClickListener mItemMulClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> l, View v, int position, long id) {
            mAdapter.changeSelection(v, position);
        }
    };

    AdapterView.OnItemClickListener mItemSingleClickListener =
            new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> l, View v, int position, long id) {
                    GridItemModel item = mAdapter.getItem(position);
                    String[] paths = new String[1];
                    paths[0] = item.mPath;
                    mListener.onDone(paths);
                }
            };


}
