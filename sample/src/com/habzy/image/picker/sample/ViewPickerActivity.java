package com.habzy.image.picker.sample;

import java.util.ArrayList;

import com.habzy.image.picker.GridItemModel;
import com.habzy.image.picker.GridViewPicker;
import com.habzy.image.picker.ViewPickerListener;
import com.habzy.image.picker.ViewPickerParams;
import com.habzy.image.tools.ImageTools;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Window;
import android.widget.LinearLayout;

public class ViewPickerActivity extends FragmentActivity {
    public static final String ACTION_MULTIPLE_PICK =
            "com.habzy.image.picker.sample.ACTION_MULTIPLE_PICK";
    public static final String ACTION_PICK = "com.habzy.image.picker.sample.ACTION_PICK";

    public static final String PATH_STRING = "all_path";

    private GridViewPicker mImagePicker;
    private LinearLayout mLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.gallery);

        ViewPickerParams params = new ViewPickerParams(true);
        initParams(params);

        mLayout = (LinearLayout) findViewById(R.id.picker_layout);
        mImagePicker = new GridViewPicker(mLayout, params, mViewPickerListener);
        mImagePicker.initialize(getSupportFragmentManager());

        ArrayList<GridItemModel> modelList = new ArrayList<GridItemModel>();
        GridItemModel item = new GridItemModel();
        item.isCameraPhoto = true;
        modelList.add(item);
        modelList.addAll(ImageTools.getGalleryPhotos(getContentResolver()));
        mImagePicker.setImagePath(modelList);
    }

    private void initParams(ViewPickerParams params) {
        String action = getIntent().getAction();
        if (action == null) {
            finish();
        }
        if (action.equalsIgnoreCase(ViewPickerActivity.ACTION_MULTIPLE_PICK)) {
            params.setMutiPick(true);
        } else if (action.equalsIgnoreCase(ViewPickerActivity.ACTION_PICK)) {
            params.setMutiPick(false);
        }

        params.setReadOnly(false);
        params.setNumClumns(4);
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
    };
}
