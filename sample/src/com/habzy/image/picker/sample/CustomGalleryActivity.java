package com.habzy.image.picker.sample;

import com.habzy.image.picker.GridViewPicker;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.LinearLayout;

public class CustomGalleryActivity extends Activity {

    private GridViewPicker mImagePicker;
    private LinearLayout mLayout;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.gallery);
        mLayout = (LinearLayout) findViewById(R.id.picker_layout);
        mImagePicker = new GridViewPicker(mLayout);
        mImagePicker.initialize();
    }
}
