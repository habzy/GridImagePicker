package com.habzy.image.picker.sample;

import java.util.ArrayList;

import com.habzy.image.picker.GridItemModel;
import com.habzy.image.picker.GridViewPicker;
import com.habzy.image.picker.ViewPickerListener;
import com.habzy.image.picker.ViewPickerParams;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;

public class MainActivity extends FragmentActivity {

    private LinearLayout mLayout;
    private Button mBtnGalleryPick;
    private Button mBtnGalleryPickMul;

    /*
     * The grid view to manage all images.
     */
    private GridViewPicker mImagePicker;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        ViewPickerParams params = new ViewPickerParams(true);
        initParams(params);

        mLayout = (LinearLayout) findViewById(R.id.shown_layout);
        mImagePicker = new GridViewPicker(mLayout, params, mViewPickerListener);
        mImagePicker.initialize(getSupportFragmentManager());
        init();
    }

    private void initParams(ViewPickerParams params) {
        params.setMutiPick(false);
        params.setReadOnly(true);
        params.setNumClumns(5);
    }


    private void init() {

        mBtnGalleryPick = (Button) findViewById(R.id.btnGalleryPick);
        mBtnGalleryPick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ViewPickerActivity.ACTION_PICK);
                startActivityForResult(i, 200);

            }
        });

        mBtnGalleryPickMul = (Button) findViewById(R.id.btnGalleryPickMul);
        mBtnGalleryPickMul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ViewPickerActivity.ACTION_MULTIPLE_PICK);
                startActivityForResult(i, 200);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            String[] paths = data.getStringArrayExtra(ViewPickerActivity.PATH_STRING);
            ArrayList<GridItemModel> dataT = new ArrayList<GridItemModel>();

            for (String string : paths) {
                GridItemModel item = new GridItemModel();
                item.mPath = string;
                dataT.add(item);
            }
            mImagePicker.setImagePath(dataT);
        }
    }

    ViewPickerListener mViewPickerListener = new ViewPickerListener() {

        @Override
        public void onDone(String[] paths) {}

        @Override
        public void onCanceled() {}
    };
}
