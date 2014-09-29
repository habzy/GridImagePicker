package com.habzy.image.picker.sample;

import java.util.ArrayList;

import com.habzy.image.picker.GridItemModel;
import com.habzy.image.picker.GridViewPicker;
import com.habzy.image.picker.ViewPickerListener;
import com.habzy.image.picker.ViewPickerParams;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;

public class MainActivity extends Activity {

    private GridViewPicker mImagePicker;
    private LinearLayout mLayout;

    private Button btnGalleryPick;
    private Button btnGalleryPickMul;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        ViewPickerParams params = new ViewPickerParams(true);
        initParams(params);

        mLayout = (LinearLayout) findViewById(R.id.shown_layout);
        mImagePicker = new GridViewPicker(mLayout, params, mViewPickerListener);
        mImagePicker.initialize();

        init();
    }

    private void initParams(ViewPickerParams params) {
        params.setMutiPick(false);

        params.setReadOnly(true);
        params.setNumClumns(5);
    }


    private void init() {

        btnGalleryPick = (Button) findViewById(R.id.btnGalleryPick);
        btnGalleryPick.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent i = new Intent(ViewPickerActivity.ACTION_PICK);
                startActivityForResult(i, 100);

            }
        });

        btnGalleryPickMul = (Button) findViewById(R.id.btnGalleryPickMul);
        btnGalleryPickMul.setOnClickListener(new View.OnClickListener() {

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
            String[] all_path = data.getStringArrayExtra("all_path");

            ArrayList<GridItemModel> dataT = new ArrayList<GridItemModel>();

            for (String string : all_path) {
                GridItemModel item = new GridItemModel();
                item.mPath = string;

                dataT.add(item);
            }
            mImagePicker.setImagePath(dataT);
        }
    }

    ViewPickerListener mViewPickerListener = new ViewPickerListener() {

        @Override
        public void onDone(String[] paths) {
            // Intent data = new Intent().putExtra("all_path", paths);
            // setResult(RESULT_OK, data);
            // finish();
        }

        @Override
        public void onCanceled() {
            // finish();
        }
    };
}
