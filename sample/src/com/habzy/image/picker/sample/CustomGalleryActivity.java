package com.habzy.image.picker.sample;

import com.habzy.image.picker.GridViewPicker;
import com.habzy.image.picker.ViewPickerListener;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.LinearLayout;

public class CustomGalleryActivity extends Activity {

    private GridViewPicker mImagePicker;
    private LinearLayout mLayout;

    // private String action;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.gallery);

        // action = getIntent().getAction();
        // if (action == null) {
        // finish();
        // }

        mLayout = (LinearLayout) findViewById(R.id.picker_layout);
        mImagePicker = new GridViewPicker(mLayout, mViewPickerListener);
        mImagePicker.initialize();
    }

    ViewPickerListener mViewPickerListener = new ViewPickerListener() {

        @Override
        public void onDone(String[] paths) {
            Intent data = new Intent().putExtra("all_path", paths);
            setResult(RESULT_OK, data);
            finish();
        }

        @Override
        public void onCanceled() {
            finish();
        }
    };
}
