package com.habzy.image.picker.sample;

import java.util.ArrayList;

import com.habzy.image.models.ItemModel;
import com.habzy.image.models.ViewParams;
import com.habzy.image.models.ViewParams.ShownStyle;
import com.habzy.image.picker.GridViewPicker;
import com.habzy.image.picker.ViewPickerListener;
import com.habzy.image.tools.ImageTools;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Window;
import android.widget.LinearLayout;

public class ViewPickerActivity extends FragmentActivity {
    public static final String ACTION_MULTIPLE_PICK =
            "com.habzy.image.picker.sample.ACTION_MULTIPLE_PICK";
    public static final String ACTION_PICK = "com.habzy.image.picker.sample.ACTION_PICK";

    public static final String PATH_STRING = "all_path";
    private static final String TAG = ViewPickerActivity.class.getSimpleName();

    private GridViewPicker mImagePicker;
    private LinearLayout mLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.gallery);

        ViewParams params = new ViewParams(getResources().getDisplayMetrics());
        initParams(params);

        mLayout = (LinearLayout) findViewById(R.id.picker_layout);
        mImagePicker = new GridViewPicker(mLayout, params, mViewPickerListener);
        mImagePicker.initialize(getSupportFragmentManager());

        ArrayList<ItemModel> modelList = new ArrayList<ItemModel>();
        ItemModel item = new ItemModel();
        item.isCameraPhoto = true;
        modelList.add(item);
        modelList.addAll(ImageTools.getGalleryPhotos(getContentResolver()));
        mImagePicker.setImagePath(modelList);
    }

    private void initParams(ViewParams params) {
        String action = getIntent().getAction();
        if (action == null) {
            finish();
        }
        if (action.equalsIgnoreCase(ViewPickerActivity.ACTION_MULTIPLE_PICK)) {
            params.setShownStyle(ShownStyle.Pick_Multiple);
        } else if (action.equalsIgnoreCase(ViewPickerActivity.ACTION_PICK)) {
            params.setShownStyle(ShownStyle.Pick_Single);
        }

        params.setCheckBoxDrawable(getResources().getDrawable(R.drawable.on_1_checkbox));
        params.setTakePhotoDrawable(getResources().getDrawable(R.drawable.icon_take_photo));
        params.setLoadingImageDrawable(getResources().getDrawable(
                R.drawable.image_view_loading_default));
        params.setTitleSt(getResources().getString(R.string.pick_title));
    }

    ViewPickerListener mViewPickerListener = new ViewPickerListener() {

        @Override
        public void onDone(String[] paths) {
            Intent data = new Intent().putExtra(PATH_STRING, paths);
            setResult(RESULT_OK, data);
            finish();
        }

        @Override
        public void onCanceled() {
            finish();
        }

        @Override
        public void onTakeingPhoto() {
            Log.d(TAG, "======Wana to take photo.");
        }

    };

}
