/*
 *  Created by Tri Phan on 7/01/19 10:22 PM
 *  Copyright (c) 2019 . All rights reserved.
 *  Last modified 7/01/19 10:22 PM
 */

package edu.flinders.crcapp.interactor.impl;

import javax.inject.Inject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.Image;
import android.media.ImageReader;
import android.util.Log;
import edu.flinders.crcapp.interactor.CameraInteractor;

import java.io.*;
import java.nio.ByteBuffer;

public final class CameraInteractorImpl implements CameraInteractor {
    private Bitmap mBitmap;
    private Image mImage;

    @Inject
    public CameraInteractorImpl() {
    }

    @Override
    public ImageReader.OnImageAvailableListener getImageReaderListener(String dir) {
        final File file = new File(dir);
        ImageReader.OnImageAvailableListener readerListener = new ImageReader.OnImageAvailableListener() {
            @Override
            public void onImageAvailable(ImageReader reader) {
                try {
                    mImage = reader.acquireLatestImage();
                    ByteBuffer buffer = mImage.getPlanes()[0].getBuffer();
                    byte[] bytes = new byte[buffer.capacity()];
                    buffer.get(bytes);
                    mBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, null);
                    // not save image on memory yet
                    //saveImage(bytes, file);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (mImage != null) {
                        mImage.close();
                    }
                }
            }
        };
        return readerListener;
    }

    public void saveImage(byte[] bytes, File file) throws IOException {
        OutputStream output = null;
        try {
            output = new FileOutputStream(file);
            output.write(bytes);
        } finally {
            if (null != output) {
                output.close();
            }
        }
    }

    @Override
    public Bitmap getBitmapFromCamera() {
        return mBitmap;
    }

    @Override
    public Image getImageFromCamera() {
        return mImage;
    }

    @Override
    public int[] getColors(int[] location, int[] areaSize) {
        if(mBitmap == null) {
            return new int[]{0, 0, 0};
        }

        int x = location[0];
        int y = location[1];

        int sumRed = 0;
        int sumBlue = 0;
        int sumGreen = 0;
        for(int i = x; i < x + areaSize[0]; i++) {
            for(int j = y; j < y + areaSize[1]; j++) {
                int pixel = mBitmap.getPixel(i,j);

                sumRed = sumRed + Color.red(pixel);
                sumBlue = sumBlue + Color.blue(pixel);
                sumGreen = sumGreen + Color.green(pixel);
            }
        }

        int totalPixel = areaSize[0] * areaSize[1];
        int avgRed = sumRed / totalPixel;
        int avgBlue = sumBlue / totalPixel;
        int avgGreen = sumGreen / totalPixel;

        return new int[]{avgRed, avgGreen, avgBlue};
    }
}