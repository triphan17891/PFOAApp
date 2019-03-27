/*
 *  Created by Tri Phan on 29/01/19 11:06 AM
 *  Copyright (c) 2019 . All rights reserved.
 *  Last modified 29/01/19 11:05 AM
 */

package edu.flinders.crcapp.model;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import java.io.IOException;
import java.io.InputStream;

public class GlobalUtils {

    /**
     * Providing a string by current time.
     *
     * @return String formatted by current time in milliseconds (eg. 1349333576093.jpg)
     */
    public static String namedByTime() {
        return ((Long) System.currentTimeMillis()).toString() + ".jpg";
    }

    /**
     * Format to px by provided dp
     *
     * @param dp
     * @param metrics
     * @return
     */
    public static int toPixels(int dp, DisplayMetrics metrics) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, metrics);
    }

    /**
     * Return the status bar height
     *
     * @param c current context of activity
     * @return
     */
    public static int getStatusBarHeight(Context c) {
        int result = 0;
        int resourceId = c.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = c.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * Return the action bar height
     *
     * @param c current context of activity
     * @return
     */
    public static int getActionBarHeight(Context c) {
        int actionBarHeight = 0;
        // Calculate ActionBar height
        TypedValue tv = new TypedValue();
        if (c.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, c.getResources().getDisplayMetrics());
        }

        return actionBarHeight;
    }


    /**
     * Resize the captured bitmap to fit the actual desired resolution
     *
     * @param b
     * @param actualSizes the size array with width is the first, height is the second child
     * @return resized bitmap
     */
    public static Bitmap resizeBitmapByScreenSize(Bitmap b, int[] actualSizes) {
        return Bitmap.createScaledBitmap(b, actualSizes[0], actualSizes[1], false);
    }

    /**
     * Get actual screen resolution
     *
     * @param a current activity
     * @return array of screen sizes
     */
    public static int[] getActualScreenSize(Activity a) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        a.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        return new int[]{width, height};
    }

    /**
     * Rotate an image if required.
     *
     * @param img           The image bitmap
     * @param selectedImage Image URI
     * @return The resulted Bitmap after manipulation
     */
    public static Bitmap rotateImageIfRequired(Context context, Bitmap img, Uri selectedImage) throws IOException {
        InputStream input = context.getContentResolver().openInputStream(selectedImage);
        ExifInterface ei;
        if (Build.VERSION.SDK_INT > 23)
            ei = new ExifInterface(input);
        else
            ei = new ExifInterface(selectedImage.getPath());

        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotateImage(img, 90);
//            case ExifInterface.ORIENTATION_ROTATE_180:
//                return rotateImage(img, 180);
            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotateImage(img, 270);
            default:
                return img;
        }
    }

    /**
     * Do the rotation an image then recycle it for memory purpose by using the provided degree.
     *
     * @param img    the source
     * @param degree the rotation degree
     * @return
     */
    public static Bitmap rotateImage(Bitmap img, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
        img.recycle();
        return rotatedImg;
    }
}
