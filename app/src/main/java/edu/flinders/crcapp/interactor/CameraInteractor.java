/*
 *  Created by Tri Phan on 7/01/19 10:22 PM
 *  Copyright (c) 2019 . All rights reserved.
 *  Last modified 7/01/19 10:22 PM
 */

package edu.flinders.crcapp.interactor;

import android.graphics.Bitmap;
import android.media.Image;
import android.media.ImageReader;

public interface CameraInteractor extends BaseInteractor {
    ImageReader.OnImageAvailableListener getImageReaderListener(String dir);
    Bitmap getBitmapFromCamera();
    Image getImageFromCamera();
    int[] getColors(int[] location, int[] areaSize);

}