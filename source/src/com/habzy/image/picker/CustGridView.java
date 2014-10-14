/*
 * Copyright 2014 Habzy Huang
 */
package com.habzy.image.picker;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

public class CustGridView extends GridView {

    public CustGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public CustGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustGridView(Context context) {
        super(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

}
