/*
 *  Created by Tri Phan on 20/03/19 11:42 AM
 *  Copyright (c) 2019 . All rights reserved.
 *  Last modified 19/03/19 8:42 PM
 */

package edu.flinders.crcapp.model;

import edu.flinders.crcapp.R;

public enum CalibrationEnum {
    MANUAL_CALIB(R.string.calib_advance_title, R.string.calib_advance_desc),
    THREE_CALIB(R.string.calib_three_point_title, R.string.calib_three_point_desc),
    TWO_CALIB(R.string.calib_two_point_title, R.string.calib_two_point_desc),
    ONE_CALIB(R.string.calib_one_point_title, R.string.calib_one_point_desc);

    private int mTitleResId;
    private int mDescResId;

    CalibrationEnum(int titleResId, int descResId) {
        mTitleResId = titleResId;
        mDescResId = descResId;
    }

    public int getTitleResId() {
        return mTitleResId;
    }

    public int getDescResId() {
        return mDescResId;
    }
}
