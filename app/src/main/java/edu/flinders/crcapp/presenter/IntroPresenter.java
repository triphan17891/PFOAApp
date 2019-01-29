/*
 *  Created by Tri Phan on 7/01/19 8:42 AM
 *  Copyright (c) 2019 . All rights reserved.
 *  Last modified 7/01/19 8:42 AM
 */

package edu.flinders.crcapp.presenter;

import android.app.Activity;
import android.os.Bundle;
import edu.flinders.crcapp.view.IntroView;

public interface IntroPresenter extends BasePresenter<IntroView> {
    void moveToOtherActivity(Activity a, Class<?> aName, Bundle bundle);
}