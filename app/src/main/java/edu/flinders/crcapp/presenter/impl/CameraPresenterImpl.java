/*
 *  Created by Tri Phan on 7/01/19 10:22 PM
 *  Copyright (c) 2019 . All rights reserved.
 *  Last modified 7/01/19 10:22 PM
 */

package edu.flinders.crcapp.presenter.impl;

import android.graphics.Bitmap;
import android.media.Image;
import android.media.ImageReader;
import android.support.annotation.NonNull;

import edu.flinders.crcapp.presenter.CameraPresenter;
import edu.flinders.crcapp.view.CameraView;
import edu.flinders.crcapp.interactor.CameraInteractor;

import javax.inject.Inject;

public final class CameraPresenterImpl extends BasePresenterImpl<CameraView> implements CameraPresenter {
    /**
     * The interactor
     */
    @NonNull
    private final CameraInteractor mInteractor;

    // The view is available using the mView variable

    @Inject
    public CameraPresenterImpl(@NonNull CameraInteractor interactor) {
        mInteractor = interactor;
    }

    @Override
    public void onStart(boolean viewCreated) {
        super.onStart(viewCreated);

        // Your code here. Your view is available using mView and will not be null until next onStop()
    }

    @Override
    public void onStop() {
        // Your code here, mView will be null after this method until next onStart()

        super.onStop();
    }

    @Override
    public void onPresenterDestroyed() {
        /*
         * Your code here. After this method, your presenter (and view) will be completely destroyed
         * so make sure to cancel any HTTP call or database connection
         */

        super.onPresenterDestroyed();
    }

    @Override
    public ImageReader.OnImageAvailableListener getImageReaderListener(String dir) {
        return mInteractor.getImageReaderListener(dir);
    }

    @Override
    public Bitmap getBitmapFromCamera() {
        return mInteractor.getBitmapFromCamera();
    }

    @Override
    public Image getImageFromCamera() {
        return mInteractor.getImageFromCamera();
    }

    @Override
    public int[] getColors(int[] location, int[] areaSize) {
        return mInteractor.getColors(location, areaSize);
    }

    @Override
    public float getFinalValue(int[] colors) {
        return mInteractor.getFinalValue(colors);
    }

    public void setBitmapFromCamera(Bitmap b) {
        mInteractor.setBitmapFromCamera(b);
    }

    @Override
    public void deleteAFile(String dir) {
        mInteractor.deleteAFile(dir);
    }

    @Override
    public void calibOneSample(int[] v0) {
        mInteractor.calibOneSample(v0);
    }

    @Override
    public void calibTwoSample(int[] v1, int[] v2) {
        mInteractor.calibTwoSample(v1, v2);
    }

    @Override
    public void calibThreeSample(int[] v0, int[] v1, int[] v2) {
        mInteractor.calibThreeSample(v0, v1, v2);
    }
}