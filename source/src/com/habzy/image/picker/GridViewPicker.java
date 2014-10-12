/*
 * Copyright 2014 Habzy Huang
 */
package com.habzy.image.picker;

import java.util.ArrayList;

import com.habzy.image.models.ItemModel;
import com.habzy.image.models.ViewParams;
import com.habzy.image.models.ViewParams.ShownStyle;
import com.habzy.image.tools.ImageTools;
import com.habzy.image.viewpager.wrap.ViewPagerListener;
import com.habzy.image.viewpager.wrap.ViewPagerDialogFragment;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class GridViewPicker {

    protected static final String TAG = GridViewPicker.class.getName();
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
    private ImageLoader mImageLoader;
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
                        if (checkImageStatus()) {
                            mAdapter.clear();
                        } else {
                            mAdapter.addAll(mModelsList);
                        }
                    }
                });
                Looper.loop();
            };

        }.start();
    }

    private void init() {
        mGridGallery = (GridView) mImagePicker.findViewById(R.id.gridGallery);
        mImgNoMedia = (ImageView) mImagePicker.findViewById(R.id.imgNoMedia);

        mBtnDone = (Button) mTitleBar.findViewById(R.id.picker_done);
        mBtnDone.setOnClickListener(mDoneClickListener);

        mBtnBack = (Button) mTitleBar.findViewById(R.id.picker_back);
        mBtnBack.setOnClickListener(mBackClickListener);
    }

    private void updateViews() {
        mImageLoader = ImageTools.getImageLoader(mContext);

        mAdapter = new GalleryAdapter(mContext, mImageLoader, mParams);
        PauseOnScrollListener listener = new PauseOnScrollListener(mImageLoader, true, true);
        mGridGallery.setOnScrollListener(listener);
        mGridGallery.setOnItemClickListener(mItemClickListener);
        mGridGallery.setNumColumns(mParams.getNumClumns());

        if (mParams.getShownStyle() != ShownStyle.Pick_Multiple) {
            mGridGallery.setFastScrollEnabled(true);
            mTitleBar.setVisibility(View.GONE);
        } else {
            mTitleBar.setVisibility(View.VISIBLE);
            if (null != mParams.getTitleSt()) {
                TextView title = (TextView) (mTitleBar.findViewById(R.id.picker_title));
                title.setText(mParams.getTitleSt());
            }
            if (-1 != mParams.getBarBgColorOpacity()) {
                mTitleBar.setBackgroundColor(mParams.getBarBgColorOpacity());
            } else {
                mTitleBar.setBackgroundResource(R.color.bg_bar_opacity);
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
        }

        mGridGallery.setAdapter(mAdapter);
    }

    private boolean checkImageStatus() {
        boolean result = false;
        switch (mModelsList.size()) {
            case 1:
                if (ShownStyle.ViewOnly == mParams.getShownStyle()) {
                    result = true;
                    mImgNoMedia.setVisibility(View.VISIBLE);
                    mImageLoader.displayImage("file://" + mModelsList.get(0).mPath, mImgNoMedia,
                            new SimpleImageLoadingListener() {
                                @Override
                                public void onLoadingStarted(String imageUri, View view) {
                                    if (null != mParams.getLoadingImageDrawable()) {
                                        mImgNoMedia.setImageDrawable(mParams
                                                .getLoadingImageDrawable());
                                    } else {
                                        mImgNoMedia.setImageResource(R.drawable.no_media);
                                    }
                                    super.onLoadingStarted(imageUri, view);
                                }
                            });
                    mImgNoMedia.setClickable(true);
                    mImgNoMedia.setOnClickListener(mOnSingleImageClickListener);
                }
                break;
            case 0:
                result = true;
                mImgNoMedia.setVisibility(View.VISIBLE);
                mImgNoMedia.setImageResource(R.drawable.no_media);
                mImgNoMedia.setClickable(false);
                break;
            default:
                break;
        }
        if (!result) {
            mImgNoMedia.setVisibility(View.GONE);
        }
        return result;
    }

    private void setFullScreen() {
        ((Activity) mContext).getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    private void quitFullScreen() {
        final WindowManager.LayoutParams attrs = ((Activity) mContext).getWindow().getAttributes();
        attrs.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ((Activity) mContext).getWindow().setAttributes(attrs);
        ((Activity) mContext).getWindow().clearFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
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
                mListener.onTakeingPhoto();
                return;
            }
            showPagerView(position);
        }
    };

    private OnClickListener mOnSingleImageClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            showPagerView(mModelsList.size());
        }
    };

    private void showPagerView(int position) {
        setFullScreen();
        ViewPagerDialogFragment fragment =
                new ViewPagerDialogFragment(mModelsList, mParams, position);
        fragment.setStyle(DialogFragment.STYLE_NO_TITLE, R.style.viewpager);
        fragment.setOnEventListener(mViewPagerListener);
        fragment.show(mFragmentManager, "viewpager");
    }

    ViewPagerListener mViewPagerListener = new ViewPagerListener() {

        @Override
        public void onDone(int currentPosition) {
            if (mParams.getShownStyle() == ShownStyle.Pick_Multiple) {
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

        @Override
        public void onDismiss() {
            quitFullScreen();
            switch (mParams.getShownStyle()) {
                case Pick_Multiple:
                    mAdapter.notifyDataSetChanged();
                    break;
                case ViewAndDelete:
                    if (mModelsList.size() != mAdapter.getCount()) {
                        mAdapter.addAll(mModelsList);
                    }
                    break;
                default:
                    break;
            }
        }
    };

}
