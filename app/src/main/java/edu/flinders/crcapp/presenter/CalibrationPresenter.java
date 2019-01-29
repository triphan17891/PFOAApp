/*
 *  Created by Tri Phan on 7/01/19 8:30 PM
 *  Copyright (c) 2019 . All rights reserved.
 *  Last modified 7/01/19 8:30 PM
 */

package edu.flinders.crcapp.presenter;

import android.app.Activity;
import android.os.Bundle;
import edu.flinders.crcapp.view.CalibrationView;

public interface CalibrationPresenter extends BasePresenter<CalibrationView> {
    void moveToOtherActivity(Activity a, Class<?> aName, Bundle b);
}