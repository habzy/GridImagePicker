/**
 * 
 * Copyright habzy
 * 
 */
package com.habzy.image.picker;

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


    private boolean isMultiPicker = true;

    private Handler mHandler;
    private Context mContext;

    private Button mBtnDone;
    private LinearLayout mLayoutParent;

    /**
     * @param context
     */
    public GridViewPicker(Context context, LinearLayout parent) {
        mContext = context;
        mLayoutParent = parent;
        mHandler = new Handler();

    }

    @SuppressLint("InflateParams")
    public void initialize() {
        mInfalter = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mTitleBar = (ViewGroup) mInfalter.inflate(R.layout.title_bar, null);
        mImagePicker = (View) mInfalter.inflate(R.layout.image_picker, null);

        init();
        mLayoutParent.addView(mTitleBar);
        mLayoutParent.addView(mImagePicker);
        updateViews();
    }

    private void init() {
        mGridGallery = (GridView) mImagePicker.findViewById(R.id.gridGallery);
        mGridGallery.setFastScrollEnabled(true);
        mImgNoMedia = (ImageView) mImagePicker.findViewById(R.id.imgNoMedia);

        ImageLoader imageLoader = ImageTools.getImageLoader(mContext);
        mAdapter = new GalleryAdapter(mContext, imageLoader, 4);
        PauseOnScrollListener listener = new PauseOnScrollListener(imageLoader, true, true);
        mGridGallery.setOnScrollListener(listener);
        mGridGallery.setAdapter(mAdapter);

        mBtnDone = (Button) mTitleBar.findViewById(R.id.picker_done);
        mBtnDone.setOnClickListener(mDoneClickListener);

        new Thread() {

            @Override
            public void run() {
                Looper.prepare();
                mHandler.post(new Runnable() {

                    @Override
                    public void run() {
                        mAdapter.addAll(ImageTools.getGalleryPhotos(mContext.getContentResolver()));
                        checkImageStatus();
                    }
                });
                Looper.loop();
            };

        }.start();
    }

    private void updateViews() {
        // mSwitcher.setDisplayedChild(1);

        if (isMultiPicker) {
            // mTitleBar.setVisibility(View.VISIBLE);
            // mGridGallery.setOnItemClickListener(mItemMulClickListener);
            mAdapter.setMultiplePick(true);
        } else {
            // mTitleBar.setVisibility(View.GONE);
            // mGridGallery.setOnItemClickListener(mItemSingleClickListener);
            mAdapter.setMultiplePick(false);
        }
    }


    public boolean isMultiPicker() {
        return isMultiPicker;
    }

    public void setMultiPicker(boolean isMultiPicker) {
        this.isMultiPicker = isMultiPicker;
        updateViews();
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
            // ArrayList<GridItemModel> selected = adapter.getSelected();
            //
            // String[] allPath = new String[selected.size()];
            // for (int i = 0; i < allPath.length; i++) {
            // allPath[i] = selected.get(i).mPath;
            // }

            // Intent data = new Intent().putExtra("all_path", allPath);
            // setResult(RESULT_OK, data);
            // finish();

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
                    // GridItemModel item = adapter.getItem(position);
                    // Intent data = new Intent().putExtra("single_path", item.mPath);
                    // setResult(RESULT_OK, data);
                    // finish();
                }
            };


}
