/*
 *  Created by Tri Phan on 7/01/19 8:30 PM
 *  Copyright (c) 2019 . All rights reserved.
 *  Last modified 7/01/19 8:30 PM
 */

package edu.flinders.crcapp.interactor;

import android.app.Activity;
import android.os.Bundle;

public interface CalibrationInteractor extends BaseInteractor {
    void moveToOtherActivity(Activity activity, Class<?> activityName, Bundle b);
}