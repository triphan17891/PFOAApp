/*
 *  Created by Tri Phan on 29/01/19 11:06 AM
 *  Copyright (c) 2019 . All rights reserved.
 *  Last modified 29/01/19 11:05 AM
 */

package edu.flinders.crcapp.model;

import android.util.DisplayMetrics;
import android.util.TypedValue;

public class GlobalUtils {

    /**
     * Providing a string by current time.
     * @return String formatted by current time in milliseconds (eg. 1349333576093.jpg)
     */
    public static String namedByTime() {
        return ((Long)System.currentTimeMillis()).toString() + ".jpg";
    }

    /**
     * Format to px by provided dp
     * @param dp
     * @param metrics
     * @return
     */
    public static int toPixels(int dp, DisplayMetrics metrics) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, metrics);
    }
}
