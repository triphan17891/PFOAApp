/*
 *  Created by Tri Phan on 7/01/19 8:42 AM
 *  Copyright (c) 2019 . All rights reserved.
 *  Last modified 7/01/19 8:42 AM
 */

package edu.flinders.crcapp.presenter.impl;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;

import edu.flinders.crcapp.presenter.IntroPresenter;
import edu.flinders.crcapp.view.IntroView;
import edu.flinders.crcapp.interactor.IntroInteractor;

import javax.inject.Inject;

public final class IntroPresenterImpl extends BasePresenterImpl<IntroView> implements IntroPresenter {
    /**
     * The interactor
     */
    @NonNull
    private final IntroInteractor mInteractor;

    // The view is available using the mView variable

    @Inject
    public IntroPresenterImpl(@NonNull IntroInteractor interactor) {
        mInteractor = interactor;
    }

    @Override
    public void onStart(boolean viewCreated) {
        super.onStart(viewCreated);

        // Your code here. Your view is available using mView and will not be null until next onStop()
    }

    @Override
    public void onStop() {
        // Your code here, mView will be null after this method until next onStart()
        super.onStop();
    }

    @Override
    public void onPresenterDestroyed() {
        /*
         * Your code here. After this method, your presenter (and view) will be completely destroyed
         * so make sure to cancel any HTTP call or database connection
         */
        super.onPresenterDestroyed();
    }

    @Override
    public void moveToOtherActivity(Activity a, Class<?> aName, Bundle bundle) {
        mInteractor.moveToOtherActivity(a, aName, bundle);
    }
}