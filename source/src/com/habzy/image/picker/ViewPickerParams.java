/**
 * 
 * Copyright habzy
 * 
 */
package com.habzy.image.picker;

public class ViewPickerParams {

    private final static int DEFAULT_NUM_CLUMNS = 3;
    private int mNumClumns = DEFAULT_NUM_CLUMNS;
    private boolean isMutiPick = true;
    private boolean isReadOnly = false;

    public ViewPickerParams(boolean isMutiPick) {
        this.isMutiPick = isMutiPick;
    }

    public int getNumClumns() {
        return mNumClumns;
    }

    public void setNumClumns(int numClumns) {
        this.mNumClumns = numClumns;
    }

    /**
     * @return the mReadOnly
     */
    public boolean isReadOnly() {
        return isReadOnly;
    }

    /**
     * @param mReadOnly the mReadOnly to set
     */
    public void setReadOnly(boolean isReadOnly) {
        this.isReadOnly = isReadOnly;
    }

    /**
     * @return the isMutiPick
     */
    public boolean isMutiPick() {
        return isMutiPick;
    }

    /**
     * @param isMutiPick the isMutiPick to set
     */
    public void setMutiPick(boolean isMutiPick) {
        this.isMutiPick = isMutiPick;
    }

}
