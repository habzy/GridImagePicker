/*
 * Copyright 2014 Habzy Huang
 */
package com.habzy.image.viewpager.wrap;

public interface ViewPagerListener {
    void onStatusChanged(int currentPosition, boolean isSelected);

    void onDone(int currentPosition);
}
