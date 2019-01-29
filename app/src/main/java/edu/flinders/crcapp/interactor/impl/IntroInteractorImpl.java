/*
 *  Created by Tri Phan on 7/01/19 8:42 AM
 *  Copyright (c) 2019 . All rights reserved.
 *  Last modified 7/01/19 8:42 AM
 */

package edu.flinders.crcapp.interactor.impl;

import javax.inject.Inject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import edu.flinders.crcapp.R;
import edu.flinders.crcapp.interactor.IntroInteractor;

public final class IntroInteractorImpl implements IntroInteractor {
    @Inject
    public IntroInteractorImpl() {

    }

    @Override
    public void moveToOtherActivity(Activity activity, Class<?> actName, Bundle bundle) {
        Intent i = new Intent(activity.getApplicationContext(), actName);
        if(bundle != null) {
            i.putExtras(bundle);
        }
        activity.startActivity(i);
    }
}