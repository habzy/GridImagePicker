package com.jfeinstein.jazzyviewpager;

import java.util.ArrayList;

import com.habzy.image.models.ItemModel;
import com.habzy.image.tools.ImageTools;
import com.nostra13.universalimageloader.core.ImageLoader;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher.OnPhotoTapListener;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

public class JazzyPagerAdapter extends PagerAdapter {

    private ImageLoader mImageLoader;
    ArrayList<ItemModel> mModelList;
    private JazzyViewPager mJazzy;
    private PhotoViewListener mPhotoViewListener;

    public JazzyPagerAdapter(JazzyViewPager jazzy) {
        mImageLoader = ImageTools.getImageLoaderInstance(jazzy.getContext());
        mJazzy = jazzy;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        final PhotoView photoView = new PhotoView(container.getContext());
        photoView.setClickable(true);
        photoView.setOnPhotoTapListener(mOnPhotoTapListener);
        mImageLoader.displayImage(mModelList.get(position).mPath, photoView);
        container.addView(photoView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        mJazzy.setObjectForPosition(photoView, position);
        return photoView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object obj) {
        container.removeView(mJazzy.findViewFromObject(position));
    }

    @Override
    public int getCount() {
        return mModelList.size();
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public boolean isViewFromObject(View view, Object obj) {
        if (view instanceof OutlineContainer) {
            return ((OutlineContainer) view).getChildAt(0) == obj;
        } else {
            return view == obj;
        }
    }

    public void setImagePath(ArrayList<ItemModel> galleryPhotos) {
        mModelList = galleryPhotos;
    }

    /**
     * @param photoViewListener the mPhotoViewListener to set
     */
    public void setPhotoViewListener(PhotoViewListener photoViewListener) {
        this.mPhotoViewListener = photoViewListener;
    }

    private OnPhotoTapListener mOnPhotoTapListener = new OnPhotoTapListener() {

        @Override
        public void onPhotoTap(View arg0, float arg1, float arg2) {
            if (null != mPhotoViewListener) {
                mPhotoViewListener.onPhotoClicked();
            }
        }
    };
}
