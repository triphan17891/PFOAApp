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
    int[] getColors(int[] location, int[] areaSize);
}