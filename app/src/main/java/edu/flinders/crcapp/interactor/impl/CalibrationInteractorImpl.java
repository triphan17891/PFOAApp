/*
 *  Created by Tri Phan on 7/01/19 8:30 PM
 *  Copyright (c) 2019 . All rights reserved.
 *  Last modified 7/01/19 8:30 PM
 */

package edu.flinders.crcapp.interactor.impl;

import javax.inject.Inject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import edu.flinders.crcapp.interactor.CalibrationInteractor;

public final class CalibrationInteractorImpl implements CalibrationInteractor {
    @Inject
    public CalibrationInteractorImpl() {

    }

    @Override
    public void moveToOtherActivity(Activity activity, Class<?> activityName, Bundle b) {
        Intent i = new Intent(activity.getApplicationContext(), activityName);
        if(b != null) {
            i.putExtras(b);
        }
        activity.startActivity(i);
    }
}