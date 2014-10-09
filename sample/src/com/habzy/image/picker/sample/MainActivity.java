package com.habzy.image.picker.sample;

import java.util.ArrayList;

import com.habzy.image.models.ItemModel;
import com.habzy.image.models.ViewParams;
import com.habzy.image.models.ViewParams.TransitionEffect;
import com.habzy.image.picker.GridViewPicker;
import com.habzy.image.picker.ViewPickerListener;

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

        ViewParams params = new ViewParams(getResources().getDisplayMetrics());
        initParams(params);

        mLayout = (LinearLayout) findViewById(R.id.shown_layout);
        mImagePicker = new GridViewPicker(mLayout, params, mViewPickerListener);
        mImagePicker.initialize(getSupportFragmentManager());
        init();
    }

    private void initParams(ViewParams params) {
        ArrayList<TransitionEffect> transitionEffects =
                new ArrayList<ViewParams.TransitionEffect>();
        transitionEffects.add(ViewParams.TransitionEffect.CubeOut);
        transitionEffects.add(ViewParams.TransitionEffect.FlipHorizontal);
        transitionEffects.add(ViewParams.TransitionEffect.FlipVertical);

        params.setTransitionEffects(transitionEffects);
        params.setMutiPick(false);
        params.setViewOnlyModel(true);
        params.setNumClumns(5);
        params.setLoadingImageDrawable(getResources().getDrawable(
                R.drawable.image_view_loading_default));
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
            ArrayList<ItemModel> dataT = new ArrayList<ItemModel>();

            for (String string : paths) {
                ItemModel item = new ItemModel();
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
