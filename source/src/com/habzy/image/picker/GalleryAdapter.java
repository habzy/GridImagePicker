package com.habzy.image.picker;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.habzy.image.models.ItemModel;
import com.habzy.image.models.ViewParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

public class GalleryAdapter extends BaseAdapter {

    private static final String TAG = GalleryAdapter.class.getName();

    private int mNumClumns = ViewParams.DEFAULT_NUM_CLUMNS;

    private ArrayList<ItemModel> data = new ArrayList<ItemModel>();
    private LayoutInflater mInfalter;
    private ImageLoader mImageLoader;

    private boolean isActionMultiplePick;
    private Drawable mCheckBoxDrawable = null;
    private Drawable mTakePhotoDrawable = null;

    public GalleryAdapter(Context context, ImageLoader imageLoader, int numClumns) {
        mInfalter = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mImageLoader = imageLoader;
        mNumClumns = numClumns;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public ItemModel getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setMultiplePick(boolean isMultiplePick) {
        this.isActionMultiplePick = isMultiplePick;
    }

    public void selectAll(boolean selection) {
        for (int i = 0; i < data.size(); i++) {
            data.get(i).isSeleted = selection;

        }
        notifyDataSetChanged();
    }

    public boolean isAllSelected() {
        boolean isAllSelected = true;

        for (int i = 0; i < data.size(); i++) {
            if (!data.get(i).isSeleted) {
                isAllSelected = false;
                break;
            }
        }

        return isAllSelected;
    }

    public boolean isAnySelected() {
        boolean isAnySelected = false;

        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).isSeleted) {
                isAnySelected = true;
                break;
            }
        }

        return isAnySelected;
    }

    public ArrayList<ItemModel> getSelected() {
        ArrayList<ItemModel> dataT = new ArrayList<ItemModel>();

        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).isSeleted) {
                dataT.add(data.get(i));
            }
        }

        return dataT;
    }

    public void addAll(ArrayList<ItemModel> files) {

        try {
            this.data.clear();
            this.data.addAll(files);

        } catch (Exception e) {
            e.printStackTrace();
        }

        notifyDataSetChanged();
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;
        if (convertView == null) {

            convertView = mInfalter.inflate(R.layout.gallery_item, null);
            holder = new ViewHolder();
            holder.imgQueue = (ImageView) convertView.findViewById(R.id.imgQueue);
            if (ViewParams.DEFAULT_NUM_CLUMNS != mNumClumns) {
                LayoutParams params = holder.imgQueue.getLayoutParams();
                params.height =
                        params.width = params.width * ViewParams.DEFAULT_NUM_CLUMNS / mNumClumns;
                holder.imgQueue.setLayoutParams(params);
            }

            holder.imgQueueMultiSelected =
                    (ImageView) convertView.findViewById(R.id.imgQueueMultiSelected);

            if (mCheckBoxDrawable != null) {
                Drawable cloneDrawable = mCheckBoxDrawable.getConstantState().newDrawable();
                holder.imgQueueMultiSelected.setImageDrawable(cloneDrawable);
            }
            if (isActionMultiplePick) {
                holder.imgQueueMultiSelected.setOnClickListener(mCheckboxListener);
                holder.imgQueueMultiSelected.setTag(position);
                holder.imgQueueMultiSelected.setVisibility(View.VISIBLE);
            } else {
                holder.imgQueueMultiSelected.setVisibility(View.GONE);
            }

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.imgQueue.setTag(position);
        mImageLoader.cancelDisplayTask(holder.imgQueue);
        try {
            if (data.get(position).isCameraPhoto) {
                holder.imgQueueMultiSelected.setVisibility(View.GONE);
                if (null != mTakePhotoDrawable) {
                    holder.imgQueue.setImageDrawable(mTakePhotoDrawable);
                } else {
                    holder.imgQueue.setImageResource(R.drawable.take_photo);
                }
            } else {
                mImageLoader.displayImage("file://" + data.get(position).mPath, holder.imgQueue,
                        new SimpleImageLoadingListener() {
                            @Override
                            public void onLoadingStarted(String imageUri, View view) {
                                holder.imgQueue.setImageResource(R.drawable.no_media);
                                super.onLoadingStarted(imageUri, view);
                            }
                        });

                if (isActionMultiplePick) {
                    holder.imgQueueMultiSelected.setSelected(data.get(position).isSeleted);
                }
            }

        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
        }

        return convertView;
    }

    public class ViewHolder {
        ImageView imgQueue;
        ImageView imgQueueMultiSelected;
    }

    public void clearCache() {
        mImageLoader.clearDiskCache();
        mImageLoader.clearMemoryCache();
    }

    public void clear() {
        data.clear();
        notifyDataSetChanged();
    }

    public void setNumColumns(int numClumns) {
        mNumClumns = numClumns;
    }

    private OnClickListener mCheckboxListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            int position = (Integer) v.getTag();
            if (data.get(position).isSeleted) {
                data.get(position).isSeleted = false;
            } else {
                data.get(position).isSeleted = true;
            }

            v.setSelected(data.get(position).isSeleted);
        }
    };

    public void setCheckBoxDrawable(Drawable checkBoxDrawable) {
        this.mCheckBoxDrawable = checkBoxDrawable;
    }

    /**
     * @param takePhotoDrawable the mTakePhotoDrawable to set
     */
    public void setTakePhotoDrawable(Drawable takePhotoDrawable) {
        this.mTakePhotoDrawable = takePhotoDrawable;
    }

    public void updateStatus(int currentPosition, boolean isSelected) {
        data.get(currentPosition).isSeleted = isSelected;
        notifyDataSetChanged();
    }
}
