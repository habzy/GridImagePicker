package com.habzy.image.picker.sample;

import java.util.ArrayList;

import com.habzy.image.picker.CustomGallery;
import com.habzy.image.picker.GalleryAdapter;
import com.habzy.image.tools.ImageTools;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ViewSwitcher;


public class MainActivity extends Activity {

    private GridView mGridGallery;
    private GalleryAdapter adapter;

    private ImageView imgSinglePick;
    private Button btnGalleryPick;
    private Button btnGalleryPickMul;

    private ViewSwitcher viewSwitcher;
    private ImageLoader imageLoader;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        imageLoader = ImageTools.getImageLoader(this);
        init();
    }

    private void init() {

        mGridGallery = (GridView) findViewById(R.id.gridGallery);
        mGridGallery.setFastScrollEnabled(true);
        adapter = new GalleryAdapter(getApplicationContext(), imageLoader, 5);
        adapter.setMultiplePick(false);
        mGridGallery.setAdapter(adapter);

        viewSwitcher = (ViewSwitcher) findViewById(R.id.viewSwitcher);
        viewSwitcher.setDisplayedChild(1);

        imgSinglePick = (ImageView) findViewById(R.id.imgSinglePick);

        btnGalleryPick = (Button) findViewById(R.id.btnGalleryPick);
        btnGalleryPick.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent i = new Intent(Action.ACTION_PICK);
                startActivityForResult(i, 100);

            }
        });

        btnGalleryPickMul = (Button) findViewById(R.id.btnGalleryPickMul);
        btnGalleryPickMul.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(Action.ACTION_MULTIPLE_PICK);
                startActivityForResult(i, 200);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            adapter.clear();

            viewSwitcher.setDisplayedChild(1);
            String single_path = data.getStringExtra("single_path");
            imageLoader.displayImage("file://" + single_path, imgSinglePick);

        } else if (requestCode == 200 && resultCode == Activity.RESULT_OK) {
            String[] all_path = data.getStringArrayExtra("all_path");

            ArrayList<CustomGallery> dataT = new ArrayList<CustomGallery>();

            for (String string : all_path) {
                CustomGallery item = new CustomGallery();
                item.mSdcardPath = string;

                dataT.add(item);
            }

            viewSwitcher.setDisplayedChild(0);
            adapter.addAll(dataT);
        }
    }
}
