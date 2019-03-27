/*
 *  Created by Tri Phan on 7/01/19 10:22 PM
 *  Copyright (c) 2019 . All rights reserved.
 *  Last modified 7/01/19 10:22 PM
 */

package edu.flinders.crcapp.view;

import android.graphics.Bitmap;
import android.support.annotation.UiThread;
import android.view.View;
import edu.flinders.crcapp.model.ProcessCallback;

@UiThread
public interface CameraView {
    void takePicture(View view, ProcessCallback processCallback);
    void createCameraPreview();
    void openCamera();
    void updatePreview();
    void closeCamera();
    void startBackgroundThread();
    void stopBackgroundThread();
    void setupViewByContext(int viewId);
    void performActionButton(View view);
}