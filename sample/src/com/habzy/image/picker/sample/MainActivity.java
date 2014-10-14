package com.habzy.image.picker.sample;

import java.util.ArrayList;

import com.habzy.image.models.ItemModel;
import com.habzy.image.models.ViewParams;
import com.habzy.image.models.ViewParams.ShownStyle;
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

    private LinearLayout mLayout1;
    private LinearLayout mLayout2;
    private LinearLayout mLayout3;
    private Button mBtnGalleryPick;
    private Button mBtnGalleryPickMul;
    private Button mBtnGalleryPickMul2;

    /*
     * The grid view to manage all images.
     */
    private GridViewPicker mImagePicker;
    /*
     * The grid view to manage all images.
     */
    private GridViewPicker mImagePicker2;

    /*
     * The grid view to manage all images.
     */
    private GridViewPicker mImagePicker3;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        ViewParams params = new ViewParams(getResources().getDisplayMetrics());
        initParams(params);

        mLayout1 = (LinearLayout) findViewById(R.id.shown_layout1);
        mImagePicker = new GridViewPicker(mLayout1, params, mViewPickerListener);
        mImagePicker.initialize(getSupportFragmentManager());

        mLayout2 = (LinearLayout) findViewById(R.id.shown_layout2);
        mImagePicker2 = new GridViewPicker(mLayout2, params, mViewPickerListener);
        mImagePicker2.initialize(getSupportFragmentManager());

        ViewParams params2 = new ViewParams(getResources().getDisplayMetrics());
        initDeltableParams(params2);
        mLayout3 = (LinearLayout) findViewById(R.id.shown_layout3);
        mImagePicker3 = new GridViewPicker(mLayout3, params2, mViewPickerListener);
        mImagePicker3.initialize(getSupportFragmentManager());
        init();
    }

    private void initParams(ViewParams params) {
        ArrayList<TransitionEffect> transitionEffects =
                new ArrayList<ViewParams.TransitionEffect>();
        transitionEffects.add(ViewParams.TransitionEffect.CubeOut);
        transitionEffects.add(ViewParams.TransitionEffect.FlipHorizontal);
        transitionEffects.add(ViewParams.TransitionEffect.FlipVertical);

        params.setTransitionEffects(transitionEffects);
        params.setShownStyle(ShownStyle.ViewOnly);
        params.setNumClumns(5);
        params.setLoadingImageDrawable(getResources().getDrawable(
                R.drawable.image_view_loading_default));
        params.setBtnBackDrawable(getResources().getDrawable(R.drawable.icon_1_back));
        params.setDeleteItemDrawable(getResources().getDrawable(R.drawable.icon_1_delete));
        params.setBarBgColorOpacity(getResources().getColor(R.color.bg_1_bar_opacity));
        params.setBarBgColorClarity(getResources().getColor(R.color.bg_1_bar_clarity));
    }

    private void initDeltableParams(ViewParams params) {
        ArrayList<TransitionEffect> transitionEffects =
                new ArrayList<ViewParams.TransitionEffect>();
        transitionEffects.add(ViewParams.TransitionEffect.CubeOut);
        transitionEffects.add(ViewParams.TransitionEffect.FlipHorizontal);
        transitionEffects.add(ViewParams.TransitionEffect.FlipVertical);

        params.setTransitionEffects(transitionEffects);
        params.setShownStyle(ShownStyle.ViewAndDelete);
        params.setNumClumns(4);
        params.setLoadingImageDrawable(getResources().getDrawable(
                R.drawable.image_view_loading_default));
        params.setBtnBackDrawable(getResources().getDrawable(R.drawable.icon_1_back));
        params.setDeleteItemDrawable(getResources().getDrawable(R.drawable.icon_1_delete));
        params.setBarBgColorOpacity(getResources().getColor(R.color.bg_1_bar_opacity));
        params.setBarBgColorClarity(getResources().getColor(R.color.bg_1_bar_clarity));
    }


    private void init() {

        mBtnGalleryPick = (Button) findViewById(R.id.btnGalleryPick);
        mBtnGalleryPick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ViewPickerActivity.ACTION_PICK);
                startActivityForResult(i, 100);

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

        mBtnGalleryPickMul2 = (Button) findViewById(R.id.btnGalleryPickMul2);
        mBtnGalleryPickMul2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ViewPickerActivity.ACTION_MULTIPLE_PICK);
                startActivityForResult(i, 300);
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
            if (requestCode == 100) {
                mImagePicker.setImagePath(dataT);
            } else if (requestCode == 200) {
                mImagePicker2.setImagePath(dataT);
            } else {
                mImagePicker3.setImagePath(dataT);
            }
        }
    }

    ViewPickerListener mViewPickerListener = new ViewPickerListener() {

        @Override
        public void onDone(String[] paths) {}

        @Override
        public void onCanceled() {}

        @Override
        public void onTakeingPhoto() {}

        @Override
        public void onImageChanged(String[] paths) {}
    };

}
