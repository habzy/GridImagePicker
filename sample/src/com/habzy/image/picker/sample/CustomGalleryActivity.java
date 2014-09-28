package com.habzy.image.picker.sample;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;

import com.habzy.image.picker.GridItemModel;
import com.habzy.image.picker.GalleryAdapter;
import com.habzy.image.tools.ImageTools;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;

public class CustomGalleryActivity extends Activity {

    GridView mGridGallery;
    Handler handler;
    GalleryAdapter adapter;

    ImageView mImgNoMedia;
    Button btnGalleryOk;

    String action;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.gallery);

        action = getIntent().getAction();
        if (action == null) {
            finish();
        }
        init();
    }

    private void init() {

        handler = new Handler();
        mGridGallery = (GridView) findViewById(R.id.gridGallery);
        mGridGallery.setFastScrollEnabled(true);

        ImageLoader imageLoader = ImageTools.getImageLoader(this);
        adapter = new GalleryAdapter(getApplicationContext(), imageLoader, 4);
        PauseOnScrollListener listener = new PauseOnScrollListener(imageLoader, true, true);
        mGridGallery.setOnScrollListener(listener);

        if (action.equalsIgnoreCase(Action.ACTION_MULTIPLE_PICK)) {

            findViewById(R.id.llBottomContainer).setVisibility(View.VISIBLE);
            mGridGallery.setOnItemClickListener(mItemMulClickListener);
            adapter.setMultiplePick(true);

        } else if (action.equalsIgnoreCase(Action.ACTION_PICK)) {

            findViewById(R.id.llBottomContainer).setVisibility(View.GONE);
            mGridGallery.setOnItemClickListener(mItemSingleClickListener);
            adapter.setMultiplePick(false);

        }

        mGridGallery.setAdapter(adapter);
        mImgNoMedia = (ImageView) findViewById(R.id.imgNoMedia);

        btnGalleryOk = (Button) findViewById(R.id.btnGalleryOk);
        btnGalleryOk.setOnClickListener(mOkClickListener);

        new Thread() {

            @Override
            public void run() {
                Looper.prepare();
                handler.post(new Runnable() {

                    @Override
                    public void run() {
                        adapter.addAll(ImageTools.getGalleryPhotos(CustomGalleryActivity.this
                                .getContentResolver()));
                        checkImageStatus();
                    }
                });
                Looper.loop();
            };

        }.start();

    }

    private void checkImageStatus() {
        if (adapter.isEmpty()) {
            mImgNoMedia.setVisibility(View.VISIBLE);
        } else {
            mImgNoMedia.setVisibility(View.GONE);
        }
    }

    View.OnClickListener mOkClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            ArrayList<GridItemModel> selected = adapter.getSelected();

            String[] allPath = new String[selected.size()];
            for (int i = 0; i < allPath.length; i++) {
                allPath[i] = selected.get(i).mPath;
            }

            Intent data = new Intent().putExtra("all_path", allPath);
            setResult(RESULT_OK, data);
            finish();

        }
    };
    AdapterView.OnItemClickListener mItemMulClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> l, View v, int position, long id) {
            adapter.changeSelection(v, position);

        }
    };

    AdapterView.OnItemClickListener mItemSingleClickListener =
            new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> l, View v, int position, long id) {
                    GridItemModel item = adapter.getItem(position);
                    Intent data = new Intent().putExtra("single_path", item.mPath);
                    setResult(RESULT_OK, data);
                    finish();
                }
            };

}
