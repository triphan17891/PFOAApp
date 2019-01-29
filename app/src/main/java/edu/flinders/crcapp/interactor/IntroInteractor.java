/*
 *  Created by Tri Phan on 7/01/19 8:42 AM
 *  Copyright (c) 2019 . All rights reserved.
 *  Last modified 7/01/19 8:42 AM
 */

package edu.flinders.crcapp.interactor;

import android.app.Activity;
import android.os.Bundle;

public interface IntroInteractor extends BaseInteractor {
    void moveToOtherActivity(Activity activity, Class<?> activityName, Bundle bundle);
}