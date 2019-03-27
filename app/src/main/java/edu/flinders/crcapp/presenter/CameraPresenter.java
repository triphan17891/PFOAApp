/*
 *  Created by Tri Phan on 7/01/19 10:22 PM
 *  Copyright (c) 2019 . All rights reserved.
 *  Last modified 7/01/19 10:22 PM
 */

package edu.flinders.crcapp.presenter;

import android.graphics.Bitmap;
import android.media.Image;
import android.media.ImageReader;
import edu.flinders.crcapp.view.CameraView;

public interface CameraPresenter extends BasePresenter<CameraView> {
    ImageReader.OnImageAvailableListener getImageReaderListener(String dir);
    Bitmap getBitmapFromCamera();
    Image getImageFromCamera();
    void setBitmapFromCamera(Bitmap b);
    int[] getColors(int[] location, int[] areaSize);
    float getFinalValue(int[] colors);
    void deleteAFile(String dir);
    void calibOneSample(int[] v0) throws Exception;
    void calibTwoSample(int[] v1, int[] v2) throws Exception;
    void calibThreeSample(int[] v0, int[] v1, int[] v2) throws Exception;
}