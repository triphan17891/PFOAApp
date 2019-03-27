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
import edu.flinders.crcapp.model.Calibration;
import edu.flinders.crcapp.model.Equation;
import edu.flinders.crcapp.model.GlobalUtils;

import java.io.*;
import java.nio.ByteBuffer;

public final class CameraInteractorImpl implements CameraInteractor {
    private Bitmap mBitmap;
    private Image mImage;
    private Equation mEquation = new Equation();

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
                    if (mBitmap != null) {
                        mBitmap.recycle();
                    }
                    mBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, null);
                    // Save the image onto the SD card
                    saveImage(bytes, file);
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
    public void setBitmapFromCamera(Bitmap b) {
        if (mBitmap != null) {
            mBitmap.recycle();
        }

        mBitmap = b;
    }

    @Override
    public Image getImageFromCamera() {
        return mImage;
    }

    @Override
    public int[] getColors(int[] location, int[] areaSize) {
        if (mBitmap == null) {
            return new int[]{0, 0, 0};
        }

        int x = location[0];
        int y = location[1];

        int sumRed = 0;
        int sumBlue = 0;
        int sumGreen = 0;
        int totalPixel = 0;

        for (int i = x; i < x + areaSize[0]; i++) {
            for (int j = y; j < y + areaSize[1]; j++) {
                int pixel = mBitmap.getPixel(i, j);

                sumRed = sumRed + Color.red(pixel);
                sumBlue = sumBlue + Color.blue(pixel);
                sumGreen = sumGreen + Color.green(pixel);

                //mBitmap.setPixel(i,j, Color.RED);
                totalPixel++;
            }
        }

        int avgRed = sumRed / totalPixel;
        int avgBlue = sumBlue / totalPixel;
        int avgGreen = sumGreen / totalPixel;

        return new int[]{avgRed, avgGreen, avgBlue};
    }

    @Override
    public float getFinalValue(int[] colors) {
        try {
            return mEquation.getFinalResult(colors[0], colors[1], colors[2]);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public void deleteAFile(String dir) {
        File file = new File(dir);
        try {
            file.getCanonicalFile().delete();
        } catch (Exception e) {
            Log.e("CameraInteractorImpl", "Could't delete the file: " + file.getAbsolutePath());
        }
    }

    @Override
    public void calibOneSample(int[] v0) {
        mEquation.calibOneSample(v0);
    }

    @Override
    public void calibTwoSample(int[] v1, int[] v2) {
        mEquation.calibTwoSamples(v1, v2);
    }

    @Override
    public void calibThreeSample(int[] v0, int[] v1, int[] v2) {
        mEquation.calibThreeSamples(v0, v1, v2);
    }
}